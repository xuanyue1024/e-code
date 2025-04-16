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
@TableName("class_problem")
@Schema(description="ClassProblem对象")
public class ClassProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "班级题目id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "题库id")
    @TableField("problem_id")
    private Integer problemId;

    @Schema(description = "班级id")
    @TableField("class_id")
    private Integer classId;


}
