package com.example.demo.domain.user.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "닉네임 중복 체크 요청")
public class CheckNickNameCommand {

        @Schema(description = "중복 체크할 닉네임", example = "페이커")
        private String nickName;
}
