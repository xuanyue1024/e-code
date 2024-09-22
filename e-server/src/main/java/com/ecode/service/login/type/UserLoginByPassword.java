package com.ecode.service.login.type;

import com.ecode.dto.UserLoginDTO;
import com.ecode.service.login.LoginStrategy;
import com.ecode.vo.UserLoginVO;
import org.springframework.stereotype.Service;

@Service("passwd")
public class UserLoginByPassword implements LoginStrategy {

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 模拟用户名密码登录的逻辑
        return null;
    }
}
