package com.example.demo.domain.chat.dto;

import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private Long chatRoomId;
    private Long userId;
    private String userName;
    private String content;
    private ChatMessage.MessageType type;    //ENTER, QUIT, TALK
    private LocalDateTime createdAt;

    public ChatMessage toEntity(ChatRoom chatRoom) {
        return new ChatMessage(
                this.userId,
                this.userName,
                this.content,
                this.getType(),
                chatRoom);
    }
}
