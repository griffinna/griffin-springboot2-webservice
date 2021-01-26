package com.griffin.springboot.web;

import com.griffin.springboot.config.auth.dto.SessionUser;
import com.griffin.springboot.domain.user.LoginUser;
import com.griffin.springboot.service.posts.PostsService;
import com.griffin.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {      // model : 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장 가능
        model.addAttribute("posts", postsService.findAllDesc());
        // 로그인
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");    @LoginUser 로 리팩토링
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

    @GetMapping("/calendar")
    public String calendar(Model model, @LoginUser SessionUser user) {
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "calendar";
    }

}
