package com.griffin.springboot.config.auth;

import com.griffin.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable()    // h2-console 화면 사용을 위해 해당 옵션들을 disable
                .and().authorizeRequests()                              // URL별 권한관리를 설정하는 옵션의 시작점 (authrizeRequests가 선언되어야만 antMatchers 사용가능
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/h2-").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/")
                .and().oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);

        /*
        * antMatchers
        * . 권한 관리 대상을 지정하는 옵션
        * . URL, HTTP 메소드별로 관리가 가능
        * . permitAll : 전체 열람 권한
        * . hasRole(Role.USER.name()) : USER 권한을 가진 사람만 가능
        * . anyRequest : 설정된 값들 이외 나머지 URL
        * . authenticated : 인증된(로그인한) 사용자들에게만 허용
        * . logout : 로그아웃 기능에 대한 여러 설정의 진입점
        * . logoutSuccessUrl : 로그아웃 성공시 이동
        * . oauth2Login : OAuth2 로그인 기능에 대한 여러 설정의 진입점
        * . userInfoEndPoint : 로그인 성공 이후 사용자 정보를 가져올 때의 설정 담담
        * . userService : 소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
        *                 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시
        * */
    }
}