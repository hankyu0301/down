package com.example.demo.domain.chat.service;

import com.example.demo.domain.chat.dto.request.ChatMessageCreateRequest;
import com.example.demo.domain.chat.dto.request.ChatMessageDeleteRequest;
import com.example.demo.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.demo.domain.chat.dto.response.ChatMessageDeleteResponseDto;
import com.example.demo.domain.chat.dto.response.ChatMessageDto;
import com.example.demo.domain.chat.dto.response.ChatMessageReadResponseDto;
import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import com.example.demo.domain.chat.entity.ChatRoomUser;
import com.example.demo.domain.chat.repository.ChatMessageJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomUserJpaRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.event.chat.ChatMessageCreatedEvent;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatMessageService {

    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatRoomUserJpaRepository chatRoomUserJpaRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,Object> chatRedisTemplate;
    private final ChannelTopic groupChannelTopic;
    private final ApplicationEventPublisher publisher;

    public void saveChatMessage(ChatMessageCreateRequest req) {
        User user = getUserById(req.getUserId());
        ChatRoom chatRoom = getChatRoomById(req.getChatRoomId());
        validateUserInChatRoom(user, chatRoom);

        List<Long> chatRoomUserIds = chatRoomUserJpaRepository.findChatRoomUserIdsByChatRoom(chatRoom);
        chatRoomUserIds.remove(user.getId());

        ChatMessageDto chatMessageDto = createChatMessage(user, req.getContent(), chatRoom);
        ChatMessage chatMessage = chatMessageDto.toEntity(chatRoom);
        chatMessageDto.setChatMessageId(chatMessage.getId());
        saveChatMessageToDatabase(chatMessage);
        publishChatMessage(chatMessageDto);
        publisher.publishEvent(new ChatMessageCreatedEvent(chatRoomUserIds, chatMessageDto));
    }

    public ChatMessageReadResponseDto findLatestMessage(ChatMessageReadCondition cond) {
        User user = getUserById(cond.getUserId());
        ChatRoom chatRoom = getChatRoomById(cond.getChatRoomId());
        ChatRoomUser chatRoomUser = getChatRoomUser(user, chatRoom);
        return createChatMessageReadResponseDto(chatRoomUser.getDeletedMessageIds(), cond);
    }

    public ChatMessageDeleteResponseDto deleteChatMessage(ChatMessageDeleteRequest req) {
        User user = getUserById(req.getUserId());
        ChatMessage chatMessage = getChatMessageById(req.getChatMessageId());
        ChatRoomUser chatRoomUser = getChatRoomUser(user, chatMessage.getChatRoom());
        deleteAndSaveChatMessage(chatMessage, chatRoomUser);
        return new ChatMessageDeleteResponseDto(chatMessage.getId());
    }

    private void deleteAndSaveChatMessage(ChatMessage chatMessage, ChatRoomUser chatRoomUser) {
        chatRoomUser.getDeletedMessageIds().add(chatMessage.getId());
        chatRoomUserJpaRepository.save(chatRoomUser);
    }

    private ChatMessage getChatMessageById(Long chatMessageId) {
        return chatMessageJpaRepository.findByIdWithChatRoom(chatMessageId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_MESSAGE));
    }

    private ChatRoomUser getChatRoomUser(User user, ChatRoom chatRoom) {
        return chatRoomUserJpaRepository.findByUserAndChatRoomWithDeletedMessageIds(user, chatRoom)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER));
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));
    }

    private ChatRoom getChatRoomById(long chatRoomId) {
        return chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));
    }

    private void validateUserInChatRoom(User user, ChatRoom chatRoom) {
        if (!chatRoomUserJpaRepository.existsByUserAndChatRoom(user, chatRoom)) {
            throw new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER);
        }
    }

    private ChatMessageDto createChatMessage(User user, String content, ChatRoom chatRoom) {
        return ChatMessageDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getChatRoomName())
                .userName(user.getUserName())
                .userId(user.getId())
                .content(content)
                .type(ChatMessage.MessageType.TALK)
                .createdAt(LocalDateTime.now())
                .build();
    }
    private void saveChatMessageToDatabase(ChatMessage chatMessage) {
        chatMessageJpaRepository.save(chatMessage);
    }

    private void publishChatMessage(ChatMessageDto chatMessageDto) {
        try {
            chatRedisTemplate.convertAndSend(groupChannelTopic.getTopic(), objectMapper.writeValueAsString(chatMessageDto));
        } catch (JsonProcessingException e) {
            throw new CustomException(ExceptionCode.JSON_PARSING_ERROR);
        }
    }

    private ChatMessageReadResponseDto createChatMessageReadResponseDto(List<Long> deletedMessageIds, ChatMessageReadCondition cond) {
        return ChatMessageReadResponseDto.toDto(chatMessageJpaRepository.findLatestMessages(deletedMessageIds, cond));
    }
}