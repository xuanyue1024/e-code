package com.ecode.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ecode.enumeration.ProblemGrade;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问题详细内容VO
 *
 * @author 竹林听雨
 * @date 2024/12/28
 */
@Data
@ApiModel("题目详细信息展示")
public class ProblemVO implements Serializable {
    private static final long serialVersionUID = -8620456099506182450L;

    @ApiModelProperty(value = "题库id")
    private Integer id;

    @ApiModelProperty(value = "题目标题")
    private String title;

    @ApiModelProperty(value = "题目内容（md格式）")
    private String content;

    @ApiModelProperty(value = "要求，为空按默认值（md格式）")
    @TableField("`require`")
    private String require;

    @ApiModelProperty(value = "题目等级（0简单，1一般，2困难）")
    private ProblemGrade grade;

    @ApiModelProperty(value = "最大运行内存（MB)默认512")
    private String maxMemory;

    @ApiModelProperty(value = "最大运行时间（s）默认5")
    private Integer maxTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
