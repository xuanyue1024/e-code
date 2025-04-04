package com.ecode.controller.user;

import com.ecode.constant.JwtClaimsConstant;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.UserLoginDTO;
import com.ecode.dto.UserRegisterDTO;
import com.ecode.dto.UserUpdateDTO;
import com.ecode.entity.User;
import com.ecode.enumeration.UserStatus;
import com.ecode.exception.LoginException;
import com.ecode.properties.JwtProperties;
import com.ecode.result.Result;
import com.ecode.service.PasskeyAuthorizationService;
import com.ecode.service.UserService;
import com.ecode.service.login.LoginStrategy;
import com.ecode.service.login.LoginStrategyFactory;
import com.ecode.utils.JwtUtil;
import com.ecode.vo.UserLoginVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @Autowired
    private PasskeyAuthorizationService passkeyAuthorizationService;


    /**
     * 登录
     *
     * @param userLoginDTO 用户登录dto
     * @return 结果<user login vo>
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) throws IOException, AssertionFailedException {
        LoginStrategy strategy = loginStrategyFactory.getStrategy(userLoginDTO.getLoginType());
        User user = strategy.login(userLoginDTO);

        //判断账号是否被锁定
        if (user.getStatus() == UserStatus.DISABLE){
            throw new LoginException(MessageConstant.ACCOUNT_LOCKED);
        }

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

    @PutMapping()
    @ApiOperation("修改用户信息")
    public Result update(@RequestBody UserUpdateDTO userUpdateDTO){
        userService.updateUser(userUpdateDTO, BaseContext.getCurrentId());
        return Result.success();
    }

    @GetMapping
    @ApiOperation("获取用户详细信息")
    public Result<User> get(){
        User u = userService.getUserInfo(BaseContext.getCurrentId());
        return Result.success(u);
    }

    @GetMapping(path = "/auth/registration")
    @ApiOperation("获取注册凭证信息")
    public Result getPasskeyRegistrationOptions() throws JsonProcessingException {
        String option = passkeyAuthorizationService.startPasskeyRegistration(BaseContext.getCurrentId());
        JsonNode jsonNode = new ObjectMapper().readTree(option);//避免json字符串被转义
        return Result.success(jsonNode);
    }

    @PostMapping("/auth/registration")
    @ApiOperation("注册凭证验证")
    public Result verifyPasskeyRegistration(String credential) throws RegistrationFailedException, IOException {
        passkeyAuthorizationService.finishPasskeyRegistration(BaseContext.getCurrentId(), credential);
        return Result.success();
    }

    @GetMapping("/auth/assertion")
    @ApiOperation("登录凭证信息")
    public Result getPasskeyAssertionOptions(String identifier) throws JsonProcessingException {
        String option = passkeyAuthorizationService.startPasskeyAssertion(identifier);
        JsonNode jsonNode = new ObjectMapper().readTree(option);//避免json字符串被转义
        return Result.success(jsonNode);
    }

}
