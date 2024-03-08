package com.example.demo.domain.gethering.controller;

import com.example.demo.domain.gethering.dto.command.TagCommand;
import com.example.demo.domain.gethering.entity.Tag;
import com.example.demo.domain.gethering.service.TagService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RequiredArgsConstructor
@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @DisplayName("태그 생성 성공")
    @Test
    @WithMockUser
    public void register() throws Exception {
        // Given
        String requestBodyJson =
                """
                    {
                        "name": "태그"
                    }
                """;


        Tag tag = Tag.builder()
                .id(1L)
                .name("태그")
                .build();

        given(tagService.register(any(TagCommand.class))).willReturn(tag);

        // When
        mockMvc.perform(post("/api/v1/gethering/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                        "success": true,
                        "data": {
                            "id": 1,
                            "name": "태그"
                        },
                        "message": "태그 생성 성공"
                    }
                """));
    }

    @DisplayName("태그 조회")
    @Test
    @WithMockUser
    public void list() throws Exception {
        // Given
        String name = "축구";

        given(tagService.getTag(name)).willReturn(List.of(
                Tag.builder().id(1L).name("축구").build(),
                Tag.builder().id(2L).name("축구1").build(),
                Tag.builder().id(3L).name("1축구").build()
        ));

        // When
        mockMvc.perform(get("/api/v1/gethering/tag")
                .contentType(MediaType.APPLICATION_JSON)
                        .param("name", name)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "success": true,
                            "data": {
                                "tags": [
                                    {
                                        "id": 1,
                                        "name": "축구"
                                    },
                                    {
                                        "id": 2,
                                        "name": "축구1"
                                    },
                                    {
                                        "id": 3,
                                        "name": "1축구"
                                    }
                                ]
                            },
                            "message": "태그 조회 성공"
                        }
                """));
    }

    @DisplayName("태그 수정")
    @Test
    @WithMockUser
    public void update() throws Exception {
        // Given
        String requestBodyJson =
                """
                        {
                            "name": "축구"
                        }
                        """;

        Tag tag = Tag.builder()
                .id(1L)
                .name("축구")
                .build();

        given(tagService.update(anyLong(), any(TagCommand.class))).willReturn(tag);

        // When
        mockMvc.perform(put("/api/v1/gethering/tag/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "success": true,
                            "data": {
                                "id": 1,
                                "name": "축구"
                            },
                            "message": "태그 수정 성공"
                        }
                """));
    }

    @DisplayName("태그 삭제")
    @Test
    @WithMockUser
    public void deleteTag() throws Exception {
        // When
        mockMvc.perform(delete("/api/v1/gethering/tag/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "success": true,
                            "data": {
                                "id": 1
                            },
                            "message": "태그 삭제 성공"
                        }
                """));
    }
}