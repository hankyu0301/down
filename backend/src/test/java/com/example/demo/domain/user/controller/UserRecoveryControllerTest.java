package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.command.UserJoinCheckCommand;
import com.example.demo.domain.user.service.EmailService;
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

@WebMvcTest(UserRecoveryController.class)
class UserRecoveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @DisplayName("O 성공 회원가입 여부 확인")
    @Test
    @WithMockUser
    void checkUserJoin() throws Exception {
        // Given
        String requestBodyJson = """
                    {
                      "email": "test@gmail.com",
                      "userName": "홍길동"
                    }
                """;

        given(userService.checkEmail(any(UserJoinCheckCommand.class))).willReturn(true);

        // When
        mockMvc.perform(post("/api/v1/users/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                              "success": true,
                              "data": {
                                "checkedEmail": "test@gmail.com",
                                "existence": true
                              },
                              "message": "회원이 존재합니다."
                            }
                        """));
    }

    @DisplayName("X 실패 회원가입 여부 확인")
    @Test
    @WithMockUser
    void checkUserJoinFail() throws Exception {
        // Given
        String requestBodyJson = """
                    {
                      "userName": "홍길동"
                    }
                """;

        given(userService.checkEmail(any(UserJoinCheckCommand.class))).willReturn(true);

        // When
        mockMvc.perform(post("/api/v1/users/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                                {
                                  "success": false,
                                  "data": [
                                    {
                                      "field": "email",
                                      "message": "이메일은 필수 입력 값입니다.",
                                      "value": null
                                    }
                                  ],
                                  "message": "유효성 검사 실패"
                                }
                        """));
    }

    @DisplayName("임시 비밀번호 전송")
    @Test
    @WithMockUser
    void sendTemporaryPassword() throws Exception {
        // Given
        String requestBodyJson = """
                    {
                      "email": "test@gmail.com",
                      "userName": "홍길동"
                    }
                """;

        given(emailService.sendResetPassword(any(UserJoinCheckCommand.class))).willReturn(true);

        // When
        mockMvc.perform(post("/api/v1/users/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                              "success": true,
                              "data": {
                                "email": "test@gmail.com",
                                "result": true
                              },
                              "message": "임시 비밀번호 전송 성공"
                            }
                        """));
    }
}