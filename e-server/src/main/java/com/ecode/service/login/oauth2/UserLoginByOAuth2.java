package com.ecode.service.login.oauth2;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecode.constant.MessageConstant;
import com.ecode.dto.UserLoginDTO;
import com.ecode.enumeration.Redis;
import com.ecode.exception.BaseException;
import com.ecode.properties.OAuthProperties;
import com.ecode.service.login.OAuth2Strategy;
import com.ecode.utils.OkHttpUtils;
import com.ecode.vo.UserOauthVO;
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
public abstract class UserLoginByOAuth2 implements OAuth2Strategy {

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

        String url = String.format("%s?client_id=%s&redirect_uri=%s&scope=read:user&state=%s",
                getConfig().getAuthorizeUrl(), getConfig().getClientId(), getConfig().getRedirectUri(), state);

        redisTemplate.opsForValue().set(Redis.OAUTH2_STATE + state, type,
                Redis.OAUTH2_STATE.getTimeout(), Redis.OAUTH2_STATE.getTimeUnit());

        String authorizeUrl = getConfig().getAuthorizeUrl() + "&state=" + state;

        return authorizeUrl;
    }

    @Override
    public UserOauthVO callback(UserLoginDTO userLoginDTO) {

        // 1. 换取 Access Token
        String accessToken = fetchAccessToken(userLoginDTO.getAuthCode(), userLoginDTO.getState());

        // 2. 获取第三方原始用户信息
        JSONObject rawUserInfo = fetchRawUserInfo(accessToken);


        // 3. 将不同的 JSON 转换成统一的系统用户对象 (这是子类唯一需要特别处理的地方)
        UserOauthVO userOauthVO = parseUserInfo(rawUserInfo);

        if (getConfig().getEmailUrl() != null) {
            String emailInfo = fetchEmailInfo(accessToken);
            String email = parseEmail(emailInfo);
            userOauthVO.getOauthIdentities().setProviderEmail(email);
            userOauthVO.getUser().setEmail(email);
        }

        return userOauthVO;
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
     * 获取邮箱
     * @param accessToken
     * @return
     */
    private String fetchEmailInfo(String accessToken) {
        String emailInfo = OkHttpUtils.builder().url(getConfig().getEmailUrl())
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .sync();

        if (emailInfo == null) {
            log.error("oauth2 获取用户邮箱信息失败");
            throw new BaseException(MessageConstant.AUTH_FAILS);
        }

        return emailInfo;
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
                .addParam("grant_type", "authorization_code")// Gitee专有参数
                .addParam("client_id", config.getClientId())
                .addParam("client_secret", config.getClientSecret())
                .addParam("state", state)
                .addParam("redirect_uri", config.getRedirectUri())
                .addParam("code", authCode)
                .addHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .addHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .post(false)
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
     * 根据获取到的用户信息去查找是否存在
     * @param rawUserInfo OAuth2返回的用户信息
     * @return
     */
    protected abstract UserOauthVO parseUserInfo(JSONObject rawUserInfo);

    /**
     * 获取邮箱,针对一些平台比如GitHub邮箱需要单独接口获取,不需要直接返回null
     * @param emailInfo
     * @return
     */
    protected  String parseEmail(String emailInfo) {
        JSONObject jsonObject = JSONArray.parseArray(emailInfo).getJSONObject(0);
        return jsonObject.getString("email");
    }

    /**
     * OAuth2渠道,如github,gitee
     * @return
     */
    protected abstract String getOAuth2Type();

}
