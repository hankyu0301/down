package com.example.demo.domain.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleChatRoomDto {

    //  채팅방 이름, 마지막 메세지, 마지막 메세지 발신 시간
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private String chatRoomName;

}
