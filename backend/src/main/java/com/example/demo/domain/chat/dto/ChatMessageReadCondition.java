package com.example.demo.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReadCondition {

    private Long chatRoomId;

    private Long lastChatMessageId = Long.MAX_VALUE;

    private Integer size;
}