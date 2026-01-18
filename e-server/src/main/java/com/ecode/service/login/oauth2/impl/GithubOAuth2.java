package com.ecode.service.login.oauth2.impl;

import com.alibaba.fastjson.JSONObject;
import com.ecode.entity.OauthIdentities;
import com.ecode.entity.User;
import com.ecode.properties.OAuthProperties;
import com.ecode.service.login.oauth2.UserLoginByOAuth2;
import com.ecode.vo.UserOauthVO;
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
    protected UserOauthVO parseUserInfo(JSONObject rawUserInfo) {
        String id = rawUserInfo.getString("id");
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

        //字段有一些与user相同,方便未注册直接插入
        OauthIdentities oauthIdentities = OauthIdentities.builder()
                .provider(LOGIN_TYPE)
                .providerEmail(email)
                .providerId(id)
                .providerUsername(login)
                .build();

        return new UserOauthVO(user, oauthIdentities);
    }

    @Override
    protected String getOAuth2Type() {
        return LOGIN_TYPE;
    }
}
