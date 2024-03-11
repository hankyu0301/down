package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.entity.PrivateChatMessage;
import com.example.demo.domain.chat.entity.PrivateChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateChatMessageJpaRepository extends JpaRepository<PrivateChatMessage, Long>, CustomPrivateChatMessageRepository {

    PrivateChatMessage findTop1ByPrivateChatRoomOrderByCreatedAtDesc(PrivateChatRoom pcr);

}
