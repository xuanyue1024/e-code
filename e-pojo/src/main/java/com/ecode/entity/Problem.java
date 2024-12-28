package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ecode.enumeration.ProblemGrade;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("problem")
@ApiModel(value="Problem对象", description="")
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题库id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目标题")
    private String title;

    @ApiModelProperty(value = "题目内容（md格式）")
    private String content;

    @ApiModelProperty(value = "要求，为空按默认值（md格式）")
    private String require;

    @ApiModelProperty(value = "标签组id")
    private Integer problemTagId;

    @ApiModelProperty(value = "题目等级（0简单，1一般，2困难）")
    private ProblemGrade grade;

    @ApiModelProperty(value = "最大运行内存（MB)默认512")
    private String maxMemory;

    @ApiModelProperty(value = "最大运行时间（s）默认5")
    private Integer maxTime;

    @ApiModelProperty(value = "测试输入1")
    private String inputTest1;

    @ApiModelProperty(value = "测试输出1")
    private String outputTest1;

    @ApiModelProperty(value = "测试输入2")
    private String inputTest2;

    @ApiModelProperty(value = "测试输出2")
    private String outputTest2;

    @ApiModelProperty(value = "测试输入3")
    private String inputTest3;

    @ApiModelProperty(value = "测试输出3")
    private String outputTest3;

    @ApiModelProperty(value = "测试输入4")
    private String inputTest4;

    @ApiModelProperty(value = "测试输出4")
    private String outputTest4;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
