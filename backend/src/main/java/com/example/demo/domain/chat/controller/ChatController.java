package com.example.demo.domain.chat.controller;


import com.example.demo.domain.chat.dto.ChatMessageDto;
import com.example.demo.domain.chat.dto.ChatMessageCreateRequest;
import com.example.demo.domain.chat.dto.ChatMessageReadCondition;
import com.example.demo.domain.chat.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatMessageService chatMessageService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageCreateRequest req){
        chatMessageService.saveChatMessage(req);
    }

    /**
     * 특정 채팅방의 최근 메시지 20개를 가져옴
     * */
    @GetMapping("/chat/message")
    public ResponseEntity<List<ChatMessageDto>> messageList(@Valid ChatMessageReadCondition cond){
        return new ResponseEntity<>(chatMessageService.findLatestMessage(cond), HttpStatus.OK);
    }
}
