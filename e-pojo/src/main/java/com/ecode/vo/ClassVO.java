package com.ecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "班级VO")
public class ClassVO implements Serializable {

    private static final long serialVersionUID = -8175862502928320993L;

    @ApiModelProperty(value = "班级id")
    private Integer id;

    @ApiModelProperty(value = "教师id")
    private Integer teacherId;

    @ApiModelProperty(value = "教师名称")
    private String teacherName;

    @ApiModelProperty(value = "班级名称")
    private String name;

    @ApiModelProperty(value = "邀请码")
    private String invitationCode;

    @ApiModelProperty(value = "加入人数")
    private Long joinNumber;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
