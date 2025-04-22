package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ecode.entity.po.CodeSubmission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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
@TableName(value = "class_score", autoResultMap = true)
@Schema(description="ClassScore对象")
@AllArgsConstructor
@NoArgsConstructor
public class ClassScore implements Serializable {

    @Serial
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

    @Schema(description = "代码提交记录")
    @TableField(value = "code_submission", typeHandler = JacksonTypeHandler.class)
    private List<CodeSubmission> codeSubmission;
}
