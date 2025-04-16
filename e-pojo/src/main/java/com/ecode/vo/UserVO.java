package com.ecode.vo;

import com.ecode.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "UserVO")
public class UserVO extends User implements Serializable {

    private static final long serialVersionUID = -2236825515263152633L;

    @Schema(description = "总得分")
    private Integer totalScore;
}
