package com.ecode.websocket.encoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class MessageEncoder implements Encoder.Text<Object> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(Object message) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new EncodeException(message, "Cannot encode message to JSON", e);
        }
    }
}