package com.ecode.service;

import com.ecode.dto.AiInputDTO;
import com.ecode.dto.EvaluateAnswerDTO;
import com.ecode.vo.EvaluateResultVO;
import com.ecode.result.Result;
import jakarta.validation.Valid;
import org.springframework.http.codec.ServerSentEvent;
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
    Flux<ServerSentEvent<Object>> getChat(AiInputDTO aiInputDTO);

    /**
     * 生成题目
     *
     * @param require 题目要求
     * @return Flux < ServerSentEvent< Object >>
     */
    Flux<ServerSentEvent<Object>> generateQuestion(String require);

    /**
     * questionAnswer方法，处理AI输入数据并返回结果流
     *
     * @param aiInputDTO 有效验证的AI输入数据传输对象
     * @return 包含结果的Flux流
     */
    Flux<ServerSentEvent<Object>> questionAnswer(@Valid AiInputDTO aiInputDTO);

    /**
     * evaluateAnswer方法，根据答案和相关信息要求进行评测
     *
     * @param dto 评测答案所需参数
     * @return 返回评测结果VO
     */
    EvaluateResultVO evaluateAnswer(@Valid EvaluateAnswerDTO dto);
}
