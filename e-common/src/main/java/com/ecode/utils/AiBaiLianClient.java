package com.ecode.utils;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.ecode.properties.AIProperties;
import com.ecode.result.Result;
import io.reactivex.Flowable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@Deprecated//废弃
public class AiBaiLianClient {
    @Autowired
    private AIProperties aiProperties;

    private Application application;
    @Getter
    private String sessionId;
    @PostConstruct
    public void init() {
        this.application = new Application();
    }

    public Flux<Result<String>> createStreamCompletion(String prompt, String sessionId) {
        ApplicationParam param = ApplicationParam.builder()
                .apiKey(aiProperties.getBailianApiKey())
                .appId(aiProperties.getBailianId())
                .incrementalOutput(true)
                .prompt(prompt)
                .build();

        if (sessionId == null){
            //todo sessionId不存在，启动新对话
        }else {
            //sessionId存在，继续对话
            param.setSessionId(sessionId);
        }
        return Flux.create(sink -> {
            try {
                Flowable<ApplicationResult> flowable = application.streamCall(param);

                flowable.subscribe(
                        result -> {
                            String output = result.getOutput().getText();
                            log.info("sessionId: {}", output);
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