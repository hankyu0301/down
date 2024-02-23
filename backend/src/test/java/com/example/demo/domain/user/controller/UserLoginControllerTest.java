package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.command.UserLoginCommand;
import com.example.demo.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserLoginController.class)
class UserLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @DisplayName("로그인 성공")
    @WithMockUser
    @Test
    void login() throws Exception {

        // Given
        String requestBodyJson = """
                {
                    "email": "test@gmail.com",
                    "password": "test1234"
                }
                """;

        given(userService.login(any(UserLoginCommand.class)))
                .willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        // When
        mockMvc.perform(post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "success": true,
                          "data": {
                            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                            "tokenType": "Bearer"
                          },
                          "message": "로그인 성공"
                        }
                """));
    }
}