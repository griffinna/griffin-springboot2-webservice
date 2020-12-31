package com.griffin.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// @WebMvcTest 에서 @Entity 클래스가 없어 에러 발생 (@EnableJpaAuditing 과 @SpringBootApplication 을 분리) > @EnableJpaAuditing 제거 후 JpaConfig 에 작성
//@EnableJpaAuditing      // JPA Auditing 활성화
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}