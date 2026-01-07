package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ecode.enumeration.AiType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 会话标题
     */
    @TableField(value = "title")
    private String title;

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

}
