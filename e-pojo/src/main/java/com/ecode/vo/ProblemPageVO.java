package com.ecode.vo;

import com.ecode.enumeration.ProblemGrade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@Schema(description="ProblemPageVO")
public class ProblemPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "班级题库id")
    private Integer classProblemId;

    @Schema(description = "题库id")
    private Integer id;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "标签组ids")
    private List<Integer> tagIds;

    @Schema(description = "题目等级（0简单，1一般，2困难）")
    private ProblemGrade grade;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
