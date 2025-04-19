package com.ecode.service.impl;

import com.ecode.constant.MessageConstant;
import com.ecode.dto.AiInputDTO;
import com.ecode.entity.AiChatHistory;
import com.ecode.exception.AiException;
import com.ecode.mapper.AiChatHistoryMapper;
import com.ecode.result.Result;
import com.ecode.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AIServiceImpl implements AIService {
    @Autowired
    private AiChatHistoryMapper aiChatHistoryMapper;

    @Autowired
    private ChatClient chatClient;

    /**
     * 获取聊天内容的方法
     *
     * @param aiInputDTO 输入数据传输对象，包含聊天ID和提示信息
     * @return 返回包含聊天结果的数据流
     * @throws AiException 当提供的聊天ID未找到时抛出异常
     */
    @Override
    public Flux<Result<String>> getChat(AiInputDTO aiInputDTO) {
        AiChatHistory ach = aiChatHistoryMapper.selectById(aiInputDTO.getChatId());
        if (ach == null) {
            throw new AiException(MessageConstant.AI_CHAT_ID_NOT_FOUND);
        }

        return chatClient.prompt()
                .user(aiInputDTO.getPrompt())
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, aiInputDTO.getChatId()))
                .stream()
                .content()
                .map(Result::success);
    }

}
