package com.ecode.service.impl;


import com.ecode.annotation.DataAccessCheck;
import com.ecode.enumeration.OperationType;
import com.ecode.enumeration.Redis;
import com.ecode.json.DanmakuMessage;
import com.ecode.service.LiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 直播服务类
 *
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-02-03  23:05
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LiveServiceImpl implements LiveService {

    private final StringRedisTemplate redisTemplate;

    /**
     * 发送弹幕
     *
     * @param classId 班级id
     * @param msg     班级消息
     */
    @DataAccessCheck(value = OperationType.ALL_TO_CLASS, paramIndex = 0)
    public void sendDanmaku(Integer classId, DanmakuMessage msg) {
        String key = Redis.DANMAKU_STORE.toString() + classId;

        redisTemplate.opsForStream().add(StreamRecords.newRecord()
                .ofObject(msg)
                .withStreamKey(key));
    }

    /**
     * 获取最近N条弹幕（用于用户刚进入直播间时，显示历史弹幕）
     */
    @DataAccessCheck(value = OperationType.ALL_TO_CLASS, paramIndex = 0)
    public List<DanmakuMessage> getLatestDanmaku(Integer classId, int count) {
        String key = Redis.DANMAKU_STORE.toString() + classId;
        // 从 Stream 末尾往前取 count 条
        List<ObjectRecord<String, DanmakuMessage>> range = redisTemplate.opsForStream()
                .range(DanmakuMessage.class,
                        key,
                        Range.unbounded(), //不限制范围
                        Limit.limit().count(count));// 最多取 count 条
        return range.stream()
                .map(Record::getValue)
                .toList();
    }
}
