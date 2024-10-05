package com.ecode.controller.user;

import com.ecode.constant.JwtClaimsConstant;
import com.ecode.dto.UserLoginDTO;
import com.ecode.dto.UserRegisterDTO;
import com.ecode.entity.User;
import com.ecode.properties.JwtProperties;
import com.ecode.result.Result;
import com.ecode.service.UserService;
import com.ecode.service.login.LoginStrategy;
import com.ecode.service.login.LoginStrategyFactory;
import com.ecode.utils.JwtUtil;
import com.ecode.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理
 *
 * @author 竹林听雨
 * @date 2024/09/22
 */
@RestController
@Slf4j
@Api(tags = "用户管理")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LoginStrategyFactory loginStrategyFactory;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param userLoginDTO 用户登录dto
     * @return 结果<user login vo>
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        LoginStrategy strategy = loginStrategyFactory.getStrategy(userLoginDTO.getLoginType());
        User user = strategy.login(userLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        claims.put(JwtClaimsConstant.USERNAME, user.getUsername());
        claims.put(JwtClaimsConstant.ROLE, user.getRole());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    /**
     * 注册
     *
     * @param userRegisterDTO 用户注册dto
     * @return 后端统一返回结果
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO){

        userService.save(userRegisterDTO);

        return Result.success();
    }
}
