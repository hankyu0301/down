package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.demo.domain.chat.dto.response.PrivateChatMessageDto;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomPrivateChatMessageRepository {

    Slice<PrivateChatMessageDto> findLatestMessages(ChatMessageReadCondition cond, List<Long> deletedMessageIds, Long limit);
}
