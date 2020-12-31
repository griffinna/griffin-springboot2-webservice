package com.griffin.springboot.config;

import com.griffin.springboot.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/*
 * LoginUserArgumentResolver 가 스프링에서 인식될 수 있도록 WebMvcConfigurer 에 추가
 * HandlerMethodArgumentResolver 는 항상 WebMvcConfigurer의 addArgumentResolvers() 를 통해 추가해야함
 * */
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }

}
