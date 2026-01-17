package com.ecode.vo;

import com.ecode.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="OAuth2 注册")
public class OAuthRegisterVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4893178211774804239L;

    @Schema(description = "用户信息")
    private User user;

    @Schema(description = "注册码,验证邮箱是否伪造")
    private String registerCode;
}
