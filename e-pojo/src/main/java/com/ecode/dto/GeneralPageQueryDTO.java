package com.ecode.dto;

import com.ecode.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 通用信息查询DTO
 *
 * @author 竹林听雨
 * @date 2024/11/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "通用信息查询DTO")
public class GeneralPageQueryDTO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 2002519740600817050L;

    @Schema(description = "名称")
    private String name;


}
