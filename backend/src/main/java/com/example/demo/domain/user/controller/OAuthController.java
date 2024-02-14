package com.example.demo.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class OAuthController {
    @GetMapping("/loginForm")
    public String home() {
        return """
                    <a href="/oauth2/authorization/google">구글 로그인</a>
                    <a href="/oauth2/authorization/kakao">카카오 로그인</a>
                    <a href="/oauth2/authorization/naver">네이버 로그인</a>
                """;
    }

    @GetMapping("/private")
    public String privatePage() {
        log.info("private page =================================================");
        return "privatePage.html";
    }
    @GetMapping("/admin")
    public String adminPage() {
        return "adminPage.html";
    }

    @GetMapping("/logOut")
    public String logOut() {
        return "logOut.html";
    }
}
