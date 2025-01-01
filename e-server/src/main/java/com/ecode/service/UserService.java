package com.ecode.service;

import com.ecode.dto.UserRegisterDTO;
import com.ecode.dto.UserUpdateDTO;
import com.ecode.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  用户服务类
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-09-22
 */
public interface UserService extends IService<User> {

    /**
     * 新增用户
     *
     * @param userRegisterDTO 用户注册dto
     */
    void save(UserRegisterDTO userRegisterDTO);

    /**
     * 更新用户
     *
     * @param userUpdateDTO 用户更新
     * @param id            id
     */
    void updateUser(UserUpdateDTO userUpdateDTO, Integer id);

    /**
     * 获取用户信息
     *
     * @param currentId 当前id
     * @return <p>
     */
    User getUserInfo(Integer currentId);

}
