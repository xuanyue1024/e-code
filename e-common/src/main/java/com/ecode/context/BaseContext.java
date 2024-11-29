package com.ecode.context;

import com.ecode.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 线程存储
 *
 * @author 竹林听雨
 * @date 2024/11/23
 */
public class BaseContext {

    /**
     * 上下文数据(用户id与角色）
     *
     * @author 竹林听雨
     * @date 2024/11/23
     */
    @AllArgsConstructor
    @Data
    public static class ContextData {
        private Integer id;
        private UserRole role;
    }

    // 使用 ThreadLocal 存储 ContextData 对象
    public static ThreadLocal<ContextData> threadLocal = new ThreadLocal<>();

    public static void setCurrentData(Integer id, UserRole role) {
        threadLocal.set(new ContextData(id, role));
    }

    public static ContextData getCurrentData() {
        return threadLocal.get();
    }

    public static void removeCurrentData() {
        threadLocal.remove();
    }

    public static Integer getCurrentId() {
        ContextData data = threadLocal.get();
        return data != null ? data.getId() : null;
    }

    public static UserRole getCurrentRole() {
        ContextData data = threadLocal.get();
        return data != null ? data.getRole() : null;
    }

    public static void setCurrentId(Integer id) {
        ContextData data = threadLocal.get();
        if (data == null) {
            data = new ContextData(id, null);
        } else {
            data.setId(id);
        }
        threadLocal.set(data);
    }

    public static void setCurrentRole(UserRole role) {
        ContextData data = threadLocal.get();
        if (data == null) {
            data = new ContextData(null, role);
        } else {
            data.setRole(role);
        }
        threadLocal.set(data);
    }
}

