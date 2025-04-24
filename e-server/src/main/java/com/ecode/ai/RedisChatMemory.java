package com.ecode.ai;

import com.ecode.entity.po.AiMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RedisChatMemory implements ChatMemory {

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper;

    private final static String PREFIX = "chat:message:";

    @Override
    public void add(String conversationId, List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        List<String> list = messages.stream().map(AiMessage::new).map(msg -> {
            try {
                return objectMapper.writeValueAsString(msg);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        redisTemplate.opsForList().rightPushAll(PREFIX + conversationId, list);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<String> list = redisTemplate.opsForList().range(PREFIX + conversationId, 0, lastN);
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        return list.stream().map(s -> {
            try {
                return objectMapper.readValue(s, AiMessage.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).map(AiMessage::toMessage).toList();
    }

    @Override
    public void clear(String conversationId) {
        redisTemplate.delete(PREFIX + conversationId);
    }
}