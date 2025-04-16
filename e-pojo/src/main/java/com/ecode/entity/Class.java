package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * class数据库对象
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-10-30
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("class")
@Schema(description="Class数据库对象")
public class Class implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "班级id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "教师id")
    @TableField("teacher_id")
    private Integer teacherId;

    @Schema(description = "班级名称")
    @TableField("name")
    private String name;

    @Schema(description = "邀请码")
    @TableField("invitation_code")
    private String invitationCode;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
