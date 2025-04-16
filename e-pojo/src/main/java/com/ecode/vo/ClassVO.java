package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "班级VO")
public class ClassVO implements Serializable {

    private static final long serialVersionUID = -8175862502928320993L;

    @Schema(description = "班级id")
    private Integer id;

    @Schema(description = "教师id")
    private Integer teacherId;

    @Schema(description = "教师名称")
    private String teacherName;

    @Schema(description = "班级名称")
    private String name;

    @Schema(description = "邀请码")
    private String invitationCode;

    @Schema(description = "加入人数")
    private Long joinNumber;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
