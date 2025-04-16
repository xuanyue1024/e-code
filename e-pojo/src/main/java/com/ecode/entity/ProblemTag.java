package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@Builder
@Accessors(chain = true)
@TableName("problem_tag")
@Schema(description="ProblemTag对象")
public class ProblemTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "题目标签id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "题目id")
    private Integer problemId;

    @Schema(description = "标签id")
    private Integer tagId;


}
