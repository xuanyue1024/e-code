package com.ecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签VO
 *
 * @author 竹林听雨
 * @date 2024/12/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "标签返回的数据格式")
public class TagVO {

    private static final long serialVersionUID = 4657308623668794631L;

    @ApiModelProperty(value = "标签id")
    private Integer id;

    @ApiModelProperty(value = "标签名称")
    private String name;
}
