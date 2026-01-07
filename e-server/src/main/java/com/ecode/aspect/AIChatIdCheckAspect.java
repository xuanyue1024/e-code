package com.ecode.aspect;

import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.AiInputDTO;
import com.ecode.entity.AiChatHistory;
import com.ecode.exception.BaseException;
import com.ecode.mapper.AiChatHistoryMapper;
import com.ecode.service.AiChatHistoryService;
import com.ecode.utils.EUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * AI聊天ID校验
 * <p>
 * 判断聊天ID是否符合格式以及是否所属登录用户
 *
 * @author 竹林听雨
 * @date 2025/01/07
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AIChatIdCheckAspect {

    private final AiChatHistoryMapper aiChatHistoryMapper;

    private final AiChatHistoryService aiChatHistoryService;

    /**
     * 在方法执行前进行数据访问权限校验的切面方法。
     *
     * @param joinPoint 切入点，用于获取方法执行的相关信息
     */
    @Before("@annotation(com.ecode.annotation.AIChatIdCheck)")
    public void DataAccessCheck(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof AiInputDTO aiInputDTO) {

                String chatId = aiInputDTO.getChatId();
                boolean uuid = EUtil.isAssignUUID(chatId);
                if (!uuid) {
                    throw new BaseException(MessageConstant.AI_CHAT_ID_NOT_STANDARD);
                }
                AiChatHistory aiChatHistory = aiChatHistoryMapper.selectById(chatId);

                if (aiChatHistory == null) {
                    aiChatHistoryService.createChatId(chatId, BaseContext.getCurrentId(), aiInputDTO.getType());
                }else if (!Objects.equals(BaseContext.getCurrentId(), aiChatHistory.getUserId())){
                    throw new BaseException(MessageConstant.DATA_ACCESS_DENIED);
                }

            }
        }
    }

}
