package com.example.demo.domain.fcm.controller;

import com.example.demo.domain.fcm.dto.response.FCMTokenDto;
import com.example.demo.domain.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(FCMController.class)
class FCMControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FCMService fcmService;

    @DisplayName("O 성공 FCM 토큰 발급 및 갱신")
    @Test
    @WithMockUser
    void saveOrUpdate() throws Exception {
        // Given
        Long userId = 1L;
        FCMTokenDto result = new FCMTokenDto("testId", "testAccessToken", 1L);

        given(fcmService.saveOrUpdateToken(userId)).willReturn(result);

        // When
        mockMvc.perform(post("/api/v1/fcm/{userId}", userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success": true,
                            "data": {
                                "id": "testId",
                                "accessToken": "testAccessToken",
                                "userId": 1
                            },
                            "message": "FCM 토큰 발급 성공"
                        }
                """));
    }

    @DisplayName("O 성공 FCM 토큰 삭제")
    @Test
    @WithMockUser
    void deleteToken() throws Exception {
        // Given
        Long userId = 1L;
        FCMTokenDto result = new FCMTokenDto("testId", "testAccessToken", 1L);

        given(fcmService.deleteToken(userId)).willReturn(result);

        // When
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/fcm/{userId}", userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success": true,
                            "data": {
                                "id": "testId",
                                "accessToken": "testAccessToken",
                                "userId": 1
                            },
                            "message": "FCM 토큰 삭제 성공"
                        }
                """));
    }
}
