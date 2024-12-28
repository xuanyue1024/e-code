package com.ecode.vo;

import com.ecode.enumeration.ProblemGrade;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-28
 */
@Data
@ApiModel(value="ProblemVO", description="")
public class ProblemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题库id")
    private Integer id;

    @ApiModelProperty(value = "题目标题")
    private String title;

    @ApiModelProperty(value = "标签组id")
    private Integer problemTagId;

    @ApiModelProperty(value = "题目等级（0简单，1一般，2困难）")
    private ProblemGrade grade;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
