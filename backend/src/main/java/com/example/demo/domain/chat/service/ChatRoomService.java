package com.example.demo.domain.chat.service;

import com.example.demo.domain.chat.dto.ChatRoomCreateRequest;
import com.example.demo.domain.chat.dto.ChatRoomResponse;
import com.example.demo.domain.chat.dto.SimpleChatRoomDto;
import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import com.example.demo.domain.chat.entity.ChatRoomUser;
import com.example.demo.domain.chat.repository.ChatMessageJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomRepository;
import com.example.demo.domain.chat.repository.ChatRoomUserJpaRepository;
import com.example.demo.domain.user.entity.UserEntity;
import com.example.demo.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserJpaRepository chatRoomUserJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    public List<SimpleChatRoomDto> getAllChatRoomByUserId(long userId) {
        List<ChatRoomUser> chatRoomUserList = chatRoomUserJpaRepository.findAllByUserId(userId);
        List<SimpleChatRoomDto> simpleChatRoomDtoList = new ArrayList<>();

        for(ChatRoomUser cru : chatRoomUserList){
            ChatMessage chatMessage = chatMessageJpaRepository.findTop1ByChatRoomIdOrderByCreatedAtDesc(cru.getChatRoom().getId());
            ChatRoom chatRoom = chatRoomJpaRepository.findById(cru.getChatRoom().getId()).orElseThrow(EntityNotFoundException::new);
            simpleChatRoomDtoList.add(new SimpleChatRoomDto(chatMessage.getContent(), chatMessage.getCreatedAt(), chatRoom.getChatRoomName()));
        }
        return simpleChatRoomDtoList;
    }

    public ChatRoomResponse getChatRoomWithUserListByChatRoomId(long chatRoomId) {
        ChatRoom chatRoom = chatRoomJpaRepository.findChatRoomWithUsers(chatRoomId);
        return new ChatRoomResponse(chatRoom.getId(), chatRoom.getChatRoomName());
    }

    public ChatRoom createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest) {

        ChatRoom chatRoomJpa = chatRoomJpaRepository.save(new ChatRoom(chatRoomCreateRequest.getChatRoomName()));

        List<UserEntity> userList = userRepository.findAllById(chatRoomCreateRequest.getUserIdList());
        List<ChatRoomUser> chatRoomUserList = new ArrayList<>();

        for(UserEntity user : userList){
            ChatRoomUser chatRoomUser = new ChatRoomUser(user, chatRoomJpa);
            chatRoomUserList.add(chatRoomUser);
        }

        chatRoomRepository.enterChatRoom(String.valueOf(chatRoomJpa.getId()));
        chatRoomUserJpaRepository.saveAll(chatRoomUserList);
        return chatRoomJpa;
    }

    public void exitChatroom(long memberId, long chatRoomId) {
        chatRoomRepository.exitChatRoom(String.valueOf(chatRoomId));
        chatRoomUserJpaRepository.deleteByUserIdAndChatRoomId(memberId, chatRoomId);
    }

}
