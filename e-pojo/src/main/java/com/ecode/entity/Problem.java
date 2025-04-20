package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ecode.enumeration.ProblemGrade;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description="Problem对象")
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "题库id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "题目内容（md格式）")
    private String content;

    @Schema(description = "题目答案（md格式）")
    @TableField("`answer`")
    private String answer;

    @Schema(description = "标签组id")
    @Deprecated
    private Integer problemTagId;

    @Schema(description = "题目等级（0简单，1一般，2困难）")
    private ProblemGrade grade;

    @Schema(description = "最大运行内存（MB)默认512")
    private String maxMemory;

    @Schema(description = "最大运行时间（s）默认5")
    private Integer maxTime;

    @Schema(description = "测试输入1")
    private String inputTest1;

    @Schema(description = "测试输出1")
    private String outputTest1;

    @Schema(description = "测试输入2")
    private String inputTest2;

    @Schema(description = "测试输出2")
    private String outputTest2;

    @Schema(description = "测试输入3")
    private String inputTest3;

    @Schema(description = "测试输出3")
    private String outputTest3;

    @Schema(description = "测试输入4")
    private String inputTest4;

    @Schema(description = "测试输出4")
    private String outputTest4;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


}
