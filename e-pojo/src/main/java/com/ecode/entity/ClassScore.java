package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="ClassScore对象", description="")
public class ClassScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "班级学生题目得分id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "指定学生_班级id")
    @TableField("sc_id")
    private Integer scId;

    @ApiModelProperty(value = "班级题目id")
    @TableField("class_problem_id")
    private Integer classProblemId;

    @ApiModelProperty(value = "得分")
    @TableField("score")
    private Integer score;


}
