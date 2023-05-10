# SSR_BOARD

# 프로젝트 구조
![](https://velog.velcdn.com/images/dzpro0327/post/25524b22-cf70-4e94-a171-5baa882e8af4/image.png)

- Spring MVC 패턴 적용
- @Controller -> Model & View 반환하는 방식 (SSR)
- JPA를 이용한 ORM 적용 (관계형 DB)
- FORM 데이터를 주고 받을 때 DTO 적용

# 적용 라이브러리
- Spring Data JPA
- Spring validation
- Lombok

# 구현 기능
<details>
<summary>회원가입</summary>
<div markdown="1">
<img alt="회원가입" src="https://velog.velcdn.com/images/dzpro0327/post/8337a7f0-3e90-4fe0-b223-b4c8bd0417fa/image.gif">
</div>
</details>

<details>
<summary>로그인</summary>
<div markdown="1">
<img alt="로그인" src="https://velog.velcdn.com/images/dzpro0327/post/4cf0f2fa-34c4-4931-83a5-715638e841d6/image.gif">
</div>
</details>

<details>
<summary>게시판 작성</summary>
<div markdown="1">
<img alt="게시판 작성" src="https://velog.velcdn.com/images/dzpro0327/post/a6bd128d-6d23-431b-a869-7d901819ac0a/image.gif">
</div>
</details>

<details>
<summary>게시판 수정</summary>
<div markdown="1">
<img alt="게시판 수정" src="https://velog.velcdn.com/images/dzpro0327/post/50590272-de76-4100-bfdd-bf3780e39c33/image.gif">
</div>
</details>

<details>
<summary>게시판 삭제</summary>
<div markdown="1">
<img alt="게시판 삭제" src="https://velog.velcdn.com/images/dzpro0327/post/e06b3fbd-33bc-4d91-a2a4-32a2ef2848a3/image.gif">

</div>
</details>

<details>
<summary>댓글 작성</summary>
<div markdown="1">
<img alt="댓글 작성" src="https://velog.velcdn.com/images/dzpro0327/post/7898dda9-dc0b-4d5c-becc-5ff674c109c6/image.gif">
</div>
</details>

<details>
<summary>댓글 삭제</summary>
<div markdown="1">
<img alt="댓글 삭제" src="https://velog.velcdn.com/images/dzpro0327/post/ed513e65-f123-4d95-a213-02e3bf803001/image.gif">
</div>
</details>

<details>
<summary>메인페이지 페이징 처리</summary>
<div markdown="1">
<img alt="페이징 처리" src="https://velog.velcdn.com/images/dzpro0327/post/03ef22ab-1038-4f9f-b822-e7928e628adf/image.gif">
</div>
</details>

# 서버 로그인 및 권한처리 방식
![](https://velog.velcdn.com/images/dzpro0327/post/4282ea5e-f769-4cb3-96d3-a33a04639c8f/image.png)

- Session 방식 적용 (로그인 Controller에서 session.setAttribute 적용)
- Spring interceptor 적용 (HandlerInterceptor -> preHandle 구현) -> session 체크
- WebMvcConfigurer 인터페이스 구현 클래스를 만들고 addInterceptors 구현 (@Configuration 적용) -> 인터셉터 추가
- 작성자가 다르거나 비로그인 경우 수정,삭제 불가능

![](https://velog.velcdn.com/images/dzpro0327/post/4af71ee4-5cfa-4391-9482-ed2a906d8a40/image.gif)

![](https://velog.velcdn.com/images/dzpro0327/post/4369de53-e92d-4196-9b05-70460017ed9f/image.gif)

![](https://velog.velcdn.com/images/dzpro0327/post/3794e5dc-3534-4383-8679-87f64d313d92/image.gif)


# 유효성 검증
- Spring validation을 이용 -> DTO 클래스에 적용 
![](https://velog.velcdn.com/images/dzpro0327/post/0c6d7ee2-a70e-4900-8efd-eb700edbc097/image.png)

- 잘못된 데이터를 전송할 경우 view에서 오류를 나타냄 (BindingResult -> view(thymeleaf 적용))
![](https://velog.velcdn.com/images/dzpro0327/post/187e312c-dee8-45f6-9e9e-7c6687e6a5c7/image.gif)

- 타임리프 th:errorclass, th:errors -> 오류시 처리 화면 지정
![](https://velog.velcdn.com/images/dzpro0327/post/a51d1a38-3264-40da-b49d-10cf45114335/image.png)


# 테스트 코드
- JUNIT5을 이용
- Service Layer 테스트 코드 작성

![](https://velog.velcdn.com/images/dzpro0327/post/b39e2c0f-0247-4704-8398-f27ef01a9b3f/image.png)
![](https://velog.velcdn.com/images/dzpro0327/post/751e6942-eadd-4552-9360-6b0b0e1240d6/image.png)



# ERD

![](https://velog.velcdn.com/images/dzpro0327/post/83cad78f-9fe9-4d5a-ac61-905f1d647dd6/image.png)

- MEMBER 객체와 BOARD 객체를 1:N관계 설정(맴버 한명당 여러개의 게시판 작성가능, 작성자 조회 가능)

- MEMBER 객체와 COMMIT 객체를 1:N관계 설정(맴버 한명당 여러개의 댓글 작성가능, 작성자 조회 가능)

- BOARD 객체와 COMMIT 객체를 1:N관계 설정(게시글 하나당 여러개의 댓글 작성가능, 댓글 조회 가능)

- COMMIT 객체는 FK를 통해 누가 어디에 작성한지 조회 가능

- Board 객체를 삭제할 경우 Board_id를 FK로 가지고 있는 Commit 객체 모두 삭제 
  (JPA -> cascade = CascadeType.REMOVE 적용)
    
- Entity 객체 생성시 JPA를 @EnableJpaAuditing를 이용해서 createAt, updatedAt 자동 생성



# 회고
- ### 프로젝트를 진행하면서 든 생각
  - 템플릿엔진으로만 화면과 상호작용을 만드는 것은 상상이상으로 힘들다.
  - HTML, JS 기본도 모르고 화면, 상호작용을 만들겠다는 것은 오만이다.
  - 부트스트랩은 생각보다 코드가 지저분하다.
  - REST API 방식으로 서버를 만드는 것과 VIEW / MODEL 을 직접 서버에서 주는 것은 결이 다르다.
  - 서버보다는 화면을 만들면서 시간을 더 보냈다.
  - 하나의 화면에 여러 컨트롤러가 작동하는 경우 단순 FORM 형식으로 데이터를 주는 건 한계가 있다.

- ### 마무리 하며
  처음에는 가벼운 마음으로 시작한 토이 프로젝트였다. 템플릿 엔진도 연습할 겸 시작했다. REST API 방식으로 서버를 만드는것이 익숙해서 그런지 초반에 꽤 애를 먹었다. Model&View 개념도 없었고 화면에서 데이터를 넘기는 방식도 HTML-FORM 형식으로 넘기는 것도 꽤 어려웠다. 또 자바스크립트를 잘 몰랐고 AJAX나 AXIOS 방식으로 데이터를 주고받는 방식은 알고 있지만 AJAX,AXIOS 작성을 못하는 것도 큰 어려움이었다. 또 화면을 예쁘게 제대로 못만들어 힘들었다. ㅋㅋㅋ 정말 혼자서 만들면서 HTML/CSS가 이렇게 어려운 건 줄 몰랐다.
서버를 만들면서 REST API는 단순 JSON 데이터를 넘겨주면 되는 방식이지만 화면을 넘겨주는 VIEW Controller, 데이터를 주고 받는 Controller를 나눠 만들었고 그 과정에서 구분을 똑바로 못해서 애먹기도 했다. 또 구조를 어떻게 만들어야 하는지 객체를 어떻게 주고 받을 것인가? 등 고민을 많이 해본 것 같다. LOMBOK을 이용해서 @Builder도 사용해보고 여러가지를 시도해본 것 같아서 나름 좋은 공부를 했다.
테스트 코드도 작성을 하면서 기존의 코드를 수정하는 경우도 많았다. 평소 TDD방식은 먼 이야기라고 생각하며 있었는데 중요하다고 하는 이유를 알 것 같았다. 그리고 테스트를 만들면서 화면을 보고 메서드를 테스트 하는 것이 아니라 코드로 테스트를 진행하면서 어디 코드가 문제인지 정확하게 알 수 있었다. 또 시간도 절약할 수 있었다.
다음에 또 SSR 방식을 해본다면 자바스크립트도 같이 적용해서 해보면 조금 더 고도화된 토이 프로젝트를 만들 수 있을 것 같다.
