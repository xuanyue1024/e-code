package com.ecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "所有用户登录返回的数据格式")
public class UserLoginVO implements Serializable {

    private static final long serialVersionUID = -8438672452042212117L;

    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("角色")
    private String role;

    @ApiModelProperty("jwt令牌")
    private String token;

}
