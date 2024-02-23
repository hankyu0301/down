package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.service.RedisSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;

    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;

    private final StringRedisTemplate stringRedisTemplate;

    private static ValueOperations<String, String> topics;

    @PostConstruct
    private void init() {
        topics = stringRedisTemplate.opsForValue();
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
            ChannelTopic topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.set(roomId, topic.toString());
    }

    public void exitChatRoom(String roomId) {
        String topicToString = topics.get(roomId);
        ChannelTopic topic = new ChannelTopic(topicToString);
        redisMessageListener.removeMessageListener(redisSubscriber, topic);
    }


    public static ChannelTopic getTopic(String roomId) {
        String topicToString = topics.get(roomId);
        return new ChannelTopic(topicToString);
    }
}
