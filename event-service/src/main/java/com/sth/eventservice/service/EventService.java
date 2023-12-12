// EventService.java
package com.sth.eventservice.service;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import com.sth.eventservice.repository.EventRepository;
import com.sth.eventservice.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.time.DayOfWeek;
import java.time.format.TextStyle;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final RestTemplate restTemplate;


    @Autowired
    public EventService(EventRepository eventRepository, RestTemplate restTemplate) {
        this.eventRepository = eventRepository;
        this.restTemplate = restTemplate;
    }

    public List<EventDTO> listEvent() {
        List<Event> list = eventRepository.findAll();
        List<EventDTO> resultList = new ArrayList<>();
        list.forEach(event -> resultList.add(event.toDto()));
        return resultList;
    }

    public Page<EventDTO> listEventPaging(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        return page.map(Event::toDto);
    }

    public void addEvent(EventDTO eventDTO) {
        eventRepository.save(eventDTO.toEntity());
    }

    public void addEvents(List<EventDTO> eventDTOList) {
        List<Event> eventList = eventDTOList.stream()
                .map(EventDTO::toEntity)
                .collect(Collectors.toList());
        eventRepository.saveAll(eventList);
    }


    public EventDTO getEventsByEventNm(String eventNm) {
        Event event = eventRepository.findByEventNm(eventNm);
        if (event != null) {
            return event.toDto();
        }
        return null;
    }

    @Transactional
    public void saveEventsFromXml() {
        List<EventDTO> eventDTOList = callApiAndParseXml();
        for (EventDTO eventDTO : eventDTOList) {
            if (eventDTO != null) {
                eventRepository.save(eventDTO.toEntity());
            }
        }
    }

    public void saveEvents() {
        String areaApiUrl = "http://localhost:8000/api/v1/area-service/areas";
        AreaResponse[] areas = restTemplate.getForObject(areaApiUrl, AreaResponse[].class);

        List<EventDTO> eventDTOList = new ArrayList<>();

        for (AreaResponse area : areas) {
            int startPage = 1;
            int pageSize = 100;
            String areaname = area.getAreaNm();
            String apiUrl = "http://openapi.seoul.go.kr:8088/48435455656b617238305977625a75/xml/citydata/" + startPage + "/" + pageSize + "/" + areaname;

            try {
                ResponseEntity<EventResponse> responseEntity = restTemplate.getForEntity(apiUrl, EventResponse.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    EventResponse response = responseEntity.getBody();
                    if (response != null && response.getCitydata().getEvents() != null && !response.getCitydata().getEvents().isEmpty()) {
                        List<EventStts> eventSttsList = response.getCitydata().getEvents();
                        List<EventDTO> eventsFromPage = eventSttsList.stream()
                                .map(eventStts -> EventDTO.builder()
                                        .areaNm(areaname)
                                        .eventNm(eventStts.getEVENT_NM())
                                        .build())
                                .collect(Collectors.toList());
                        eventDTOList.addAll(eventsFromPage);
                        logger.info("API 호출 시작");
                        System.out.println("API 호출 중");
                        logger.info("API 호출 끝");

                    } else {
                        logger.info("API 응답에서 이벤트 정보를 찾을 수 없습니다.");
//                        System.out.println("API 응답에서 이벤트 정보를 찾을 수 없습니다.");
                    }
                } else {
                    logger.warn("API 호출 실패");
                    System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                }
            } catch (Exception e) {
                logger.warn("API 호출 중 오류 발생");
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }

        addEvents(eventDTOList);
    }

    private List<EventDTO> callApiAndParseXml() {
        int firstPage = 1;
        int lastPage = 1000;
        int maxPage = 4000;
        List<EventDTO> eventDTOList = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        while (firstPage <= maxPage) {
            String apiUrl = "http://openapi.seoul.go.kr:8088/71684451416f75723738574b486156/xml/culturalEventInfo/" + firstPage + "/" + (firstPage + lastPage - 1);
            try {
                ResponseEntity<EventResponseTotal> responseEntity = restTemplate.getForEntity(apiUrl, EventResponseTotal.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    EventResponseTotal response = responseEntity.getBody();
                    if (response != null && response.getEvents() != null && !response.getEvents().isEmpty()) {
                        List<EventDTO> eventsFromPage = response.getEvents().stream()
                                .map(eventdata -> {
                                    Event event = eventRepository.findByEventNm(eventdata.getTitle());
                                    if (event == null) {
                                        return null;
                                    }
                                    EventDTO eventDTO = event.toDto();
                                    eventDTO.setTitle(eventdata.getTitle());
                                    eventDTO.setCodename(eventdata.getCodeName());
                                    LocalDate startDate = LocalDate.parse(eventdata.getStartDate().substring(0, 10));
                                    LocalDate endDate = LocalDate.parse(eventdata.getEndDate().substring(0, 10));
                                    eventDTO.setStrtdate(startDate);
                                    eventDTO.setEndDate(endDate);
                                    eventDTO.setPlace(eventdata.getPlace());
                                    eventDTO.setUseFee(eventdata.getUseFee());
                                    eventDTO.setPlayer(eventdata.getPlayer());
                                    eventDTO.setProgram(eventdata.getProgram());
                                    eventDTO.setOrgLink(eventdata.getOrgLink());
                                    eventDTO.setLot(eventdata.getLat());
                                    eventDTO.setLat(eventdata.getLot());
                                    eventDTO.setGuname(eventdata.getGuname());
                                    eventDTO.setMainImg(eventdata.getMainImg());
                                    return eventDTO;
                                })
                                .collect(Collectors.toList());
                        eventDTOList.addAll(eventsFromPage);
                        logger.info("DB 저장 완료");
                    }
                } else {
                    logger.warn("API 호출 실패");
                    System.out.println("API 호출이 실패했습니다. 상태 코드: " + responseEntity.getStatusCodeValue());
                }
            } catch (Exception e) {
                logger.warn("API 호출 실패");
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
            firstPage += lastPage;
        }
        return eventDTOList;
    }


    //////////////////////////////////////////////////////////////////////

    public ResponseEntity<Map<String, Object>> getEventCountByDayAndMonth() {
        Map<String, Object> result = new HashMap<>();

        // 현재 날짜 구하기
        LocalDate currentDate = LocalDate.now();

        // 현재 월 포함하여 7개의 월 계산
        List<String> monthList = new ArrayList<>();
        int currentMonthValue = currentDate.getMonthValue();
        for (int i = 0; i < 7; i++) {
            Month month = Month.of((currentMonthValue + i - 1) % 12 + 1); // 1부터 12까지 순환
            String monthName = month.getDisplayName(TextStyle.FULL, Locale.getDefault());
            monthList.add(monthName);
        }

        // 현재 주의 시작 날짜와 끝나는 날짜 계산
        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = currentDate.with(DayOfWeek.SUNDAY);

        // 일별 이벤트 건수를 저장할 Map 초기화 (요일 순서대로)
        Map<String, Integer> eventCountByDayOfWeek = new LinkedHashMap<>();
        eventCountByDayOfWeek.put("월요일", 0);
        eventCountByDayOfWeek.put("화요일", 0);
        eventCountByDayOfWeek.put("수요일", 0);
        eventCountByDayOfWeek.put("목요일", 0);
        eventCountByDayOfWeek.put("금요일", 0);
        eventCountByDayOfWeek.put("토요일", 0);
        eventCountByDayOfWeek.put("일요일", 0);

        // 월별 이벤트 건수를 저장할 Map 초기화 (월 순서대로)
        Map<String, Integer> eventCountByMonth = new LinkedHashMap<>();
        for (String monthName : monthList) {
            eventCountByMonth.put(monthName, 0);
        }

        Iterable<EventDTO> eventList = listEvent();

        // 각 이벤트의 시작 날짜와 끝나는 날짜를 기준으로 요일 및 월을 계산하고 건수를 집계
        for (EventDTO event : eventList) {
            LocalDate startDate = event.getStrtdate();
            LocalDate endDate = event.getEndDate();
            if (startDate != null && endDate != null) {
                // 현재 주에 해당하는 이벤트만 집계
                if (!startDate.isAfter(endOfWeek) || !endDate.isBefore(startOfWeek)) {
                    // 요일 계산
                    DayOfWeek dayOfWeek = startDate.getDayOfWeek();
                    String dayOfWeekName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());

                    // 해당 요일의 이벤트 건수 증가
                    eventCountByDayOfWeek.put(dayOfWeekName, eventCountByDayOfWeek.get(dayOfWeekName) + 1);

                    // 월 계산
                    Month month = startDate.getMonth();
                    String monthName = month.getDisplayName(TextStyle.FULL, Locale.getDefault());

                    // 현재 월부터 7개의 월까지의 이벤트 건수 증가
                    if (monthList.contains(monthName)) {
                        eventCountByMonth.put(monthName, eventCountByMonth.getOrDefault(monthName, 0) + 1);
                    }
                }
            }
        }

        // 결과 맵에 현재 월부터 7개의 월까지의 월별 이벤트 건수와 현재 주의 일별 이벤트 건수를 추가
        result.put("Monthly Event", eventCountByMonth);
        result.put("Weekly Event", eventCountByDayOfWeek);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    private Map<String, Integer> initializeEventCountMap() {
        Map<String, Integer> eventCountByDayOfWeek = new HashMap<>();
        eventCountByDayOfWeek.put("월요일", 0);
        eventCountByDayOfWeek.put("화요일", 0);
        eventCountByDayOfWeek.put("수요일", 0);
        eventCountByDayOfWeek.put("목요일", 0);
        eventCountByDayOfWeek.put("금요일", 0);
        eventCountByDayOfWeek.put("토요일", 0);
        eventCountByDayOfWeek.put("일요일", 0);

        Map<String, Integer> eventCountByMonth = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            eventCountByMonth.put(i + "월", 0);
        }

        return eventCountByDayOfWeek;
    }

    /////////////////////////////////////////////////////////////////////////////////

    public ResponseEntity<Map<String, Object>> getMonthlyEventByArea(int month) {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Integer> monthlyEventByArea = new LinkedHashMap<>();

        // 입력된 월(month)을 기준으로 해당 월의 지역별 문화행사 건수를 계산
        LocalDate startDateOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDate endDateOfMonth = startDateOfMonth.withDayOfMonth(startDateOfMonth.lengthOfMonth());

        Iterable<EventDTO> eventList = listEvent();

        for (EventDTO event : eventList) {
            String areaName = event.getAreaNm();
            LocalDate eventStartDate = event.getStrtdate();
            LocalDate eventEndDate = event.getEndDate();

            if (eventStartDate != null && eventEndDate != null) {
                // 이벤트가 해당 월에 포함되는지 확인
                if (!eventEndDate.isBefore(startDateOfMonth) && !eventStartDate.isAfter(endDateOfMonth)) {
                    // 해당 지역구의 이벤트 건수 증가
                    int currentCount = monthlyEventByArea.getOrDefault(areaName, 0);
                    monthlyEventByArea.put(areaName, currentCount + 1);
                }
            }
        }

        // 결과 맵에 지역구별 월별 이벤트 건수 추가
        result.put(month + "월 지역구별 문화행사", monthlyEventByArea);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}