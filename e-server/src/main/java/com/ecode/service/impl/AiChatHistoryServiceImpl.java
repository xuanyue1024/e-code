package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.entity.AiChatHistory;
import com.ecode.mapper.AiChatHistoryMapper;
import com.ecode.service.AiChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 竹林听雨
 * @since 2025-04-19
 */
@Service
public class AiChatHistoryServiceImpl extends ServiceImpl<AiChatHistoryMapper, AiChatHistory> implements AiChatHistoryService {

    @Autowired
    private AiChatHistoryMapper aiChatHistoryMapper;

    @Override
    public String createChatId(Integer userId, String type) {
        AiChatHistory ach = AiChatHistory.builder()
                .type(type)
                .userId(userId)
                .build();
        aiChatHistoryMapper.insert(ach);
        return ach.getId();
    }

    @Override
    public List<String> getChatIds(Integer userId, String type) {
        return aiChatHistoryMapper.selectList(
                new LambdaQueryWrapper<AiChatHistory>()
                        .eq(AiChatHistory::getUserId, userId)
                        .eq(AiChatHistory::getType, type)
        ).stream()
         .map(AiChatHistory::getId)
         .toList(); // 返回空集合时，toList() 会自动处理为空集合
    }
}
