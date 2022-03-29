
# ITya(잇챠) 웹 개발 프로젝트

# 프로젝트 개발 환경

-   운영체제 : window 10
-   IDE : IntelliJ
-   JDK : JDK 8
-   스프링 부트 버전 : 2.5.2
-   데이터베이스 : MYSQL
-   빌드 툴 : MAVEN
- ORM : JPA

# 프로젝트 개요

IT 관련 상품을 새 제품이나 중고로 사고 팔수 있고, 관련 커뮤니티 게시판이 있는 웹 개발을 바랍니다.

아래의 상세 요구사항을 모두 만족해야 합니다.
#
### 요구사항정의서

-   상품을 등록, 수정, 삭제할 수 있어야 한다.
    -   상품은 상품코드, 상품명, 가격, 재고수량, 상품 상세 설명 상품 상태(판매 중, 재고 없음)을 포함해야 한다.
    -   상품을 관리자가 직접 등록할 수 있어야 한다.
        -   등록한 상품은 메인 이미지와 가격이 메인화면에 표시된다.
        -   메인 상품 클릭 시 상품 상세 정보로 이동된다.
    -   ‘상품명’을 대상으로 검색어가 포함된 상품을 검색할 수 있어야 한다.
-   회원을 등록, 수정, 탈퇴 할 수 있어야 한다.
    -   회원 가입(어드민, 회원)은 이름, 이메일주소, 비밀번호, 주소를 포함해야 한다.
    -   회원 가입 시 정보가 조건에 부합하지 않을 경우 오류 메세지를 출력한다.
-   로그인 로그아웃을 할 수 있어야 한다.
    -   가입한 아이디와 비밀번호로 로그인이 가능하고 상시적으로 로그아웃이 가능해야 한다.
    -   로그인한 아이디의 권한인 어드민과 일반 사용자, 비회원에 따라 다른 페이지 권한을 부여한다.
        -   비회원은 로그인만 표시된다.
        -   어드민은 상품 등록, 상품 관리, 장바구니, 구매이력, 게시판이 표시된다.
        -   회원은 장바구니, 구매이력, 게시판이 표시된다.
-   회원
    -   등록된 상품을 회원이 클릭 시 수량을 정해 장바구니에 넣거나 주문할수 있고, 상품 정보로 품절여부와 상세 설명 이미지들이 표시된다.
    -   장바구니에서 주문할 상품을 취소할 수 있다.
    -   장바구니에 등록된 상품을 주문할 경우 ‘구매이력’에서 확인이 가능하다.
-   게시판
    -   모든 이용자가 가능한 게시판을 만든다.
    -   게시글을 등록, 수정, 삭제가 가능해야 한다.
#
### 요구조건목록
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/ZgfHV/btrxLQZqSfF/62grCDYsVWu7W8kHEPGnm1/img.png)
#
### 메인화면
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/rbaU5/btrxVkkBcQ3/tnKl2pA7fQ4cDr6RLrbF9K/img.png)

### 상품상세정보
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/9AdCt/btrxIMceUs1/lJau3Q3Y4uvWa7rApRtgv0/img.png)

### 로그인
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/bwQBwz/btrxVmbFsR3/2okCBx1e9RSwbuMJ9Zfnsk/img.png)

### 회원가입
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/clczFT/btrxHYjEfP2/edK0fkAeu8cEK4JqCjXVa1/img.png)

### 상품등록
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/c1a2aE/btrxU0fBh1F/VhfFX4knoWSCqsRPkzgEv1/img.png)

### 상품관리
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/eqsddW/btrxUY3auRm/ivbOEA0mv2tM4esWNUp66k/img.png)

### 등록상품수정
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/W8aD7/btrxVHmkLqI/C0Fv800vZxoiy28AB3DorK/img.png)

### 장바구니
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/k6i9y/btrxQ4JD0sI/UuLg0SDG3yyCevbcHhjAqK/img.png)

### 구매이력
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/dnXUtu/btrxKhpwbqn/lmXKI3LenG8A5BjaaA8iBk/img.png)

#
### 기술스택
