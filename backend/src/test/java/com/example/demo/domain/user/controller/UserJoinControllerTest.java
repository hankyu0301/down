package com.example.demo.domain.user.controller;


import com.example.demo.domain.user.dto.command.CheckEmailVerificationCommand;
import com.example.demo.domain.user.dto.command.CheckNickNameCommand;
import com.example.demo.domain.user.dto.command.SendEmailVerificationCommand;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.EnumUserRole;
import com.example.demo.domain.user.model.EmailVerification;
import com.example.demo.domain.user.service.EmailService;
import com.example.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(UserJoinController.class)
class UserJoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserService userService;

    @DisplayName("이메일 중복 체크 성공")
    @Test
    @WithMockUser
    void checkEmail() throws Exception {
        // Given
        String requestBodyJson =
                """
                    {
                        "email": "test@gmail.com"
                    }
                """;

        given(emailService.registerPendingEmail("test@gmail.com")).willReturn(true);

        // When
        mockMvc.perform(post("/api/v1/users/email/check")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                            {
                              "success": true,
                              "data": {
                                "checkedEmail": "test@gmail.com",
                                "available": true
                              },
                              "message": "사용 가능한 이메일입니다."
                            }
                    """));
    }

    @DisplayName("이메일 인증코드 발송")
    @Test
    @WithMockUser
    void sendEmailVerification() throws Exception {
        // Given
        String requestBodyJson =
                        """
                            {
                                "email": "test@gmail.com"
                            }
                        """;

        EmailVerification emailVerification = EmailVerification.builder()
                        .email("test@gmail.com")
                        .code("123456")
                        .build();

        given(emailService.sendEmailVerification(any(SendEmailVerificationCommand.class)))
                .willReturn(emailVerification);

        // When
        mockMvc.perform(post("/api/v1/users/email/send")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "success": true,
                          "data": {
                            "success": true,
                            "email": "test@gmail.com"
                          },
                          "message": "이메일 인증코드가 발송되었습니다."
                        }
                """));
    }

    @DisplayName("이메일 인증코드 확인")
    @Test
    @WithMockUser
    void checkEmailVerification() throws Exception {
        // Given
        String requestBodyJson =
                        """
                            {
                                "email": "test@gmail.com",
                                "code": "123456"
                            }
                        """;

        given(emailService.checkEmailVerification(any(CheckEmailVerificationCommand.class)))
                .willReturn(true);

        // When
        mockMvc.perform(post("/api/v1/users/email/verify")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                            {
                              "success": true,
                              "data": {
                                "email": "test@gmail.com",
                                "result": true
                              },
                              "message": "이메일 인증코드 확인이 완료되었습니다."
                            }
                """));
    }

    @DisplayName("회원 가입")
    @Test
    @WithMockUser
    void join() throws Exception {
        // Given
        String requestBodyJson =
                """
                    {
                      "email": "test@gmail.com",
                      "password": "test1234",
                      "nickName": "페이커",
                      "gender": "male",
                      "birth": "9999-99-99",
                      "userName": "이상혁",
                      "code": "1A3B5C",
                      "termsAgree": true
                    }
                """;

        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .password(passwordEncoder.encode("test1234"))
                .nickName("페이커")
                .userName("이상혁")
                .gender("male")
                .birth("9999-99-99")
                .role(EnumUserRole.ROLE_USER)
                .termsAgree(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        given(userService.join(any())).willReturn(user);

        // When
        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                   {
                     "success": true,
                     "data": {
                       "id": 1,
                       "email": "test@gmail.com"
                     },
                     "message": "회원가입이 완료되었습니다."
                   }
                """));
    }

    @DisplayName("성공 닉네임 중복 체크")
    @Test
    @WithMockUser
    void checkNickName() throws Exception {
        // Given
        String requestBodyJson =
                """
                    {
                        "nickName": "페이커"
                    }
                """;

        given(userService.checkNickName(any(CheckNickNameCommand.class))).willReturn(true);

        // When
        mockMvc.perform(post("/api/v1/users/nickname/check")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                    {
                      "success": true,
                      "data": {
                        "nickName": "페이커",
                        "available": true
                      },
                      "message": "사용 가능한 닉네임입니다."
                    }
                """));
    }
}