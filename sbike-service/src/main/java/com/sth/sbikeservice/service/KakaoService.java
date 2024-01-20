package com.sth.sbikeservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sth.sbikeservice.model.dto.KakoDTO;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.repository.KaKaoRepository;
import com.sth.sbikeservice.repository.SbikeRepository;
import com.sth.sbikeservice.vo.NearSbike;
import com.sth.sbikeservice.vo.ResponseEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoService {

    private final KaKaoRepository kaKaoRepository;
    private final SbikeService sbikeService;

    Environment env;

    @Autowired
    public KakaoService(KaKaoRepository kaKaoRepository, Environment env, SbikeService sbikeService) {

        this.kaKaoRepository = kaKaoRepository;
        this.env = env;
        this.sbikeService = sbikeService;
    }

    public List<KakoDTO> listKakao() {
        List<KaKao> list = kaKaoRepository.findAll();
        List<KakoDTO> resultList = new ArrayList<>();
        list.forEach(kaKao -> resultList.add(kaKao.toDto()));
        return resultList;
    }

    public List<KakoDTO> listKakaoByEventId(String eventId) {
        List<KaKao> events = kaKaoRepository.findByEventId(Long.valueOf(eventId));
        List<KakoDTO> resultList = new ArrayList<>();
        for (KaKao event : events) {
            resultList.add(event.toDto());
        }
        return resultList;
    }

    @CircuitBreaker(name = "basicCircuitBreaker", fallbackMethod = "fallbackForCreateKakao")
    public void createKakao() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String eventApiUrl = "http://" + env.getProperty("gateway") + "/api/v1/event-service/events";
            ResponseEvent[] responsEvents = restTemplate.getForObject(eventApiUrl, ResponseEvent[].class);
            List<SbikeDTO> sbikeDTOList = sbikeService.listSbike();

            if (!sbikeDTOList.isEmpty()) {
                List<KaKao> kaKaoList = new ArrayList<>();

                for (ResponseEvent responseEvent : responsEvents) {
                    long eventId = responseEvent.getEventId();
                    String eventName = responseEvent.getEventNm();
                    double eventLatitude = responseEvent.getLat();
                    double eventLongitude = responseEvent.getLot();

                    // DB에 이미 있는 데이터인지 확인
                    if (kaKaoRepository.existsByEventName(eventName)) {
                        System.out.println("이미 존재한 데이터입니다: EventName=" + eventName);
                        continue;
                    }

                    String origin = eventLongitude + "," + eventLatitude + ",name=" + eventName;

                    // 각 이벤트에 대한 정류장 거리 계산 및 KaKao 엔티티 생성
                    KaKao kakao = KaKao.builder()
                            .eventId(eventId)
                            .eventName(eventName)
                            .build();

                    List<NearSbike> nearSbikes = new ArrayList<>();

                    for (SbikeDTO sbikeDTO : sbikeDTOList) {
                        double stationLongitude = Double.parseDouble(sbikeDTO.getStationLongitude());
                        double stationLatitude = Double.parseDouble(sbikeDTO.getStationLatitude());

                        // 범위 내의 데이터만 처리
                        if (stationLongitude >= eventLongitude - 0.015 && stationLongitude <= eventLongitude + 0.015
                                && stationLatitude >= eventLatitude - 0.015 && stationLatitude <= eventLatitude + 0.015) {

                            String destination = stationLongitude + "," + stationLatitude;

                            int distance = getDistance(origin, destination);

                            NearSbike nearSbike = NearSbike.builder()
                                    .sbikeDTO(sbikeDTO)
                                    .distance(distance)
                                    .build();

                            nearSbikes.add(nearSbike);
                        }
                    }

                    // 거리를 기준으로 정렬
                    nearSbikes.sort(Comparator.comparingInt(NearSbike::getDistance));

                    List<Sbike> sbikes = new ArrayList<>();
                    // 상위 3개의 데이터만 선택
                    int max = Math.min(3, nearSbikes.size());
                    for(int i = 0; i < max; i++) {
                        sbikes.add(nearSbikes.get(i).getSbikeDTO().toEntity());
                    }
                    kakao.setSbike(sbikes);

                    kaKaoRepository.save(kakao); // 이벤트 당 상위 3개 저장
                }
            } else {
                System.out.println("리스트에 사용 가능한 데이터가 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("이벤트 데이터를 가져오거나 API 호출에 실패했습니다.");
        }
    }
    public void fallbackForCreateKakao(Exception e) {
        log.error("CreateKakao 실행 중 예외 발생: " + e.getMessage());
    }

    @CircuitBreaker(name = "basicCircuitBreaker", fallbackMethod = "fallbackForGetDistance")
    public int getDistance(String origin, String destination) {
        try {
            // 카카오디벨로퍼스에서 발급 받은 REST API 키
            String REST_API_KEY = "b607b434b034948b1f4dcba5efc74551";

            // 요청 URL 생성
            String baseUrl = "https://apis-navi.kakaomobility.com/v1/directions";
            String waypoints = "";
            String priority = "RECOMMEND";
            String carFuel = "GASOLINE";
            String carHipass = "false";
            String alternatives = "false";
            String roadDetails = "false";

            StringBuilder queryParams = new StringBuilder();
            queryParams.append("origin=").append(URLEncoder.encode(origin, "UTF-8"));
            queryParams.append("&destination=").append(URLEncoder.encode(destination, "UTF-8"));
            queryParams.append("&waypoints=").append(URLEncoder.encode(waypoints, "UTF-8"));
            queryParams.append("&priority=").append(URLEncoder.encode(priority, "UTF-8"));
            queryParams.append("&car_fuel=").append(URLEncoder.encode(carFuel, "UTF-8"));
            queryParams.append("&car_hipass=").append(URLEncoder.encode(carHipass, "UTF-8"));
            queryParams.append("&alternatives=").append(URLEncoder.encode(alternatives, "UTF-8"));
            queryParams.append("&road_details=").append(URLEncoder.encode(roadDetails, "UTF-8"));

            URL url = new URL(baseUrl + "?" + queryParams.toString());

            // HttpURLConnection 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 요청 메서드 설정 (GET)
            connection.setRequestMethod("GET");

            // 인증 정보 추가
            connection.setRequestProperty("Authorization", "KakaoAK " + REST_API_KEY);

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();

            // 응답이 성공(200)일 경우 데이터 읽기
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // 응답 내용 출력
//                    System.out.println("Response:\n" + response.toString());

                    // JSON 파싱하여 distance 값 추출
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(response.toString());
                    int distance = jsonNode
                            .path("routes")
                            .path(0)
                            .path("summary")
                            .path("distance")
                            .asInt();

                    // 추출한 distance 값 출력
                    System.out.println("둘 사이 거리: " + distance + " 미터");

                    // 추출한 distance 값 반환
                    return distance;
                }
            } else {
                // 에러 발생 시 에러 코드 출력
                System.out.println("HTTP Request Failed with error code: " + responseCode);
                return -1; // 에러 발생 시 -1 반환 또는 다른 방식으로 처리
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 예외 발생 시 -1 반환 또는 다른 방식으로 처리
        }
    }

    public void fallbackForGetDistance(Exception e) {
        log.error("GetDistance 실행 중 예외 발생: " + e.getMessage());
    }
}