package com.example.demo.domain.gethering.controller;

import com.example.demo.domain.gethering.dto.command.CommentCommand;
import com.example.demo.domain.gethering.entity.Comment;
import com.example.demo.domain.gethering.entity.Gethering;
import com.example.demo.domain.gethering.service.CommentService;
import com.example.demo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RequiredArgsConstructor
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @DisplayName("댓글 생성 성공")
    @Test
    @WithMockUser
    public void register() throws Exception {
        // Given
        String requestBodyJson =
                """
                    {
                        "content": "댓글"
                    }
                """;

        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("1234")
                .nickName("페이커")
                .build();

        Gethering gethering = Gethering.builder()
                .id(1L)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .user(user)
                .gethering(gethering)
                .content("댓글")
                .createdAt(LocalDateTime.of(2021, 7, 1, 0, 0, 0))
                .build();

        given(commentService.register(any(Long.class), any(Long.class), any(CommentCommand.class)))
                .willReturn(comment);

        // When
        mockMvc.perform(post("/api/v1/gethering/{id}/comment/users/{id}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success": true,
                            "data": {
                                "id": 1,
                                "content": "댓글",
                                "getheringId": 1,
                                "nickName": "페이커",
                                "createdAt": "2021-07-01T00:00:00"
                            },
                            "message": "댓글 생성 성공"
                        }
                """));
    }

    @DisplayName("댓글 조회")
    @Test
    @WithMockUser
    public void list() throws Exception {
        // Given
        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("1234")
                .nickName("페이커")
                .build();

        List<Comment> comments = List.of(
                Comment.builder()
                        .id(1L)
                        .user(user)
                        .gethering(Gethering.builder().id(1L).build())
                        .content("댓글")
                        .createdAt(LocalDateTime.of(2021, 7, 1, 0, 0, 0))
                        .build(),
                Comment.builder()
                        .id(2L)
                        .user(user)
                        .gethering(Gethering.builder().id(1L).build())
                        .content("댓글")
                        .createdAt(LocalDateTime.of(2021, 7, 1, 0, 0, 0))
                        .build(),
                Comment.builder()
                        .id(3L)
                        .user(user)
                        .gethering(Gethering.builder().id(1L).build())
                        .content("댓글")
                        .createdAt(LocalDateTime.of(2021, 7, 1, 0, 0, 0))
                        .build()
        );

        Pageable pageable = Pageable.ofSize(10);

        Page<Comment> page = new PageImpl<>(comments, pageable, comments.size());

        given(commentService.list(any(Long.class), any(Integer.class), any(Integer.class)))
                .willReturn(page);

        // When
        mockMvc.perform(get("/api/v1/gethering/{id}/comment", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                       {
                         	"success": true,
                         	"data": {
                         		"content": [
                         			{
                         				"id": 1,
                         				"content": "댓글",
                         				"getheringId": 1,
                         				"nickName": "페이커",
                         				"createdAt": "2021-07-01T00:00:00"
                         			},
                         			{
                         				"id": 2,
                         				"content": "댓글",
                         				"getheringId": 1,
                         				"nickName": "페이커",
                         				"createdAt": "2021-07-01T00:00:00"
                         			},
                         			{
                         				"id": 3,
                         				"content": "댓글",
                         				"getheringId": 1,
                         				"nickName": "페이커",
                         				"createdAt": "2021-07-01T00:00:00"
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
                         		"totalElements": 3,
                         		"totalPages": 1,
                         		"size": 10,
                         		"number": 0,
                         		"sort": {
                         			"empty": true,
                         			"sorted": false,
                         			"unsorted": true
                         		},
                         		"first": true,
                         		"numberOfElements": 3,
                         		"empty": false
                         	},
                         	"message": "댓글 조회 성공"
                         }
                """));
    }

    @DisplayName("댓글 수정")
    @Test
    @WithMockUser
    public void update() throws Exception {
        // Given
        String requestBodyJson =
                """
                    {
                        "content": "댓글 수정"
                    }
                """;

        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("1234")
                .nickName("페이커")
                .build();

        Gethering gethering = Gethering.builder()
                .id(1L)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .user(user)
                .gethering(gethering)
                .content("댓글 수정")
                .createdAt(LocalDateTime.of(2021, 7, 1, 0, 0, 0))
                .build();

        given(commentService.update(any(Long.class), any(Long.class), any(CommentCommand.class)))
                .willReturn(comment);

        // When
        mockMvc.perform(put("/api/v1/gethering/{id}/comment/{id}/users/{id}", 1L, 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success": true,
                            "data": {
                                "id": 1,
                                "content": "댓글 수정",
                                "getheringId": 1,
                                "nickName": "페이커",
                                "createdAt": "2021-07-01T00:00:00"
                            },
                            "message": "댓글 수정 성공"
                        }
                """));
    }

    @DisplayName("댓글 삭제")
    @Test
    @WithMockUser
    public void deleteComment() throws Exception {
        // When
        mockMvc.perform(delete("/api/v1/gethering/{id}/comment/{id}/users/{id}", 1L, 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success": true,
                            "data": {
                                "id": 1
                            },
                            "message": "댓글 삭제 성공"
                        }
                """));
    }
}