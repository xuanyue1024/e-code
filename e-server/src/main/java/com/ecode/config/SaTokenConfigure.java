package com.ecode.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.ecode.constant.JwtClaimsConstant;
import com.ecode.entity.User;
import com.ecode.enumeration.UserRole;
import com.ecode.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

/**
 * sa-token 自动配置
 */
@Configuration
@RequiredArgsConstructor
public class SaTokenConfigure implements StpInterface {

    private final UserMapper userMapper;

    // Sa-Token 整合 jwt (Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        UserRole role;
        if (loginId.equals(StpUtil.getLoginId())) {
            role = Enum.valueOf(UserRole.class, String.valueOf(StpUtil.getExtra(JwtClaimsConstant.ROLE)));
        }else {
            User user = userMapper.selectById((Serializable) loginId);
            role = user.getRole();
        }
        return List.of(role.name());
    }
}
