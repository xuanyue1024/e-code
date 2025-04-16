package com.ecode.config;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiClientConfig {
    @Bean
    public ChatClient chatClient(DashScopeChatModel model){
        return ChatClient
                .builder(model)
                .defaultSystem("你是一个热心、可爱的智能助手，请以小团团的身份和语气回答问题。")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()/*,
                        new MessageChatMemoryAdvisor(chatMemory)*/
                )
                .build();
    }
}
