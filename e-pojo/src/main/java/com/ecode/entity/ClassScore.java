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

/**
 * <p>
 * 
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("class_score")
@Schema(description="ClassScore对象")
public class ClassScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "班级学生题目得分id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "指定学生_班级id")
    @TableField("sc_id")
    private Integer scId;

    @Schema(description = "班级题目id")
    @TableField("class_problem_id")
    private Integer classProblemId;

    @Schema(description = "得分")
    @TableField("score")
    private Integer score;


    @Schema(description = "提交次数")
    @TableField("submit_number")
    private Integer submitNumber;

    @Schema(description = "通过次数")
    @TableField("pass_number")
    private Integer passNumber;
}
