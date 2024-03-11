package com.example.demo.domain.chat.service;

import com.example.demo.domain.chat.dto.request.ChatRoomCreateRequest;
import com.example.demo.domain.chat.dto.request.ChatRoomDeleteRequest;
import com.example.demo.domain.chat.dto.request.ChatRoomInviteRequest;
import com.example.demo.domain.chat.dto.response.*;
import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import com.example.demo.domain.chat.entity.ChatRoomUser;
import com.example.demo.domain.chat.repository.ChatMessageJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomUserJpaRepository;
import com.example.demo.domain.user.dto.response.UserInfoResponseDTO;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatRoomUserJpaRepository chatRoomUserJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,Object> chatRedisTemplate;
    private final ChannelTopic groupChannelTopic;

    public ChatRoomListResponseDto getAllChatRoomByUserId(long userId) {

        User user = getUserById(userId);

        List<ChatRoomUser> chatRoomUserList = chatRoomUserJpaRepository.findAllWithChatRoomByUser(user);

        List<SimpleChatRoomResponseDto> simpleChatRoomResponseDtoList = chatRoomUserList.stream()
                .map(cru -> {
                    //  각 채팅방의 이름과 최신 메시지의 내용, 발신시간을 반환 (SimpleChatRoomResponseDto)
                    ChatMessage chatMessage = chatMessageJpaRepository.findTop1ByChatRoomOrderByCreatedAtDesc(cru.getChatRoom());
                    return new SimpleChatRoomResponseDto(chatMessage.getContent(), chatMessage.getCreatedAt(), cru.getChatRoom().getChatRoomName(), cru.getChatRoom().getId());
                })
                .sorted(comparing(SimpleChatRoomResponseDto::getLastMessageTime).reversed())
                .collect(Collectors.toList());

        return new ChatRoomListResponseDto(simpleChatRoomResponseDtoList);
    }

    public ChatRoomDto getChatRoomWithUserListByChatRoomId(long chatRoomId) {

        ChatRoom chatRoom = getChatRoomWithUserList(chatRoomId);

        List<UserInfoResponseDTO> dtoList = chatRoom.getChatRoomUserList()
                .stream()
                .map(ChatRoomUser::getUser)
                .map(UserInfoResponseDTO::from)
                .collect(Collectors.toList());

        return new ChatRoomDto(chatRoom.getId(), chatRoom.getChatRoomName(), dtoList);
    }

    public ChatRoomCreateResponseDto createChatRoom(ChatRoomCreateRequest req) {

        ChatRoom chatRoomJpa = saveChatRoomToDatabase(req.getChatRoomName());

        User inviter = getUserById(req.getUserId());

        List<User> userList = userRepository.findAllById(req.getUserIdList());

        if(!userList.contains(inviter)) {
            userList.add(inviter);
        }

        String invitedUsers = userList.stream()
                .filter(user -> !user.equals(inviter)) // inviter를 제외한 사용자만 필터링
                .map(User::getUserName)
                .collect(Collectors.joining(", "));

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(chatRoomJpa.getId())
                .userId(inviter.getId())
                .userName(inviter.getUserName())
                .content(inviter.getUserName() + " 님이 " + invitedUsers + " 님을 초대했습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage chatMessage = saveChatMessageToDatabase(chatMessageDto.toEntity(chatRoomJpa));

        chatMessageDto.setChatMessageId(chatMessage.getId());

        List<ChatRoomUser> chatRoomUserList = userList.stream()
                .map(user -> new ChatRoomUser(user, chatRoomJpa))
                .collect(Collectors.toList());

        chatRoomUserJpaRepository.saveAll(chatRoomUserList);
        publishChatMessage(chatMessageDto);

        return new ChatRoomCreateResponseDto(chatRoomJpa.getId(), chatRoomJpa.getChatRoomName(), req.getUserIdList());
    }

    public ChatRoomDeleteResponseDto exitChatroom(Long chatRoomId, ChatRoomDeleteRequest req)   {

        User user = getUserById(req.getUserId());

        ChatRoom chatRoom = getChatRoomById(chatRoomId);

        ChatRoomUser chatRoomUser = getChatRoomUser(user, chatRoom);

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(chatRoom.getId())
                .userId(user.getId())
                .userName(user.getUserName())
                .content(user.getUserName() + " 님이 퇴장했습니다.")
                .type(ChatMessage.MessageType.QUIT)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage chatMessageEntity = saveChatMessageToDatabase(chatMessageDto.toEntity(chatRoom));

        chatMessageDto.setChatMessageId(chatMessageEntity.getId());

        deleteChatRoomUser(chatRoomUser);

        publishChatMessage(chatMessageDto);

        return new ChatRoomDeleteResponseDto(chatRoom.getId());
    }


    public ChatRoomInviteResponseDto inviteChatRoom(ChatRoomInviteRequest req) {

        User inviter = getUserById(req.getInviterId());

        User target = getUserById(req.getTargetId());

        ChatRoom chatRoom = getChatRoomById(req.getChatRoomId());

        validateChatRoomInvitation(inviter, target, chatRoom);

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(chatRoom.getId())
                .userId(inviter.getId())
                .userName(inviter.getUserName())
                .content(inviter.getUserName() + " 님이 " + target.getUserName() + " 님을 초대했습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage chatMessage = saveChatMessageToDatabase(chatMessageDto.toEntity(chatRoom));

        chatMessageDto.setChatMessageId(chatMessage.getId());

        ChatRoomUser chatRoomUser = new ChatRoomUser(target, chatRoom);
        chatRoomUser.updateFirstMessageId(chatMessage.getId());

        saveChatRoomUserToDatabase(chatRoomUser);
        publishChatMessage(chatMessageDto);

        return new ChatRoomInviteResponseDto(inviter.getId(), target.getId(), chatRoom.getId());
    }

    private void saveChatRoomUserToDatabase(ChatRoomUser chatRoomUser) {
        chatRoomUserJpaRepository.save(chatRoomUser);
    }

    private ChatRoom getChatRoomWithUserList(long chatRoomId) {
        return chatRoomJpaRepository.findByIdWithChatRoomUser(chatRoomId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));
    }

    private ChatRoom getChatRoomById(long chatRoomId) {
        return chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));
    }

    private ChatMessage saveChatMessageToDatabase(ChatMessage chatMessage) {
        return chatMessageJpaRepository.save(chatMessage);
    }

    private ChatRoom saveChatRoomToDatabase(String chatRoomName) {
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(chatRoomName)
                .build();
        return chatRoomJpaRepository.save(chatRoom);
    }

    private void deleteChatRoomUser(ChatRoomUser chatRoomUser) {
        chatRoomUserJpaRepository.delete(chatRoomUser);
    }

    private void publishChatMessage(ChatMessageDto chatMessageDto) {
        try {
            chatRedisTemplate.convertAndSend(groupChannelTopic.getTopic(), objectMapper.writeValueAsString(chatMessageDto));
        } catch (JsonProcessingException e) {
            throw new CustomException(ExceptionCode.JSON_PARSING_ERROR);
        }
    }

    private ChatRoomUser getChatRoomUser(User user , ChatRoom chatRoom) {
        return chatRoomUserJpaRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER));
    }

    private void validateChatRoomInvitation(User inviter, User target, ChatRoom chatRoom) {
        if(!chatRoomUserJpaRepository.existsByUserAndChatRoom(inviter, chatRoom)) {
            throw new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER);
        }

        if(chatRoomUserJpaRepository.existsByUserAndChatRoom(target, chatRoom)) {
            throw new CustomException(ExceptionCode.ALREADY_EXIST_CHAT_ROOM_USER);
        }
    }
}
