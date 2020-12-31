package com.griffin.springboot.web;

import com.griffin.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloController.class,    // @ContorllerAdvice, @Controller 를 Scan (@Repository, @Service, @Component 는 스캔대상이 아님)
        // SecurityConfig 를 생성하기 위해 필요한 CustomOAuth2UserService 는 읽을 수 없음 (스캔범위에서 제거)
        excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @WithMockUser(roles = "USER")
    @Test
    public void Hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void HelloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                    get("/hello/dto")
                    .param("name", name)
                    .param("amount", String.valueOf(amount))
                ).andExpect(status().isOk())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(name)))
        .andExpect(jsonPath("$.amount", is(amount)));

    }
}
