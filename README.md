# email-scheduler
Spring boot + quartz를 이용한 이메일 스케쥴러 tutorial 작성 
* 참고 튜토리얼 영상 : https://www.youtube.com/watch?v=6KavPTuC0pU&list=PLS1QulWo1RIZL1UicW3PV905pirgXjfQV

## TO-DO
- [X] @Valid가 적용되지 않는 문제
  - javax.validation:validation-api는 구현체가 아니라 인터페이스만 있어서 동작하지 않았음. 
  - org.springframework.boot:spring-boot-starter-validation를 추가해서 해결하였음.
