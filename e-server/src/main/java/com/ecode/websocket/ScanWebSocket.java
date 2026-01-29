package com.ecode.websocket;

import com.ecode.websocket.encoder.MessageEncoder;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 扫码登录WebSocket服务
 */
@Component
@ServerEndpoint(value = "/ws/scan/{sceneId}", encoders = {MessageEncoder.class})
@Slf4j
public class ScanWebSocket {

    //存放会话对象
    private static final Map<String, Session> sessionMap = new HashMap();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sceneId") String sceneId) {
        log.info("客户端：{}建立连接", sceneId);
        sessionMap.put(sceneId, session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sceneId") String sceneId) {
        log.info("收到来自客户端：{}的信息:{}", sceneId, message);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param sceneId
     */
    @OnClose
    public void onClose(@PathParam("sceneId") String sceneId) {
        log.info("连接断开:{}", sceneId);
        sessionMap.remove(sceneId);
    }

    public static void send(String sceneId, Object message) {
        Session session = sessionMap.get(sceneId);

        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendObject(message, sendResult -> {
                if (sendResult.isOK()) {
                    log.info("发送消息到{}成功", sceneId);
                }else {
                    log.error("发送消息到{}失败!", sceneId);
                }
            });
        }
    }

    /**
     * 群发
     *
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}