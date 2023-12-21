# MSA_CultureWithSBike
서울시의 문화생활을 따릉이로 즐겨보아요 <br>
#### <a href="https://sw-sth.notion.site/898e9ed7b37e4d4e8170a86defd68769">노션 프로젝트 소개</a>

## 🖥️프로젝트 소개
#### 스프링 클라우드 모듈을 활용한 MSA 개발과 분산처리 환경의 경험

### 주제
큰 행사가 예정되어있는 곳의 예상 인구 밀집, 혼잡도 +교통혼잡도와 따릉이를 지도에 보여주는 서비스

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
#### 지도
- 문화행사 핀
- 마커 클릭으로 간단정보
- 교통혼잡도 보기
#### 문화행사 리스트
- 문화행사 리스톨 보기
- 페이징 처리
#### 디테일
- 문화행사 세부정보
- 지역 혼잡도 정보
- 가까운 따릉이 정보
#### 유저 프로필
- 유저 정보
- 보유 쿠폰
#### 쿠폰
- 선착순 쿠폰 발급
- 자동 쿠폰 생성
#### 로그인
- 회원가입
- 로그인시 JWT 토큰 발급

## 💻주요 기술
- 외부 API 호출을 통한 공공 데이터 처리 
- JWT 토큰을 사용한 로그인 기능 
- 로그인을 사용하여 얻은 토큰을 활용하여 API 게이트웨이를 통한 서비스 간 통신
- 동시성 처리를 진행한 쿠폰 서비스
- 로드 밸런싱을 통한 부하 처리
- 방해 전파 방지를 위한 서킷 브레이커 사용
