package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.demo.domain.chat.dto.response.ChatMessageDto;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomChatMessageRepository {

    Slice<ChatMessageDto> findLatestMessages(List<Long> deletedMessageIds, ChatMessageReadCondition cond);
}
