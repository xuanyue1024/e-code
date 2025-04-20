package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ecode.enumeration.AiType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * ai会话历史实体类
 * </p>
 *
 * @author 竹林听雨
 * @since 2025-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ai_chat_history")
@Builder
public class AiChatHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话历史主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 会话所属用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 会话类型
     */
    @TableField("type")
    private AiType type;

    /**
     * 会话创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 会话历史内容(不再创建一个新表，而是序列化为json)
     */
    @TableField(value = "history_content", typeHandler = JacksonTypeHandler.class)
    private List<String> historyContent;


}
