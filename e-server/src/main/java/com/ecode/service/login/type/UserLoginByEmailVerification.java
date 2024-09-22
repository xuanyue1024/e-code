package com.ecode.service.login.type;

import com.ecode.dto.UserLoginDTO;
import com.ecode.service.login.LoginStrategy;
import com.ecode.vo.UserLoginVO;
import org.springframework.stereotype.Service;

@Service("email")
public class UserLoginByEmailVerification implements LoginStrategy {

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 模拟手机号验证码登录的逻辑
        return null;
    }
}
