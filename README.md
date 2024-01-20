# 따릉이로 문화생활
서울시의 문화생활을 따릉이로 즐겨보아요 <br><br>
🏆 SW전문인재양성사업 프로젝트개발 경진대회 **대상** 수상<br>
🏆 헥토그룹 서경SW아카데미 프로젝트 발표회 **골드상** 수상<br>
#### <a href="https://sw-sth.notion.site/898e9ed7b37e4d4e8170a86defd68769">노션 프로젝트 소개</a>

## 🖥️프로젝트 소개
#### 스프링 클라우드 모듈을 활용한 MSA 개발과 분산처리 환경의 경험

### 주제
서울시 문화행사가 예정되어있는 곳의 인구 밀집, 혼잡도와 해당 지역의 교통혼잡도 및 따릉이 정보를 지도에 보여주는 서비스

### 목적
큰 행사에는 사람이 많이 몰리기 때문에 교통 체증이 발생 → 자동차, 대중교통을 이용할지 따릉이를 이용할지 도움을 주는 목적

## 🕰️개발 기간
- 23.10.25 ~ 23.12.19

### 🧑‍🤝‍🧑팀원
- 팀장 : 김성태
- 조원 : 박태현

## ⚙️개발 환경
- `Java 17` 
- `JDK 17` 
- **IDE** : Intellij 
- **Framework** : 스프링부트 3.1.5 
- **Front** : 리액트
- **DataBase** : MySQL, Redis
- **ORM** : JPA
- **Server** : KT Cloud
- **Release** : Docker

## 📌주요 기능
#### 대시보드
- 월별, 주간별 문화행사 수 통계
- 일별 발급된 쿠폰 수 통계
- 문화행사 개최된 지역 순위
- 현재 방문자수가 가장 많은 지역 순위
![대시보드](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/923d473b-f137-4716-9804-758e640ddedf)

#### 지도
- 개최되고 있는 문화행사 위치 핀
- 핀 마커 클릭으로 간단한 정보 제공
- 실시간 교통혼잡도 보기
![지도](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/720c5076-ef84-46f6-aac0-cad0afecf86b)
#### 문화행사 리스트
- 문화행사 리스트 보기
- 페이징 처리
![리스트](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/7c8a54e6-2770-42f6-880d-fb3164a611f3)
#### 디테일
- 문화행사 세부정보
- 지역 혼잡도 정보
- 가까운 따릉이 정보
- 문화행사 명 클릭시 해당 위치 지도에 표시 후 가까운 따릉이 대여소 위치 제공
![디테일](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/978ea2d3-251c-4a54-b778-631bab7be144)
![디테일 지도](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/7185ef78-eca5-4630-ac7c-eef01145fd3c)
#### 유저 프로필
- 유저 정보
- 보유 쿠폰
![프로필](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/178b8981-119f-44e9-97dd-6cf83b6be06d)
#### 쿠폰
- Redis를 사용한 선착순 쿠폰 발급
- 일정주기 자동 쿠폰 생성
![쿠폰](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/7e86381a-14d9-4999-9f7b-ba5c8040f7a2)
64efab-5717-4322-804b-0c0555c933a2)
#### 로그인
- 회원가입
- 로그인시 JWT 토큰 발급
![회원가입](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/22ae4702-3945-4bed-b5fb-3f328131595c)
![로그인](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/12fb1396-0809-4064-88ee-5949e6a6b545)

## 💻주요 기술
- 외부 API 호출을 통한 공공 데이터 처리 
- JWT 토큰을 사용한 로그인 기능 
- 로그인을 사용하여 얻은 JWT 토큰을 활용하여 API 게이트웨이를 통한 서비스 간 통신
- Redis를 통한 동시성 처리를 진행한 쿠폰 서비스
- 로드 밸런싱을 통한 부하 처리
- 방해 전파 방지를 위한 서킷 브레이커 사용
