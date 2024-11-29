package com.ecode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecode.entity.Class;

/**
 * 班级服务类
 *
 * @author 竹林听雨
 * @date 2024/11/23
 */
public interface ClassService extends IService<Class> {
    /**
     * 添加班级
     *
     * @param name 班级名称
     */
    void addClass(String name);
}
