package com.ecode.service.login.type;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecode.constant.MessageConstant;
import com.ecode.dto.UserLoginDTO;
import com.ecode.entity.User;
import com.ecode.exception.LoginException;
import com.ecode.mapper.UserMapper;
import com.ecode.service.login.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户密码登录
 *
 * @author 竹林听雨
 * @date 2024/09/22
 */
@Service("passwd")
public class UserLoginByPassword implements LoginStrategy {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(User::getId, User::getName, User::getUsername, User::getRole)
                .eq(User::getUsername, userLoginDTO.getUsername())
                .eq(User::getPassword, userLoginDTO.getPassword());

        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            throw new LoginException(MessageConstant.LOGIN_FAILED_USERNAME_OR_PASSWD);
        }
        return user;
    }
}
