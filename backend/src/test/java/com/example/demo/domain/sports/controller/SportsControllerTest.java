package com.example.demo.domain.sports.controller;


import com.example.demo.domain.sports.dto.command.SportsCommand;
import com.example.demo.domain.sports.entity.Sports;
import com.example.demo.domain.sports.service.SportsService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
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
@WebMvcTest(SportsController.class)
class SportsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportsService sportsService;

    @DisplayName("운동 생성 성공")
    @Test
    @WithMockUser
    void create() throws Exception {
        // Given
        String requestBodyJson =
                """
                    {
                        "name": "test"
                    }
                """;

        given(sportsService.registerSports(any(SportsCommand.class)))
                .willReturn(createSports(1L));

        // When
        mockMvc.perform(post("/api/v1/sports")
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
                                        "name": "test"
                                    },
                                    "message": "운동 생성 성공"
                                }
                        """));
    }

    @DisplayName("운동 정보 목록 조회")
    @Test
    @WithMockUser
    void list() throws Exception {
        // Given
        Integer page = 0;
        Integer size = 10;

        List<Sports> sportsList = new ArrayList<>();
        sportsList.add(createSports(1L));
        sportsList.add(createSports(2L));

        given(sportsService.sportsList(page, size))
                .willReturn(new PageImpl<>(sportsList));

        // When
        mockMvc.perform(get("/api/v1/sports")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                            {
                                "success": true,
                                "data": {
                                    "content": [
                                        {
                                            "id": 1,
                                            "name": "test"
                                        },
                                        {
                                            "id": 2,
                                            "name": "test"
                                        }
                                    ],
                                    "pageable": "INSTANCE",
                                    "totalPages": 1,
                                    "totalElements": 2,
                                    "last": true,
                                    "size": 2,
                                    "number": 0,
                                    "sort": {
                                        "empty": true,
                                        "sorted": false,
                                        "unsorted": true
                                    },
                                    "numberOfElements": 2,
                                    "first": true,
                                    "empty": false
                                },
                                "message": "운동 정보 목록 조회 성공"
                            }
                """));
    }

    @DisplayName("운동 상세 조회")
    @Test
    @WithMockUser
    void getTest() throws Exception {
        // Given
        Long id = 1L;

        given(sportsService.findById(id))
                .willReturn(createSports(id));

        // When
        mockMvc.perform(get("/api/v1/sports/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success": true,
                            "data": {
                                "id": 1,
                                "name": "test"
                            },
                            "message": "운동 상세 조회 성공"
                        }
                """));
    }

    @DisplayName("운동 수정")
    @Test
    @WithMockUser
    void update() throws Exception {
        // Given
        Long id = 1L;
        String requestBodyJson =
                """
                    {
                        "name": "update"
                    }
                """;

        given(sportsService.updateSports(any(Long.class), any(SportsCommand.class)))
                .willReturn(updateSports(id, "update"));

        // When
        mockMvc.perform(put("/api/v1/sports/" + id)
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
                                        "name": "update"
                                    },
                                    "message": "운동 수정 성공"
                                }
                        """));
    }

    @DisplayName("운동 삭제")
    @Test
    @WithMockUser
    void deleteTest() throws Exception {
        // Given
        long id = 1L;

        // When
        mockMvc.perform(delete("/api/v1/sports/" + id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success": true,
                            "data": {
                                "id": 1
                            },
                            "message": "운동 삭제 성공"
                        }
                """));
    }

    private Sports createSports (Long id) {
        return Sports.builder()
                .id(id)
                .name("test")
                .build();
    }

    private Sports updateSports (Long id, String name) {
        return Sports.builder()
                .id(id)
                .name(name)
                .build();
    }
}