package com.ecode.websocket;


import com.ecode.websocket.encoder.MessageEncoder;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket 直播 服务端点
 *
 */
@Component
@Slf4j
@ServerEndpoint(value = "/ws/live/{classId}", encoders = {MessageEncoder.class})
public class LiveWebSocket {

    // 用 ConcurrentHashMap 存储：房间ID → 所有连接到该房间的 WebSocket 会话
    // CopyOnWriteArraySet 是线程安全的 Set，适合读多写少（观众进/出房间不频繁）
    private static final Map<Integer, CopyOnWriteArraySet<Session>> ROOM_SESSIONS = new ConcurrentHashMap<>();


    /**
     * 当客户端连接成功时触发
     * @param session 当前连接的会话对象
     * @param classId 班级id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("classId") Integer classId) {
        // 把当前 session 加入对应房间的集合
        ROOM_SESSIONS.computeIfAbsent(classId, k -> new CopyOnWriteArraySet<>()).add(session);
        log.info("用户加入房间: {}, 当前连接数: {}", classId, ROOM_SESSIONS.get(classId).size());

        broadcastToRoom(classId, Map.of("number", ROOM_SESSIONS.get(classId).size()));
        // 发送最近 10 条历史弹幕（让新用户看到之前的弹幕）
        // var latest = danmakuStreamService.getLatestDanmaku(Long.parseLong(classId), 10);
        // for (var danmaku : latest) { ... }
    }

    /**
     * 当客户端断开连接时触发
     */
    @OnClose
    public void onClose(Session session, @PathParam("classId") Integer classId) {
        var sessions = ROOM_SESSIONS.get(classId);
        if (sessions != null) {
            sessions.remove(session); // 移除当前会话
            if (sessions.isEmpty()) {
                ROOM_SESSIONS.remove(classId); // 如果房间没人了，清理内存
            }else {
                broadcastToRoom(classId, Map.of("number", ROOM_SESSIONS.get(classId).size()));
            }
        }
        log.info("用户离开房间: {}", classId);
    }

    /**
     * 连接发生错误时触发
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 向指定房间的所有在线用户广播一条弹幕
     */
    public static void broadcastToRoom(Integer classId, Object message) {
        var sessions = ROOM_SESSIONS.get(classId);
        if (sessions == null || sessions.isEmpty()) return;

        for (Session session : sessions) {
            if (!session.isOpen()) continue;

            session.getAsyncRemote().sendObject(message, sendResult -> {
                if (sendResult.isOK()) {
                    log.info("发送消息到{}成功", classId);
                }else {
                    log.error("发送消息到{}失败!", classId);
                }
            });
        }
    }
}