package com.ecode.vo;

import com.ecode.entity.OauthIdentities;
import com.ecode.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "oauth2与user VO")
public class UserOauthVO {

    @Schema(description = "用户")
    private User user;

    @Schema(description = "OAuth信息")
    private OauthIdentities oauthIdentities;
}
