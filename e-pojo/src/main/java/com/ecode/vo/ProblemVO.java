package com.ecode.vo;

import com.ecode.entity.Tag;
import com.ecode.enumeration.ProblemGrade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 问题详细内容VO
 *
 * @author 竹林听雨
 * @date 2024/12/28
 */
@Data
@Schema(description = "题目详细信息展示")
public class ProblemVO implements Serializable {
    private static final long serialVersionUID = -8620456099506182450L;

    @Schema(description = "题库id")
    private Integer id;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "标签组")
    private List<Tag> tags;

    @Schema(description = "题目内容（md格式）")
    private String content;

    @Schema(description = "题目答案（md格式）")
    private String answer;

    @Schema(description = "题目等级（0简单，1一般，2困难）")
    private ProblemGrade grade;

    @Schema(description = "最大运行内存（MB)默认512")
    private String maxMemory;

    @Schema(description = "最大运行时间（s）默认5")
    private Integer maxTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
