package com.ecode.service;

import com.ecode.entity.OauthIdentities;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 竹林听雨
 * @since 2026-01-18
 */
public interface OauthIdentitiesService extends IService<OauthIdentities> {

    /**
     * 根据用户三方平台名称,账号获取绑定用户
     * @param provider
     * @param providerId
     * @return
     */
    OauthIdentities getByProviderId(String provider, String providerId);

    /**
     * 新增字段
     * @param oauthIdentities
     * @return
     */
    int insertOauthIdentities(OauthIdentities oauthIdentities);
}
