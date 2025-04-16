package com.ecode.vo;

import com.ecode.enumeration.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "所有用户登录返回的数据格式")
public class UserLoginVO implements Serializable {

    private static final long serialVersionUID = -8438672452042212117L;

    @Schema(description = "主键值")
    private Integer id;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "昵称")
    private String name;

    @Schema(description = "角色")
    private UserRole role;

    @Schema(description = "jwt令牌")
    private String token;

}
