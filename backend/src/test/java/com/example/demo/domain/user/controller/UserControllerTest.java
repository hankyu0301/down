package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.command.PasswordChangeCommand;
import com.example.demo.domain.user.dto.command.UserInfoChangeCommand;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.UserRoleEnumType;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.auth.LoginEnumType;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RequiredArgsConstructor
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("O 성공 회원들 정보 조회")
    @Test
    @WithMockUser
    void getUsersInfo() throws Exception {

        // Given
        int page = 0;
        int size = 10;

        List<User> userList = new ArrayList<>();
        userList.add(createUser(1L));
        userList.add(createUser(2L));
        userList.add(createUser(3L));

        Page<User> userPage = new PageImpl<>(userList, PageRequest.of(0, 10), userList.size());

        given(userService.getUsersInfo(any(Integer.class), any(Integer.class)))
                .willReturn(userPage);

        // When
        mockMvc.perform(get("/api/v1/users")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size)))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "success": true,
                                "data": {
                                    "content": [
                                        {
                                            "id": 1,
                                            "email": "example@example.com",
                                            "nickName": "exampleNickname",
                                            "userName": "John Doe"
                                        },
                                        {
                                            "id": 2,
                                            "email": "example@example.com",
                                            "nickName": "exampleNickname",
                                            "userName": "John Doe"
                                        },
                                        {
                                            "id": 3,
                                            "email": "example@example.com",
                                            "nickName": "exampleNickname",
                                            "userName": "John Doe"
                                        }
                                    ],
                                    "pageable": {
                                        "pageNumber": 0,
                                        "pageSize": 10,
                                        "sort": {
                                            "empty": true,
                                            "sorted": false,
                                            "unsorted": true
                                        },
                                        "offset": 0,
                                        "paged": true,
                                        "unpaged": false
                                    },
                                    "last": true,
                                    "totalPages": 1,
                                    "totalElements": 3,
                                    "size": 10,
                                    "number": 0,
                                    "sort": {
                                        "empty": true,
                                        "sorted": false,
                                        "unsorted": true
                                    },
                                    "numberOfElements": 3,
                                    "first": true,
                                    "empty": false
                                },
                                "message": "회원들 정보 조회 성공"
                            }
                        """));
    }

    @DisplayName("O 성공 회원 정보 조회")
    @Test
    @WithMockUser
    void getUserInfo() throws Exception {
        // given
        given(userService.getUserInfo(any(Long.class)))
                .willReturn(createUser(1L));

        // when
        mockMvc.perform(get("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "success": true,
                                "data": {
                                    "id": 1,
                                    "email": "example@example.com",
                                    "nickName": "exampleNickname",
                                    "userName": "John Doe"
                                },
                                "message": "회원 정보 조회 성공"
                               }
                """));
    }

    @DisplayName("O 성공 회원 정보 수정")
    @Test
    @WithMockUser
    void updateUserInfo() throws Exception {

        // Given
        String requestBodyJson = """
                    {
                      "nickName": "exampleNickname"
                    }
                """;

        given(userService.updateUserInfo(any(Long.class), any(UserInfoChangeCommand.class)))
                .willReturn(true);

        // When
        mockMvc.perform(patch("/api/v1/users/1")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "success": true,
                                "data": {
                                    "result": true
                                },
                                "message": "회원 정보 수정 성공"
                            }
                        """));
    }

    @DisplayName("O 성공 회원 탈퇴")
    @Test
    @WithMockUser
    void deleteUser() throws Exception {

        // Given
        given(userService.deleteUser(any(Long.class)))
                .willReturn(true);

        // When
        mockMvc.perform(delete("/api/v1/users/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "success": true,
                                "data": {
                                    "result": true
                                },
                                "message": "회원 탈퇴 성공"
                            }
                        """));
    }

    @DisplayName("성공 비밀번호 변경")
    @Test
    @WithMockUser
    void changePassword() throws Exception {
        // Given
        String requestBodyJson =
                """
                    {
                        "password": "test1234!"
                    }
                """;

        given(jwtTokenProvider.authorizationToJwt(any())).willReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpc3MiOiJkb3duLXNlcnZlci1qd3QiLCJpZCI6MSwiZXhwIjoxNzE2NTY0OTU4LCJpYXQiOjE3MDg3ODg5NTgsInVzZXJuYW1lIjoidGVzdCJ9.5somLxNq43BKloXJmUTCn0a31hsavnzyVzVpPXk_ed8");
        given(jwtTokenProvider.getEmailFormToken(any())).willReturn("test@gmail.com");
        given(userService.changePassword(any(Long.class), any(PasswordChangeCommand.class), any(String.class))).willReturn(true);

        // When
        mockMvc.perform(post("/api/v1/users/1/password/change")
                        .with(csrf())
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpc3MiOiJkb3duLXNlcnZlci1qd3QiLCJpZCI6MSwiZXhwIjoxNzE2NTY0OTU4LCJpYXQiOjE3MDg3ODg5NTgsInVzZXJuYW1lIjoidGVzdCJ9.5somLxNq43BKloXJmUTCn0a31hsavnzyVzVpPXk_ed8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                    {
                      "success": true,
                      "data": {
                        "success": true
                      },
                      "message": "비밀번호 변경이 완료되었습니다."
                    }
                """));
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .email("example@example.com")
                .password("password123")
                .nickName("exampleNickname")
                .userName("John Doe")
                .provider(LoginEnumType.SERVICE)
                .gender("male")
                .birth("1990-01-01")
                .providerId(123456789L)
                .role(UserRoleEnumType.ROLE_USER)
                .termsAgree(true)
                .build();
    }
}