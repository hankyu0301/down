package com.example.demo.global.config;

import com.example.demo.domain.chat.service.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    /**
     * Email Redis 설정
     * */
    @Value("${spring.redis.host.email}")
    private String emailHost;

    @Value("${spring.redis.port.email}")
    private int emailPort;

    @Value("${spring.redis.password.email}")
    private String emailPassword;

    /**
     * Chat Redis 설정
     * */
    @Value("${spring.redis.host.chat}")
    private String chatHost;

    @Value("${spring.redis.port.chat}")
    private int chatPort;

    @Value("${spring.redis.password.chat}")
    private String chatPassword;

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatRoom");
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "sendMessage");
    }

    @Bean(name = "emailRedisConnectionFactory")
    @Primary
    public RedisConnectionFactory emailRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(emailHost);
        configuration.setPort(emailPort);
        configuration.setPassword(emailPassword);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean(name = "chatRedisConnectionFactory")
    public RedisConnectionFactory chatRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(chatHost);
        configuration.setPort(chatPort);
        configuration.setPassword(chatPassword);
        return new LettuceConnectionFactory(configuration);
    }


    /** RedisMessageListenerContainer는 Redis Channel(Topic)로 부터 메시지를 받고,
     주입된 리스너들에게 비동기적으로 dispatch 하는 역할을 수행하는 컨테이너이다.
     즉, 발행된 메시지 처리를 위한 리스너들을 설정할 수 있다.
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory,
                                                              MessageListenerAdapter messageListenerAdapter,
                                                              ChannelTopic channelTopic){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, channelTopic);
        return container;
    }
}