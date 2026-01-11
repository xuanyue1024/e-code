package com.ecode.context;

import cn.dev33.satoken.stp.StpUtil;
import com.ecode.enumeration.UserRole;

import java.util.List;

/**
 * sa-token线程存储封装工具类
 *
 * @author 竹林听雨
 * @date 2026/01/11
 */
public class BaseContext {

    public static Integer getCurrentId() {
        return StpUtil.getLoginIdAsInt();
    }

    public static UserRole getCurrentRole() {
        List<String> roleList = StpUtil.getRoleList();
        return Enum.valueOf(UserRole.class, roleList.get(0));
    }

}

