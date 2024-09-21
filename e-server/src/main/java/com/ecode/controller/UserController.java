package com.ecode.controller;

import com.ecode.dto.UserLoginByPasswdDTO;
import com.ecode.result.Result;
import com.ecode.vo.UserLoginVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = "所有用户信息统一处理接口")
@RequestMapping("/user")
public class UserController {
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginByPasswdDTO userLoginByPasswdDTO){
        return Result.success(new UserLoginVO(1L, "111", "nihao", "424rfewfrefrgfegdg"));
    }
}
