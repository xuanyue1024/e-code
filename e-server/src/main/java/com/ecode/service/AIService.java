package com.ecode.service;

import com.ecode.dto.AiInputDTO;
import com.ecode.result.Result;
import reactor.core.publisher.Flux;

/**
 * AI服务
 *
 * @author 竹林听雨
 * @date 2024/12/27
 */
public interface AIService {
    /**
     * 获取聊天
     *
     * @param aiInputDTO Ai输入
     * @return Flux < result< string>>
     */
    Flux<Result<String>> getChat(AiInputDTO aiInputDTO);

}
