package com.ecode.service.impl;

import com.ecode.dto.UserRegisterDTO;
import com.ecode.entity.User;
import com.ecode.enumeration.UserStatus;
import com.ecode.mapper.UserMapper;
import com.ecode.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-09-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void save(UserRegisterDTO userRegisterDTO) {
        User user = User.builder()
                .status(UserStatus.ENABLE)
                .name("默认用户")
                .profilePicture("https://web-tlias-itheima1024.oss-cn-chengdu.aliyuncs.com/23f57464-eb9b-42a6-b98e-91ab1937fbf3.jpg")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .score(0L)
                .build();

        BeanUtils.copyProperties(userRegisterDTO, user);
        userMapper.insert(user);

    }
}
