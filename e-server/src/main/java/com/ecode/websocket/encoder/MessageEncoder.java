package com.ecode.websocket.encoder;


import com.alibaba.fastjson.JSON;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class MessageEncoder implements Encoder.Text<Object> {

    @Override
    public String encode(Object message) throws EncodeException {
        try {
            return JSON.toJSONString(message);
        } catch (Exception e) {
            throw new EncodeException(message, "Cannot encode message to JSON", e);
        }
    }
}