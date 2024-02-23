package com.example.demo.domain.chat.service;

import com.example.demo.domain.chat.dto.ChatMessageDto;
import com.example.demo.domain.chat.dto.ChatMessageCreateRequest;
import com.example.demo.domain.chat.dto.ChatMessageReadCondition;
import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import com.example.demo.domain.chat.entity.ChatRoomUser;
import com.example.demo.domain.chat.repository.*;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.domain.chat.repository.ChatRoomRepository.getTopic;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatRoomUserJpaRepository chatRoomUserJpaRepository;
    private final UserRepository userRepository;
    private final RedisPublisher redisPublisher;

    public void saveChatMessage(ChatMessageCreateRequest req) {

        User user = userRepository.findById(req.getUserId()).orElseThrow(EntityNotFoundException::new);
        ChatRoom chatRoom = chatRoomJpaRepository.findById(req.getChatRoomId()).orElseThrow(EntityNotFoundException::new);
        ChatRoomUser chatRoomUser = chatRoomUserJpaRepository.findByUserIdAndChatRoomId(req.getUserId(), req.getChatRoomId()).orElseThrow(EntityNotFoundException::new);

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(req.getChatRoomId())
                .userName(user.getUserName())
                .userId(req.getUserId())
                .content(req.getContent())
                .type(req.getType())
                .createdAt(LocalDateTime.now())
                .build();

        String chatRoomIdStr = String.valueOf(req.getChatRoomId());

        chatMessageRepository.save(chatMessageDto); // redis에 저장
        ChatMessage chatMessage = chatMessageDto.toEntity(chatRoom);
        chatMessageJpaRepository.save(chatMessage); //  MySQL에 저장
        redisPublisher.messagePublish(getTopic(chatRoomIdStr), chatMessageDto);  //  webSocket에 메세지 발행
    }

    /**
     * 채팅방 입장시 redis를 조회해서 캐싱된 메시지가 있으면 불러오고
     * 없으면 jpaRepository에서 가장 최근에 생성된 20개만 불러옴.
     */

    public List<ChatMessageDto> findLatestMessage(ChatMessageReadCondition cond) {

        List<ChatMessageDto> chatMessageDtoList = chatMessageRepository.findAllMessage(cond.getChatRoomId());

        if (chatMessageDtoList.size() > 0) {
            return chatMessageDtoList;
        } else {
            chatMessageDtoList = chatMessageJpaRepository.findLatestMessages(cond.getChatRoomId(), cond.getLastChatMessageId(), Pageable.ofSize(cond.getSize()))
                            .stream()
                            .map(ChatMessage::of)
                            .toList();

            //redis에 정보가 없으니, 다음부터 조회할때는 redis를 사용하기 위하여 넣어준다.
            chatMessageRepository.saveAll(chatMessageDtoList, cond.getChatRoomId());
            return chatMessageDtoList;
        }
    }
}
