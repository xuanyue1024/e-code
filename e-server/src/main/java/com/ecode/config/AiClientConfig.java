package com.ecode.config;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.ecode.ai.tools.ProblemRecommendationTools;
import com.ecode.ai.tools.ProblemSolutionTools;
import com.ecode.constant.AiSystemConstant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiClientConfig {
    @Autowired
    private ProblemRecommendationTools problemRecommendationTools;

    @Autowired
    private ProblemSolutionTools problemSolutionTools;

    /**
     * 创建聊天客户端的方法
     *
     * @param model      聊天模型
     * @param chatMemory 聊天记忆对象
     * @return 创建好的聊天客户端
     */
    @Bean
    public ChatClient chatClient(DashScopeChatModel model, ChatMemory chatMemory){
        return ChatClient
                .builder(model)
                .defaultSystem(AiSystemConstant.DEFAULT_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .defaultTools(problemRecommendationTools)
                .build();
    }

    @Bean
    public ChatClient questionAnswerClient(DashScopeChatModel model, ChatMemory chatMemory){
        return ChatClient
                .builder(model)
                .defaultSystem(AiSystemConstant.CODE_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .defaultTools(problemSolutionTools)
                .build();
    }

    /**
     * 创建生成问题的聊天客户端
     *
     * @param model 聊天模型实例
     * @return 构建的聊天客户端
     */
    @Bean
    public ChatClient generateQuestionClient(DashScopeChatModel model){
        return ChatClient
                .builder(model)
                .defaultSystem(AiSystemConstant.GENERATE_QUESTION)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

}
