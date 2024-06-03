# BE
spring

# 생각해보기

### MVC 패턴 적용
MVC 패턴을 적용하여 프로젝트를 구성
* **MVC 패턴**: Model, View, Controller의 약자로, 소프트웨어를 세 가지의 역할로 구분한 디자인 패턴
  * **Model**: 데이터베이스와 연결되어 데이터를 가져오거나 저장하는 역할
  * **View**: 사용자에게 보여지는 화면
  * **Controller**: 사용자의 요청을 받아서 데이터를 처리하고 결과를 View에 전달하는 역할
* Spring Boot에서 MVC 패턴을 적용하기 위한 나머지 
보통 View는 프론트엔드에서 처리하므로, Spring Boot에서는 Controller와 Model만 구현하면 된다.
  * **Service**: 비즈니스 로직을 처리하는 역할
  * **Repository**: 데이터베이스와 직접적으로 연결되어 데이터를 가져오거나 저장하는 역할
  * **Entity**: 데이터베이스의 테이블과 매핑되는 객체
  * **DTO**: 데이터 전송 객체로서, Entity와 View 사이에서 데이터를 전달하는 역할
  * **Request, Response**: 사용자의 요청과 응답을 담당하는 객체

### 개발자 자동 할당 알고리즘
새로 생성된 티켓의 컴포너트를 파악해 어떤 분야의 이슈(티켓)인지 파악하고 많이 개발해봤으면서도 덜 바쁜 사람에게 우선 할당한다

* **Ticket**
* **Controller**
  * 생성된 티켓의 컴포넌트를 정보 서비스에 넘김 
* **Service**
  * 해당 티켓의 프로젝트와 유저를 기준으로 개발자 찾기 
  * 개발자에게 할당된 티켓 수를 entry로 매핑
  * 비교 & return

### Get 으로 조회시 문제
Get으로 조회시 Entity가 그대로 노출되면 Domain 단에 프록시.Lazy를 걸어뒀기 때문에 임의 값으로 인한 에러가 뜰 수 있다. 엔티티를 그대로 노출하지 않는 습관을 가져야 한다. API 명세가 바뀔 것을 대비해서 Get은 컨트롤러 단에서 Result<> Generic으로 감싸줘야 함.

# 함수 및 기능, 로직 설명
## [유저]
### 유저 로그인 : login(loginReq)
유저의 이메일을 통해 유저를 조회한 뒤, 해당 유저의 비밀번호 일치 여부를 확인하고 이메일과 비밀번호가 일치하지 않을 시 에러 처리. 일치하면 userId를 프론트에 보내 캐시에 저장.

### 회원 가입 : signup
CreateUserReq(CreateUserReq) :
유저의 정보를 DB에 저장.

## [프로젝트]
### 프로젝트 생성 : createProject
프로젝트의 이름, 설명과 참여하는 유저들을 리스트로 추가. 이때 유저는 여러 프로젝트에 참여할 수 있어 프로젝트:유저가 다대다 관계이기 때문에 Member 테이블을 생성함. 따라서 실제로 DB에 저장되는 것은 Member 리스트임. 따라서 이메일을 통해 유저의 아이디를 조회하고, usertype을 입력받아 Member 테이블에 유저와 프로젝트 아이디에 대한 외래키와 해당 유저가 프로젝트에서 어떤 역할을 맡았는지를 의미하는 UserType이 Enum으로 들어감.

### 프로젝트 리스트 조회 : readProjectList
유저의 아이디를 통해 외래키로 Member 리스트를 조회한 후, Member 테이블의 외래키를 통해 프로젝트를 조회. 해당 프로젝트 리스트의 필요한 Attribute를 DTO로 반환. 이때 프로젝트 아이디는 캐시에, 프로젝트 이름과 마감일은 프론트의 UI 상에 전달.

## [댓글]
### 댓글 생성 : createComment
댓글을 다는 유저의 아이디와 댓글을 달려고 하는 티켓의 아이디를 path에 포함시켜 보내고, 댓글의 내용을 DTO로 보낸다. 이를 각가 DB에 댓글 repoter = 유저의 아이디, 댓글의 내용을 Comment 테이블의 contents에 저장. 댓글의 생성 일자는 post 시점에서 자동으로 찍히도록 생성.

## [티켓]
### 티켓 생성 : createTicket
티켓의 마일스톤은 자동으로 현재 시점부터 마감일 한 달로 찍히도록 구현, 이후 마일스톤 테이블에 저장. 티켓의 생성일, 수정일은 API 호출 기준 자동으로 찍히도록 구현. 이때 생성한 티켓은 모두 Status가 New이며 자동 Developer 할당 로직을 구현하였다. 하지만 프로젝트 생성 초기에는 아직 개발자에 대한 상세한 파트가(맡은 임무) 정해지지 않았다고 가정하고, 오름차순으로 할당되게끔 구현하였음. 이후 ProjectLeader가 Assigned된 Developer를 변경할 수 있게끔 구현하여, 이후에 생길 불편함을 최소화함.

개발자 자동 할당 알고리즘은 다음과 같다.
1. projectId로 프로젝트 확인, 프로젝트 내 member list 가져오기
2. 멤버 중 Developer인 애들을 가져옴: developers
3. Developer인 Member 티켓 리스트를 DevTicketList 라하자. DevTicketList 중에 dto의 Component와 일치하는 티켓 리스트(티켓 수)를 HitCount로 함. DevTicketList 중 assigned인 티켓 수를 Busycount로 함.
4. HitCount가 제일 Dev가 한 명이면 그 Dev에게 assigned
5. HitCount가 여러 명이면 Busycount가 제일 작은 Dev에게 assigned
6. 4, 5의 단계로도 구분할 수 없다면 MemberId가 작은 Dev에게 assigned: 담당 개발자 변경

### 티켓 상세 정보 조회(단일 조회) : readTicket
티켓 아이디를 통해 티켓에 대한 정보 조회. 티켓을 클릭 시 티켓에 달린 댓글을 포함해서 티켓의 상세 정보를 볼 수 있어야 한다. 티켓 ID를 통해 외래키로 Comment를 조회한 뒤 티켓의 정보와 댓글의 정보를 함께 반환. 이때 리턴하기 위해 DTO에 해당 티켓에 대한 설명, 상태(Status), 우선 순위, 생성 시점, 수정 시점, 티켓의 종류(컴포넌트), 할당된 개발자, 티켓 발행한 사람, 마일스톤 이름 등이 리턴 됨. 댓글에 대한 내용과 댓글의 생성 시점을 함께 반환.

### [티켓 검색 기능]
* **AssignedTicket 조회** : readAssignedTicketList
* **New 조회** : readNewTicketList
* **Resolved 조회** : readResolvedTicketList
* **Reopened 조회** : readReopenedTicketList
* **Closed 조회** : readClosedTicketList

### readAssignedTicketList
프로젝트 아이디와 유저의 아이디를 이용해 내가 할당된 프로젝트의 티켓 리스트 중에 티켓의 Status가 Assigned인 티켓의 List를 DTO로 감싸 화면에 티켓 제목, 우선 순위, 생성 날짜 등이 자동으로 보이게끔 GET 방식을 이용하여 생성. 이때 검색 기능을 통해 얻은 ticket id는 캐시에 담아둔다.

### readNewTicketList
프로젝트 아이디와 유저의 아이디를 이용해 내가 할당된 프로젝트의 티켓 리스트 중에 티켓의 Status가 New인 티켓의 List를 DTO로 감싸 화면에 티켓 제목, 우선 순위, 생성 날짜 등이 자동으로 보이게끔 GET 방식을 이용하여 생성. 이때 검색 기능을 통해 얻은 ticket id는 캐시에 담아둔다.

AssignedTicket 조회 : readAssignedTicketList
New 조회 : readNewTicketList
Resolved 조회 : readResolvedTicketList
Reopened 조회 : readReopenedTicketList
Closed 조회 : readClosedTicketList
이하 나머지 셋의 로직은 동일하다.

그 외에 New, Resolved, Closed, Reopened 또한 같은
projectId로 프로젝트 내에 포함된 티켓을 찾아서
오늘을 기준으로 createdTime이 오늘인 Ticket의 Status 별 갯수와 priority 별 갯수,
마찬가지로 createdTime이 이번 달인 Ticket의 Status 별 갯수와 priority 별 갯수를 따로 통계치로 시각화.

### 티켓 상태 수정 : updateTicketStatus
업데이트할 Status를 Dto로 받아 해당 수정 내용을 저장.

### 담당 개발자 변경 : updateAssignedDeveloper
projectId로 프로젝트와 UserId를 통해 Member 테이블의 UserType을 찾아 UserType이 ProjectLeader이면 티켓의 Status를 AssignedDeveloper를 이메일을 찾아 변경할 수 있다.

### Additional Links : 참고 자료
* [생성 날짜를 자동으로 찍자](https://ozofweird.tistory.com/entry/%EC%82%BD%EC%A7%88-%ED%94%BC%ED%95%98%EA%B8%B0-Spring-Boot-%EB%82%A0%EC%A7%9C-%EB%8B%A4%EB%A3%A8%EA%B8%B0?category=938335)
* [REST 튜토리얼](https://spring.io/guides/tutorials/rest/)
* [Submission 핸들링하기](https://spring.io/guides/gs/handling-form-submission/)
* [JPA 공식 문서](https://spring.io/guides/gs/accessing-data-jpa/)
* [JPA 공식 문서2](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [API 명세서를 자동으로 써보자 : Swagger 버전 바뀜 참고](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#web.security)
