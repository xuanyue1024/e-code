package com.ecode.service.login.type.oauth2;

import com.alibaba.fastjson.JSONObject;
import com.ecode.entity.User;
import com.ecode.properties.OAuthProperties;
import com.ecode.service.login.type.UserLoginByOAuth2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service(GithubOAuth2.LOGIN_TYPE)
@Slf4j
public class GithubOAuth2 extends UserLoginByOAuth2 {

    public static final String LOGIN_TYPE = "github";


    public GithubOAuth2(OAuthProperties properties, RedisTemplate redisTemplate) {
        super(properties, redisTemplate);
    }

    @Override
    protected User parseUserInfo(JSONObject rawUserInfo) {
        String email = rawUserInfo.getString("email");
        String login = rawUserInfo.getString("login");
        String name = rawUserInfo.getString("name");
        String location = rawUserInfo.getString("location");
        String avatarUrl = rawUserInfo.getString("avatar_url");

        User user = User.builder()
                        .username(login)
                        .email(email)
                        .name(name)
                        .address(location)
                        .profilePicture(avatarUrl)
                        .build();
        log.info("成功获取到OAuth2用户信息{}", user);
        return user;
    }

    @Override
    protected String getOAuth2Type() {
        return LOGIN_TYPE;
    }
}
