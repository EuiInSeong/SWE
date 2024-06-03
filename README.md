
# BE
spring
# 생각해보기

### MVC 패턴 적용
MVC 패턴을 적용하여 프로젝트를 구성
* MVC 패턴: Model, View, Controller의 약자로, 소프트웨어를 세 가지의 역할로 구분한 디자인 패턴
  * Model: 데이터베이스와 연결되어 데이터를 가져오거나 저장하는 역할
  * View: 사용자에게 보여지는 화면
  * Controller: 사용자의 요청을 받아서 데이터를 처리하고 결과를 View에 전달하는 역할
* Spring Boot에서 MVC 패턴을 적용하기 위한 나머지 
보통 View는 프론트엔드에서 처리하므로, Spring Boot에서는 Controller와 Model만 구현하면 된다.
  * Service: 비즈니스 로직을 처리하는 역할
  * Repository: 데이터베이스와 직접적으로 연결되어 데이터를 가져오거나 저장하는 역할
  * Entity: 데이터베이스의 테이블과 매핑되는 객체
  * DTO: 데이터 전송 객체로서, Entity와 View 사이에서 데이터를 전달하는 역할
  * Request, Response: 사용자의 요청과 응답을 담당하는 객체


### 개발자 자동 할당 알고리즘
새로 생성된 티켓의 컴포너트를 파악해 어떤 분야의 이슈(티켓)인지 파악하고 많이 개발해봤으면서도 덜 바쁜 사람에게 우선 할당한다

* Ticket
* Controller
  * 생선된 티켓의 컴포넌트를 정보 서비스에 넘김 
* Service:
  * 해당 티켓의 프로젝트와 유저를 기준으로 개발자 찾기 
  * 개발자에게 할당된 티켓 수를 entry로 매핑
  * 비교& return
  * 
### Get 으로 조회시 문제점
get 으로 조회시 Entitiy가 그대로 노출되면 Domian 단에 프록시.Lazy를 걸어뒀기 때문에
임의 값으로 인한 에러가 뜰수 있다.엔티티를 고대로 노출하지 않는 습관
api 명세가 바뀔 것을 대비해서 Get은 컨트롤러 단에서 Result<> Generic으로 감싸줘야함 


# 함수 및 기능, 로직 설명
## 1. 티켓 관련 로직

* ### 티켓 생성
(optioal) 이슈(티켓) 생성 시 개발자 자동 할당 알고리즘
Ticket 생성시 자동으로 현재 시간 찍혀야 함
* createTicket

티켓을 생성 시 같은 프로젝트에 참여하고 있는 Member 중에 UserType이 Developer인 사람에게 티켓을 자동 할당해야함 
이때 현재 발행 한 티켓의 Component 가 같은 티켓 중에  (UserType이 Developer인 사람에게) 티켓의 DeveloperID에 가장 많이 들어있는 사람(DeveloperID가 많다는 것은 같은 컴포넌트 티켓을 처리해본 경험이 많다고 가정) 을 자동으로 할당하고, 
만약 그 수가 2명 이상이면, 현재 Developer에게 할당된 티켓 중 Status 가 assigned가 가장 적은 사람에게(가장 덜 바쁠 것이라는 가정) 할당. 
만약 그 수가 2명 이상이면 id가 작은대로 할당해야 함.

* ### 티켓 (이슈) 검색 기능
* readAssignedTicketList
* readNewTicketList
* readResolvedTicketList
* readReopenedTicketList
* readClosedTicketList

현재 진행 중인 프로젝트 클릭 시 프로젝트 내 티켓(이슈) 중, status에 따라 분류하여 볼수 있어야 함.
이에 따라 검색하여 보길 원하는 프로젝트 내 project id를 받아와 해당 프로젝트 내 티켓을 status에 따라 분류하여 보여주는 기능을 구현

### 티켓 (이슈) 통계 기능 : 
* readTicketStatics

projectId로 프로젝트 내에 포함된 티켓을 찾아서
오늘을 기준으로 createdTime이 오늘인 Ticket의 Status 별 갯수와 priority 별 갯수,
마찬가지로 createdTime이 이번 달인  Ticket의 Status 별 갯수와 priority 별 갯수, 를 따로 통계치를 보여 줘야
현재 시점 일,월의 info 를 반환 받아, 이번달, 오늘의 티켓 상태(status), 우선 순위(priority) 별 카운트하여 프론트 측에 전달

* ### 티켓 (이슈) 상세 조회
* readTicketDetail

티켓을 클릭시 티켓에 달린 댓글을 포함해서 티켓의 상세 정보를 볼수있어야함

* ### 티켓 (이슈) status 변경
* updateTicketStatus(수정 중)
projectId로 프로젝트 와 UserId 를 통해 Member테이블의 UserType을 찾아
UserType이 ProjectLeader이면 티켓의 Status를 Assigned,Closed로 변경 할 수 있다.
UserType이 Developer 면  티켓의 Status를 Resolved로 변경 할 수 있다.
UserType이 Tester 면 New,Reopened 로 티켓의 Status를 로 변경 할 수 있다.
-> 전체 수정? 
* ### 티켓 (이슈) 삭제



## 유저

## 프로젝트


## 댓글
댓글은 티켓에만 달 수 있음을 기반으로 구현

### Additional Links : 참고 자료
* [생성 날짜를 자동으로 찍자](https://ozofweird.tistory.com/entry/%EC%82%BD%EC%A7%88-%ED%94%BC%ED%95%98%EA%B8%B0-Spring-Boot-%EB%82%A0%EC%A7%9C-%EB%8B%A4%EB%A3%A8%EA%B8%B0?category=938335)
* [REST 튜토리얼](https://spring.io/guides/tutorials/rest/)
* [Submission 핸들링하기](https://spring.io/guides/gs/handling-form-submission/)
* [JPA 공식 문서](https://spring.io/guides/gs/accessing-data-jpa/)
* [JPA 공식 문서2](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [API명세서를 자동으로 써보자 : Swagger 버전 바뀜 참고](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#web.security)


