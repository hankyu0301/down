package com.example.demo.global.event.chat;

import com.example.demo.domain.chat.dto.response.PrivateChatMessageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateChatMessageCreatedEvent {

    private Long receiverId;
    private Long senderId;
    private String senderName;
    private Long chatRoomId;
    private String chatRoomName;
    private String content;

    public PrivateChatMessageCreatedEvent(Long receiverId, String chatRoomName, PrivateChatMessageDto dto) {
        this.receiverId = receiverId;
        this.senderId = dto.getUserId();
        this.senderName = dto.getUserName();
        this.chatRoomId = dto.getChatRoomId();
        this.chatRoomName = chatRoomName;
        this.content = dto.getContent();
    }
}
