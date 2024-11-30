package com.ecode.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "分页查询实体")
public class PageQuery implements Serializable {
    private static final long serialVersionUID = -4369849971849494143L;

    @ApiModelProperty("页码")
    private Long pageNo;
    @ApiModelProperty("每页记录数")
    private Long pageSize;
    @ApiModelProperty("排序字段")
    private String sortBy;
    @ApiModelProperty("是否升序")
    private Boolean isAsc;
}