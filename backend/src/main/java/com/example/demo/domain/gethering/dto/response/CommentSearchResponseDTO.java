package com.example.demo.domain.gethering.dto.response;

import com.example.demo.domain.gethering.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
@Schema(description = "댓글 검색 응답")
public class CommentSearchResponseDTO {
    public static CommentSearchResponseDTO of(Page<Comment> page) {
        return CommentSearchResponseDTO.builder()
                .comments(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
