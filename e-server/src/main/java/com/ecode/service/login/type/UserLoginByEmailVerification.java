package com.ecode.service.login.type;

import com.ecode.dto.UserLoginDTO;
import com.ecode.entity.User;
import com.ecode.service.login.LoginStrategy;
import org.springframework.stereotype.Service;

@Service("email")
public class UserLoginByEmailVerification implements LoginStrategy {

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        // 模拟手机号验证码登录的逻辑
        return null;
    }
}
