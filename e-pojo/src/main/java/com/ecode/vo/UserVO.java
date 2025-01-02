package com.ecode.vo;

import com.ecode.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value="UserVO", description="")
public class UserVO extends User implements Serializable {

    private static final long serialVersionUID = -2236825515263152633L;

    @ApiModelProperty("总得分")
    private Integer totalScore;
}
