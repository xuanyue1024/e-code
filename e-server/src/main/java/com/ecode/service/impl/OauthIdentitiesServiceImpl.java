package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.entity.OauthIdentities;
import com.ecode.mapper.OauthIdentitiesMapper;
import com.ecode.service.OauthIdentitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 竹林听雨
 * @since 2026-01-18
 */
@Service
@RequiredArgsConstructor
public class OauthIdentitiesServiceImpl extends ServiceImpl<OauthIdentitiesMapper, OauthIdentities> implements OauthIdentitiesService {

    private final OauthIdentitiesMapper mapper;


    public OauthIdentities getByProviderId(String provider, String providerId) {
        return mapper.selectOne(
                new LambdaQueryWrapper<OauthIdentities>()
                        .eq(OauthIdentities::getProvider, provider)
                        .eq(OauthIdentities::getProviderId, providerId)
        );
    }


    public int insertOauthIdentities(OauthIdentities oauthIdentities) {
        oauthIdentities.setCreateTime(LocalDateTime.now());
        oauthIdentities.setUpdateTime(LocalDateTime.now());
        return mapper.insert(oauthIdentities);
    }
}
