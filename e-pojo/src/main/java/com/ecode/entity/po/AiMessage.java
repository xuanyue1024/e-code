package com.ecode.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AiMessage {
    MessageType messageType;
    String text;
    Map<String, Object> metadata;

    public AiMessage(Message message) {
        this.messageType = message.getMessageType();
        this.text = message.getText();
        this.metadata = message.getMetadata();
    }

    public Message toMessage() {
        return switch (messageType) {
            case SYSTEM -> new SystemMessage(text);
            case USER -> UserMessage.builder().text(text).media(List.of()).metadata(metadata).build();
            case ASSISTANT -> AssistantMessage.builder().content(text).properties(metadata).build();
            default -> throw new IllegalArgumentException("Unsupported message type: " + messageType);
        };
    }
}