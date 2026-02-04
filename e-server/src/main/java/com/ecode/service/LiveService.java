package com.ecode.service;


import com.ecode.json.DanmakuMessage;

import java.util.List;

/**
 * <p>
 *  直播服务类
 * </p>
 *
 * @author 竹林听雨
 * @since 2026-02-03
 */

public interface LiveService {

    /**
     * 发送弹幕
     *
     * @param classId 班级id
     * @param msg     班级消息
     */
    void sendDanmaku(Integer classId, DanmakuMessage msg);

    /**
     * 获取最近N条弹幕（用于用户刚进入直播间时，显示历史弹幕）
     */
    List<DanmakuMessage> getLatestDanmaku(Integer classId, int count);
}
