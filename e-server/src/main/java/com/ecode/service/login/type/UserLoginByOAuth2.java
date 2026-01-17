package com.ecode.service.login.type;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecode.constant.MessageConstant;
import com.ecode.dto.UserLoginDTO;
import com.ecode.entity.User;
import com.ecode.enumeration.Redis;
import com.ecode.exception.BaseException;
import com.ecode.properties.OAuthProperties;
import com.ecode.service.login.LoginStrategy;
import com.ecode.utils.OkHttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * OAuth2 登录
 *
 * @author 竹林听雨
 * @date 2026/01/13
 */
@Service
@RequiredArgsConstructor
@Slf4j
public abstract class UserLoginByOAuth2 implements LoginStrategy {

    private final OAuthProperties properties;

    private final RedisTemplate redisTemplate;

    /**
     * 获取当前策略对应的配置信息
     */
    protected OAuthProperties.AuthSourceConfig getConfig() {
        // getOAuth2Type() 会返回 "GITHUB" 或 "GITEE"
        // 统一转成小写去 Map 里匹配 yml 中的 key
        String type = getOAuth2Type().toLowerCase();

        OAuthProperties.AuthSourceConfig config = properties.getConfigs().get(type);

        if (config == null) {
            throw new RuntimeException("未找到平台 [" + type + "] 的配置信息，请检查 application.yml");
        }
        return config;
    }

    @Override
    public String prepare() {
        String state = UUID.randomUUID().toString();
        String type = getOAuth2Type().toLowerCase();

        String url = String.format("%s/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=read:user&state=%s",
                getConfig().getAuthorizeUrl(), getConfig().getClientId(), getConfig().getRedirectUri(), state);

        redisTemplate.opsForValue().set(Redis.OAUTH2_STATE + state, type,
                Redis.OAUTH2_STATE.getTimeout(), Redis.OAUTH2_STATE.getTimeUnit());

        return url;
    }

    @Override
    public User login(UserLoginDTO userLoginDTO) {

        // 1. 换取 Access Token
        String accessToken = fetchAccessToken(userLoginDTO.getAuthCode(), userLoginDTO.getState());

        // 2. 获取第三方原始用户信息
        JSONObject rawUserInfo = fetchRawUserInfo(accessToken);

        // 3. 将不同的 JSON 转换成统一的系统用户对象 (这是子类唯一需要特别处理的地方)

        return parseUserInfo(rawUserInfo);
    }

    /**
     *  根据 accessToken 获取用户信息
     * @param accessToken 
     * @return
     */
    private JSONObject fetchRawUserInfo(String accessToken) {
        String userInfo = OkHttpUtils.builder().url(getConfig().getUserInfoUrl())
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .sync();

        if (userInfo == null) {
            log.error("oauth2 获取用户信息失败");
            throw new BaseException(MessageConstant.AUTH_FAILS);
        }
        
        return JSON.parseObject(userInfo);
    }

    /**
     * 根据回调code获取accessToken
     * @param authCode
     * @param state
     * @return
     */
    private String fetchAccessToken(String authCode, String state) {
        OAuthProperties.AuthSourceConfig config = getConfig();

        String type = String.valueOf(redisTemplate.opsForValue().getAndDelete(Redis.OAUTH2_STATE + state));
        if (type == null) {
            throw new BaseException(MessageConstant.PARAMETER_ERROR);
        }

        String reStr = OkHttpUtils.builder().url(config.getAccessTokenUrl())
                .addParam("client_id", config.getClientId())
                .addParam("client_secret", config.getClientSecret())
                .addParam("state", state)
                .addParam("redirect_uri", config.getRedirectUri())
                .addParam("code", authCode)
                .addHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .addHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .get()
                .sync();
        JSONObject jsonObject = JSON.parseObject(reStr);
        String accessToken = jsonObject.getString("access_token");
        if (accessToken == null) {
            log.error(MessageConstant.ACCESS_TOKEN_GET_FAILED + "{}", reStr);
            throw new BaseException(MessageConstant.ACCESS_TOKEN_GET_FAILED);
        }
        return accessToken;
    }

    /**
     * 把获取到的用户信息转化为标准 {@link User}
     * @param rawUserInfo OAuth2返回的用户信息
     * @return
     */
    protected abstract User parseUserInfo(JSONObject rawUserInfo);

    /**
     * OAuth2渠道,如github,gitee
     * @return
     */
    protected abstract String getOAuth2Type();

}
