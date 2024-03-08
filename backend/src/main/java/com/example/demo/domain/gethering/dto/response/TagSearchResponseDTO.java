package com.example.demo.domain.gethering.dto.response;

import com.example.demo.domain.gethering.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "태그 검색 응답")
public class TagSearchResponseDTO {

    @Schema(description = "태그 목록")
    private List<TagResponseDTO> tags;

    public static TagSearchResponseDTO of(List<Tag> tags) {
        return TagSearchResponseDTO.builder()
                .tags(tags.stream().map(TagResponseDTO::of).toList())
                .build();
    }
}