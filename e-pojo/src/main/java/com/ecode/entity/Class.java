package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ecode.entity.po.RepositoryFile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
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
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "class", autoResultMap = true)
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

    @Schema(description = "知识库文件")
    @TableField(value = "repository_file", typeHandler = JacksonTypeHandler.class)
    private RepositoryFile repositoryFile;

    @Schema(description = "加入人数")
    @TableField("join_number")
    private Integer joinNumber;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
