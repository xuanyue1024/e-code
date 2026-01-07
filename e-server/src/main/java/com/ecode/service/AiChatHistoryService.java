package com.ecode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecode.entity.AiChatHistory;
import com.ecode.enumeration.AiType;
import com.ecode.vo.AiChatIdsVO;

import java.util.List;

/**
 * <p>
 *  AI对话历史服务类，用于储存AI会话id列表
 * </p>
 *
 * @author 竹林听雨
 * @since 2025-04-19
 */
public interface AiChatHistoryService extends IService<AiChatHistory> {
    /**
     * 生成会话ID的方法。
     *
     * @param uuid 表id
     * @param userId 用户ID
     * @param type   业务类型，例如"chat"、"service"、"pdf"等
     * @return 返回生成的会话ID字符串
     */
    AiChatHistory createChatId(String uuid, Integer userId, AiType type);

    /**
     * 根据用户ID和业务类型获取会话ID列表。
     *
     * @param userId 用户ID
     * @param type   业务类型，例如："chat"、"service"、"pdf"
     * @return 会话ID的列表
     */
    List<AiChatIdsVO> getChatIds(Integer userId, AiType type);

    /**
     * 根据用户ID和业务类型删除会话ID。
     *
     * @param chatId 会话ID
     * @param userId 用户ID
     */
    void deleteChatId(String chatId, Integer userId);
}
