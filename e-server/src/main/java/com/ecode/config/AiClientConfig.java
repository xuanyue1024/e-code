package com.ecode.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.ecode.ai.tools.ProblemRecommendationTools;
import com.ecode.ai.tools.ProblemSolutionTools;
import com.ecode.constant.AiSystemConstant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.ai.vectorstore.redis.autoconfigure.RedisVectorStoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPooled;

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
     * @return 创建好的聊天客户端
     */
    @Bean
    public ChatClient chatClient(DashScopeChatModel model){
        return ChatClient
                .builder(model)
                .defaultSystem(AiSystemConstant.DEFAULT_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .defaultTools(problemRecommendationTools)
                .build();
    }

    /**
     * 创建聊天标题生成的方法
     *
     * @param model 聊天模型
     * @return 创建好的聊天客户端
     */
    @Bean
    public ChatClient titleChatClient(DashScopeChatModel model){
        return ChatClient
                .builder(model)
                .defaultOptions(ChatOptions.builder()
                        .model("qwen1.5-110b-chat")
                        .build())
                .defaultSystem(AiSystemConstant.TITLE_GENERATION)
                .defaultAdvisors(SimpleLoggerAdvisor.builder().build())
                .build();
    }

    @Bean
    public ChatClient questionAnswerClient(DashScopeChatModel model){
        return ChatClient
                .builder(model)
                .defaultSystem(AiSystemConstant.CODE_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .defaultTools(problemSolutionTools)
                .build();
    }
    @Bean
    public ChatClient questionAnswerClientVec(DashScopeChatModel model, VectorStore vectorStore){
        return ChatClient
                .builder(model)
                .defaultSystem(AiSystemConstant.CODE_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        QuestionAnswerAdvisor.builder(vectorStore).searchRequest(
                                SearchRequest.builder() // 向量检索的请求参数
                                        .similarityThreshold(0.3) // 相似度阈值
                                        .topK(2) // 返回的文档片段数量
                                        .build()
                        ).build()
                )
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

    //redis向量数据库初始化不初始元数据字段而不得已而为之,只能自己手动配置
    // https://github.com/spring-projects/spring-ai/pull/3809
    // 1. 先把 JedisConnectionFactory 转换成 JedisPooled 并注册为 Bean
    @Bean
    public JedisPooled jedisPooled(JedisConnectionFactory factory) {
        RedisStandaloneConfiguration config = factory.getStandaloneConfiguration();

        // 处理带密码的情况
        if (config.getPassword().isPresent()) {
            return new JedisPooled(
                    config.getHostName(),
                    config.getPort(),
                    null,
                    new String(config.getPassword().get())
            );
        }
        // 处理无密码的情况
        return new JedisPooled(config.getHostName(), config.getPort());
    }

    // 2. 此时 Parameter 0 就能找到 JedisPooled Bean 了
    @Bean
    public RedisVectorStore vectorStore(
            JedisPooled jedisPooled, // 现在可以找到了
            EmbeddingModel embeddingModel,
            RedisVectorStoreProperties properties) {
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName(properties.getIndexName())
                .prefix(properties.getPrefix())
                .initializeSchema(true)
                .metadataFields(
                        RedisVectorStore.MetadataField.numeric("classId"),
                        RedisVectorStore.MetadataField.text("file_name")
                )
                .build();
    }

}
