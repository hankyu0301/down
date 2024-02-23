package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {

    ChatMessage findTop1ByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id = :chatRoomId AND cm.id < :lastMessageId ORDER BY cm.id DESC")
    List<ChatMessage> findLatestMessages(@Param("chatRoomId") Long chatRoomId, @Param("lastMessageId") Long lastMessageId, Pageable pageable);
}
