package com.ecode.controller;

import com.ecode.dto.UserLoginByPasswdDTO;
import com.ecode.dto.UserLoginDTO;
import com.ecode.result.Result;
import com.ecode.service.login.LoginStrategy;
import com.ecode.service.login.LoginStrategyFactory;
import com.ecode.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = "所有用户信息统一处理接口")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LoginStrategyFactory loginStrategyFactory;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        LoginStrategy strategy = loginStrategyFactory.getStrategy(userLoginDTO.getLoginType());
        return Result.success(new UserLoginVO(1L, "111", "nihao", "424rfewfrefrgfegdg"));
    }
}
