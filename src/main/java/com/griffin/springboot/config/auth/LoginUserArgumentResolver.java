package com.griffin.springboot.config.auth;

import com.griffin.springboot.config.auth.dto.SessionUser;
import com.griffin.springboot.domain.user.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;

    // 컨트롤러 메서드의 특정 파라미터를 지원하는지 판단
    // 파라미터에 @LoginUser가 붙어있고, 파라미터 클래스 타입이 SessionUser.class 인 경우 true 반환
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    // 파라미터에 전달할 객체를 생성 (아래의 경우 세션에서 객체를 가져옴)
   @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        return httpSession.getAttribute("user");
    }
}
