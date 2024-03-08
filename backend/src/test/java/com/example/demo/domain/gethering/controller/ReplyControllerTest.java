package com.example.demo.domain.gethering.controller;


import com.example.demo.domain.gethering.dto.command.ReplyCommand;
import com.example.demo.domain.gethering.entity.Comment;
import com.example.demo.domain.gethering.entity.Reply;
import com.example.demo.domain.gethering.service.ReplyService;
import com.example.demo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(ReplyController.class)
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReplyService replyService;

    @DisplayName("대댓글 생성 성공")
    @Test
    @WithMockUser
    void register() throws Exception {
        // given
        String requestBodyJson =
                """
                        {
                            "content": "대댓글 내용입니다."
                        }
                """;

        Comment comment = Comment.builder()
                .id(1L)
                .build();

        User user = User.builder()
                .id(1L)
                .nickName("페이커")
                .build();

        Reply reply = Reply.builder()
                .id(1L)
                .parent(comment)
                .user(user)
                .content("대댓글 내용입니다.")
                .build();

        given(replyService.register(any(Long.class), any(Long.class), any(Long.class), any(ReplyCommand.class)))
                .willReturn(reply);

        // when
        mockMvc.perform(post("/api/v1/gethering/1/comment/1/reply/users/1")
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
                                "content": "대댓글 내용입니다.",
                                "parentId": 1,
                                "nickname": "페이커"
                            },
                            "message": "대댓글이 생성되었습니다."
                        }
                """));
    }

    @DisplayName("대댓글 수정")
    @Test
    @WithMockUser
    public void modify() throws Exception {
        // given
        String requestBodyJson =
                """
                        {
                            "content": "대댓글 내용 수정"
                        }
                """;

        Reply reply = Reply.builder()
                .id(1L)
                .parent(Comment.builder().id(1L).build())
                .user(User.builder().id(1L).nickName("페이커").build())
                .content("대댓글 내용 수정")
                .build();

        given(replyService.modify(any(Long.class), any(Long.class), any(Long.class), any(Long.class), any(ReplyCommand.class)))
                .willReturn(reply);

        // when
        mockMvc.perform(put("/api/v1/gethering/{id}/comment/{id}/reply/{id}/users/{id}", 1, 1, 1, 1)
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
                                "content": "대댓글 내용 수정",
                                "parentId": 1,
                                "nickname": "페이커"
                            },
                            "message": "대댓글이 수정되었습니다."
                        }
                """));
    }

    @DisplayName("대댓글 삭제")
    @Test
    @WithMockUser
    public void deleteTest() throws Exception {
        // when
        mockMvc.perform(delete("/api/v1/gethering/{id}/comment/{id}/reply/{id}/users/{id}", 1, 1, 1, 1)
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
                            "message": "대댓글이 삭제되었습니다."
                        }
                """));
    }
}