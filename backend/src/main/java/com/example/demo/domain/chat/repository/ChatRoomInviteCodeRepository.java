package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.entity.ChatRoomInviteCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChatRoomInviteCodeRepository extends CrudRepository<ChatRoomInviteCode, Long> {
    Optional<ChatRoomInviteCode> findByInviteCode(String inviteCode);
}
