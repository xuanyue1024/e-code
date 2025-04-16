package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 标签VO
 *
 * @author 竹林听雨
 * @date 2024/12/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签返回的数据格式")
public class TagVO implements Serializable {

    private static final long serialVersionUID = 4657308623668794631L;

    @Schema(description = "标签id")
    private Integer id;

    @Schema(description = "标签名称")
    private String name;
}
