package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUserJpaRepository extends JpaRepository<ChatRoomUser, Long> {

    Optional<ChatRoomUser> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);
    boolean existsByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    void deleteByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    // 회원 ID로 채팅방 찾기
    List<ChatRoomUser> findAllByUserId(Long userId);

}
