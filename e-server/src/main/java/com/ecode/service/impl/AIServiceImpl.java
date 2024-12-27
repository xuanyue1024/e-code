package com.ecode.service.impl;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.ecode.context.BaseContext;
import com.ecode.dto.AiInputDTO;
import com.ecode.enumeration.AiAction;
import com.ecode.properties.AIProperties;
import com.ecode.result.Result;
import com.ecode.service.AIService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
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
    private AIProperties aiProperties;

    @Override
    public Flux<Result<String>> getChat(AiInputDTO aiInputDTO) {
        String key = "ai:bailian:sessionId:" + BaseContext.getCurrentId();
        String sessionId = null;
        if (aiInputDTO.getAiAction() == AiAction.NEXT){
            sessionId = (String) redisTemplate.opsForValue().get(key);
        }else {
            redisTemplate.delete(key);
        }

        ApplicationParam param = ApplicationParam.builder()
                .apiKey(aiProperties.getBailianApiKey())
                .appId(aiProperties.getBailianId())
                .incrementalOutput(true)
                .prompt(aiInputDTO.getContent())
                .build();

        if (sessionId == null){
            //todo sessionId不存在，启动新对话
        }else {
            //sessionId存在，继续对话
            param.setSessionId(sessionId);
        }
        Application application =  new Application();

        return Flux.create(sink -> {
            try {
                Flowable<ApplicationResult> flowable = application.streamCall(param);
                flowable.subscribe(
                        result -> {
                            String output = result.getOutput().getText();
                            String sessionId1 = result.getOutput().getSessionId();
                            redisTemplate.opsForValue().set(key, sessionId1);
                            log.info("sessionId: {}", sessionId1);
                            log.info("Stream Output: {}", output);
                            sink.next(Result.success(output));
                        },
                        error -> {
                            log.error("Error during streamCall: {}", error.getMessage(), error);
                            sink.error(error);
                        },
                        () -> sink.complete()
                );
            } catch (NoApiKeyException | InputRequiredException | ApiException e) {
                log.error("Error during streamCall: {}", e.getMessage(), e);
                sink.error(e);
            }
        });
    }
}
