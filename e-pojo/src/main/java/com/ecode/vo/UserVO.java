package com.ecode.vo;

import com.ecode.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserVO extends User {
    private static final long serialVersionUID = -2236825515263152633L;

    @ApiModelProperty("总得分")
    private Integer totalScore;
}
