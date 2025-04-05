package com.ecode.service.login.type;

import com.ecode.constant.MessageConstant;
import com.ecode.dto.UserLoginDTO;
import com.ecode.entity.User;
import com.ecode.exception.LoginException;
import com.ecode.mapper.UserMapper;
import com.ecode.service.PasskeyAuthorizationService;
import com.ecode.service.login.LoginStrategy;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通行秘钥登录
 *
 * @author 竹林听雨
 * @date 2025/04/01
 */
@Service("passkey")
public class UserLoginByPasskey implements LoginStrategy {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasskeyAuthorizationService passkeyAuthorizationService;

    @SneakyThrows
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        Integer id = passkeyAuthorizationService.finishPasskeyAssertion(userLoginDTO.getIdentifier(), userLoginDTO.getCredential());
        User user = userMapper.selectById(id);
        if (user == null){
            throw new LoginException(MessageConstant.LOGIN_FAILED_USERNAME_OR_PASSWD);
        }
        return user;
    }
}
