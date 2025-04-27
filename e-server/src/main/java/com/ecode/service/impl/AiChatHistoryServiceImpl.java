package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.constant.MessageConstant;
import com.ecode.entity.AiChatHistory;
import com.ecode.enumeration.AiType;
import com.ecode.exception.AiException;
import com.ecode.mapper.AiChatHistoryMapper;
import com.ecode.service.AiChatHistoryService;
import com.ecode.vo.AiChatIdsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public String createChatId(Integer userId, AiType type) {
        AiChatHistory ach = AiChatHistory.builder()
                .type(type)
                .userId(userId)
                .createTime(LocalDateTime.now())
                .build();
        aiChatHistoryMapper.insert(ach);
        return ach.getId();
    }

    @Override
    public List<AiChatIdsVO> getChatIds(Integer userId, AiType type) {
        List<AiChatIdsVO> list = new ArrayList<>();

        aiChatHistoryMapper.selectList(
                new LambdaQueryWrapper<AiChatHistory>()
                        .eq(AiChatHistory::getUserId, userId)
                        .eq(AiChatHistory::getType, type)
                        .orderByDesc(AiChatHistory::getCreateTime)
        ).forEach(ach -> {
             AiChatIdsVO acv = AiChatIdsVO.builder()
                     .chgatId(ach.getId())
                     .createTime(ach.getCreateTime())
                     .build();
             list.add(acv);
         });
        return list;
    }

    @Override
    public void deleteChatId(String chatId, Integer userId) {
        int i = aiChatHistoryMapper.delete(
                new LambdaQueryWrapper<AiChatHistory>()
                        .eq(AiChatHistory::getId, chatId)
                        .eq(AiChatHistory::getUserId, userId)
        );
        if (i == 0) {
            throw new AiException(MessageConstant.AI_CHAT_ID_NOT_FOUND);
        }
    }
}
