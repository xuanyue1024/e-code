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
 * student_class实体
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-07
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("student_class")
@Schema(description="StudentClass对象")
public class StudentClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "学生id")
    @TableField("student_id")
    private Integer studentId;

    @Schema(description = "班级id")
    @TableField("class_id")
    private Integer classId;

    @Schema(description = "加入时间")
    @TableField("join_time")
    private LocalDateTime joinTime;


}
