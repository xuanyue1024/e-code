package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("problem_tag")
@ApiModel(value="ProblemTag对象", description="")
public class ProblemTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目标签id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目id")
    private Integer problemId;

    @ApiModelProperty(value = "标签id")
    private Integer tagId;


}
