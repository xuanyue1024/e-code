package com.ecode.dto;

import com.ecode.query.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 班级信息查询DTO
 *
 * @author 竹林听雨
 * @date 2024/11/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClassPageQueryDTO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 2002519740600817050L;

    @ApiModelProperty("班级名称")
    private String name;


}
