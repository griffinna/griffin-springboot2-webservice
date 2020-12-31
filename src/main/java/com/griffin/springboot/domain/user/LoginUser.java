package com.griffin.springboot.domain.user;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)  // 이 어노테이션이 생성될 수 있는 위치 지정, 메서드의 파라미터로 선언된 객체에서만 사용가능
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {   // @interface : 해당 클래스 이름의 어노테이션이 생성

}
