package com.ecode.service;

import com.ecode.dto.AdminUserCreateDTO;
import com.ecode.dto.AdminUserPageQueryDTO;
import com.ecode.dto.AdminUserUpdateDTO;
import com.ecode.dto.UserRegisterDTO;
import com.ecode.dto.UserUpdateDTO;
import com.ecode.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecode.enumeration.UserStatus;
import com.ecode.vo.AdminUserVO;
import com.ecode.vo.PageVO;
import com.ecode.vo.ScanVO;

import java.util.List;

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

    /**
     * 根据用户id获取图像
     * @param id
     * @return
     */
    String getProfilePictureById(Integer id);

    /**
     * 通过邮箱获取用户名和姓名
     * @param email 邮箱
     * @return 用户名和姓名
     */
    User getUserByEmail(String email);

    /**
     * 生成登录二维码
     * @return 二维码信息
     */
    ScanVO scanGenerate();

    /**
     * 管理员分页查询用户。
     *
     * @param queryDTO 查询条件
     * @return 用户分页结果
     */
    PageVO<AdminUserVO> adminPage(AdminUserPageQueryDTO queryDTO);

    /**
     * 管理员查询用户详情。
     *
     * @param id 用户id
     * @return 用户详情
     */
    AdminUserVO adminGetById(Integer id);

    /**
     * 管理员创建用户。
     *
     * @param createDTO 创建参数
     */
    void adminCreate(AdminUserCreateDTO createDTO);

    /**
     * 管理员更新用户。
     *
     * @param updateDTO 更新参数
     */
    void adminUpdate(AdminUserUpdateDTO updateDTO);

    /**
     * 管理员修改用户状态。
     *
     * @param id 用户id
     * @param status 账号状态
     */
    void adminUpdateStatus(Integer id, UserStatus status);

    /**
     * 管理员批量删除用户。
     *
     * @param ids 用户id集合
     */
    void adminDeleteBatch(List<Integer> ids);
}
