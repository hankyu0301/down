package com.example.demo.domain.chat.controller;

import com.example.demo.domain.chat.dto.ChatRoomCreateRequest;
import com.example.demo.domain.chat.dto.ChatRoomResponse;
import com.example.demo.domain.chat.dto.SimpleChatRoomDto;
import com.example.demo.domain.chat.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/chatRoom/list")
    public List<SimpleChatRoomDto> getChatRoomList(long userId) {
        return chatRoomService.getAllChatRoomByUserId(userId);
    }

    @GetMapping("/chatRoom/{chatRoomId}")
    public ChatRoomResponse getChatRoom(@PathVariable long chatRoomId) {
        return chatRoomService.getChatRoomWithUserListByChatRoomId(chatRoomId);
    }

    @PostMapping("/chatRoom")
    public void createChatRoom(@Valid @RequestBody ChatRoomCreateRequest req) {
        chatRoomService.createChatRoom(req);
    }

    @DeleteMapping("/chatRoom/{chatRoomId}")
    public void exitChatRoom(@PathVariable long chatRoomId, @RequestParam long userId) {
        chatRoomService.exitChatroom(userId, chatRoomId);
    }

}
