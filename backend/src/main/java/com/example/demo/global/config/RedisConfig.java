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

    @Bean(name = "groupChannelTopic")
    public ChannelTopic groupChannelTopic() {
        return new ChannelTopic("groupChatRoom");
    }

    @Bean(name = "privateChannelTopic")
    public ChannelTopic privateChannelTopic() {
        return new ChannelTopic("privateChatRoom");
    }

    @Bean(name = "groupMessageListenerAdapter")
    public MessageListenerAdapter groupMessageListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "sendGroupMessage");
    }

    @Bean(name = "privateMessageListenerAdapter")
    public MessageListenerAdapter privateMessageListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "sendPrivateMessage");
    }

    @Bean(name = "redisGroupMessageListenerContainer")
    public RedisMessageListenerContainer redisGroupMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                              MessageListenerAdapter groupMessageListenerAdapter,
                                                              ChannelTopic groupChannelTopic){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(groupMessageListenerAdapter, groupChannelTopic);
        return container;
    }

    @Bean(name = "redisPrivateMessageListenerContainer")
    public RedisMessageListenerContainer redisPrivateMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                              MessageListenerAdapter privateMessageListenerAdapter,
                                                              ChannelTopic privateChannelTopic){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(privateMessageListenerAdapter, privateChannelTopic);
        return container;
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

}