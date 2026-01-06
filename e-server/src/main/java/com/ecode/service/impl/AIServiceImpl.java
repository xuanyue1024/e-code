package com.ecode.service.impl;

import com.alibaba.fastjson.JSON;
import com.ecode.ai.repository.FileRepository;
import com.ecode.constant.AiSystemConstant;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.AiInputDTO;
import com.ecode.entity.AiChatHistory;
import com.ecode.entity.po.RepositoryFile;
import com.ecode.exception.AiException;
import com.ecode.mapper.AiChatHistoryMapper;
import com.ecode.result.Result;
import com.ecode.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@Slf4j
public class AIServiceImpl implements AIService {
    @Autowired
    private AiChatHistoryMapper aiChatHistoryMapper;

    @Autowired
    private ChatClient chatClient ;

    @Autowired
    private ChatClient titleChatClient;

    @Autowired
    private ChatClient generateQuestionClient;

    @Autowired
    private ChatClient questionAnswerClient;

    @Autowired
    private ChatClient questionAnswerClientVec;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ChatMemory chatMemory;
    /**
     * 获取聊天内容的方法
     *
     * @param aiInputDTO 输入数据传输对象，包含聊天ID和提示信息
     * @return 返回包含聊天结果的数据流
     * @throws AiException 当提供的聊天ID未找到时抛出异常
     */
    @Override
    public Flux<ServerSentEvent<Object>> getChat(AiInputDTO aiInputDTO) {
        AiChatHistory ach = aiChatHistoryMapper.selectById(aiInputDTO.getChatId());
        if (ach == null) {
            throw new AiException(MessageConstant.AI_CHAT_ID_NOT_FOUND);
        }

        String systemPrompt;
        switch (aiInputDTO.getType()) {
            case CHAT -> systemPrompt = AiSystemConstant.getSmartRecommendations() + BaseContext.getCurrentId();
            case CODE -> systemPrompt = AiSystemConstant.CODE_SYSTEM_PROMPT;
            default -> throw new AiException(MessageConstant.AI_CHAT_TYPE_NOT_FOUND);
        }


        Flux<ServerSentEvent<Object>> contentStream = chatClient.prompt()
                .user(aiInputDTO.getPrompt())
                .system(systemPrompt)
                .advisors()
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).conversationId(aiInputDTO.getChatId()).build())
                .stream()
                .content()
                .map(content -> ServerSentEvent.builder()
                        .data(Result.success(content))
                        .build()
                );

        Mono<ServerSentEvent<Object>> titleMono;
        List<Message> messages = chatMemory.get(aiInputDTO.getChatId());
        if (!messages.isEmpty() && messages.size() > 4) {
            titleMono = Mono.empty();
        }else {
            titleMono = Mono.fromCallable(() -> {
                    messages.add(new UserMessage(aiInputDTO.getPrompt()));
                    String title = titleChatClient.prompt()
                            .user(JSON.toJSONString(messages))
                            .call()
                            .content();

                    AiChatHistory aiChatHistory = AiChatHistory.builder()
                            .id(aiInputDTO.getChatId())
                            .title(title)
                            .build();
                    aiChatHistoryMapper.updateById(aiChatHistory);
                    return Result.success(title);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(t -> ServerSentEvent.builder()
                        .event("title")
                        .data(t)
                        .build()
                );
        }

        return Flux.merge(contentStream, titleMono);
    }

    @Override
    public Flux<Result<String>> questionAnswer(AiInputDTO aiInputDTO) {
        AiChatHistory ach = aiChatHistoryMapper.selectById(aiInputDTO.getChatId());
        if (ach == null) {
            throw new AiException(MessageConstant.AI_CHAT_ID_NOT_FOUND);
        }

        RepositoryFile file = fileRepository.getFile(aiInputDTO.getClassId());
        log.info("文件路径: {}", file == null ? "null" :file.getName());
        if (file == null) {
            log.info("AI解答走数据库答案通道");
            return questionAnswerClient.prompt()
                    .user(aiInputDTO.getPrompt())
                    .system(AiSystemConstant.CODE_SYSTEM_PROMPT + aiInputDTO.getProblemId())
                    .advisors(MessageChatMemoryAdvisor.builder(chatMemory).conversationId(aiInputDTO.getChatId()).build())
                    .stream()
                    .content()
                    .map(Result::success);
        }
        log.info("AI解答走知识库答案通道");
        return questionAnswerClientVec.prompt()
                .user(aiInputDTO.getPrompt())
                .system("请根据知识库内容回答用户问题")
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).conversationId(aiInputDTO.getChatId()).build())
                .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "file_name == '" + file.getName() + "' && classId == " + aiInputDTO.getClassId()))
                .stream()
                .content()
                .map(Result::success);
    }

    /**
     * 生成题目的方法
     *
     * @param require 题目要求
     * @return 返回包含生成题目的数据流
     */
    @Override
    public Flux<Result<String>> generateQuestion(String require) {
        return generateQuestionClient.prompt()
                .user(require)
                .stream()
                .content()
                .map(Result::success);
    }

}
