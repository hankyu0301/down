package com.example.demo.domain.chat.dto;

import com.example.demo.domain.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageCreateRequest {

    private Long chatRoomId;
    private Long userId;
    private String content;
    private ChatMessage.MessageType type;    //ENTER, QUIT, TALK
    private LocalDateTime createdAt;

}
