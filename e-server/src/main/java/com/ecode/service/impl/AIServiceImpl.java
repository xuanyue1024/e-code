package com.ecode.service.impl;

import com.ecode.dto.AiInputDTO;
import com.ecode.result.Result;
import com.ecode.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AIServiceImpl implements AIService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ChatClient chatClient;

    @Override
    public Flux<Result<String>> getChat(AiInputDTO aiInputDTO) {
        return chatClient.prompt()
                .user(aiInputDTO.getContent())
                .stream()
                .content()
                .map(Result::success);
    }
}
