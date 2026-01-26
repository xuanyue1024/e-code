package com.ecode.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.ecode.annotation.Captcha;
import com.ecode.constant.JwtClaimsConstant;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.ScanDTO;
import com.ecode.dto.UserLoginDTO;
import com.ecode.dto.UserRegisterDTO;
import com.ecode.dto.UserUpdateDTO;
import com.ecode.entity.OauthIdentities;
import com.ecode.entity.User;
import com.ecode.enumeration.Redis;
import com.ecode.enumeration.ResponseCode;
import com.ecode.enumeration.ScanStatus;
import com.ecode.enumeration.UserStatus;
import com.ecode.exception.BaseException;
import com.ecode.exception.LoginException;
import com.ecode.json.ScanData;
import com.ecode.result.Result;
import com.ecode.service.OauthIdentitiesService;
import com.ecode.service.UserService;
import com.ecode.service.login.LoginStrategy;
import com.ecode.service.login.LoginStrategyFactory;
import com.ecode.service.login.OAuth2Strategy;
import com.ecode.service.login.OAuth2StrategyFactory;
import com.ecode.vo.*;
import com.ecode.websocket.ScanWebSocket;
import com.yubico.webauthn.exception.AssertionFailedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 用户管理
 *
 * @author 竹林听雨
 * @date 2024/09/22
 */
@RestController
@Slf4j
@Tag(name = "用户管理")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LoginStrategyFactory loginStrategyFactory;

    @Autowired
    private OAuth2StrategyFactory oAuth2StrategyFactory;

    @Autowired
    private UserService userService;

    @Autowired
    private OauthIdentitiesService oauthIdentitiesService;
    
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 登录
     *
     * @param userLoginDTO 用户登录dto
     * @return 结果<user login vo>
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) throws IOException, AssertionFailedException {
        LoginStrategy strategy = loginStrategyFactory.getStrategy(userLoginDTO.getLoginType());
        User user = strategy.login(userLoginDTO);

        UserLoginVO userLoginVO = getLoginData(user);

        return Result.success(userLoginVO);
    }

    /**
     * 注册
     *
     * @param userRegisterDTO 用户注册dto
     * @return 后端统一返回结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    @Captcha
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO){

        userService.save(userRegisterDTO);

        return Result.success();
    }

    @GetMapping("/callback")
    @Operation(summary = "OAuth2 回调接口")
    public Result callback(@RequestParam String code, @RequestParam String state) throws IOException, AssertionFailedException {
        //这里先获取一遍,用来查找登录类型,后面还会再获取
        String oAuth2Type  = String.valueOf(redisTemplate.opsForValue().get(Redis.OAUTH2_STATE + state));

        if (oAuth2Type == null) {
            throw new BaseException(MessageConstant.CALLBACK_FAIL);
        }

        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                .loginType(oAuth2Type)
                .state(state)
                .authCode(code).build();
        OAuth2Strategy strategy = oAuth2StrategyFactory.getStrategy(userLoginDTO.getLoginType());
        UserOauthVO uoVO = strategy.callback(userLoginDTO);//里面没有密码或者id信息
        OauthIdentities oauthIdentities = uoVO.getOauthIdentities();

        // 查找用户
        OauthIdentities oi = oauthIdentitiesService.getByProviderId(oauthIdentities.getProvider(),
                oauthIdentities.getProviderId());
        if (oi == null) {
            if (oauthIdentities.getProviderEmail() == null){
                log.error(MessageConstant.CALLBACK_FAIL + ",邮箱不能为空");
                throw new BaseException(MessageConstant.CALLBACK_FAIL);
            }

            //根据邮箱查找用户
            User userByEmail = userService.getUserByEmail(oauthIdentities.getProviderEmail());

            //存在用户,直接关联权限表登录
            if (userByEmail != null) {
                oauthIdentities.setUserId(userByEmail.getId());
                oauthIdentitiesService.insertOauthIdentities(oauthIdentities);
                return Result.success(getLoginData(userByEmail));
            }

            //不存在用户,启动注册流程
            OAuthRegisterVO ov = new OAuthRegisterVO();
            ov.setUserOauthVO(uoVO);
            ov.setRegisterCode(UUID.randomUUID().toString());
            Redis oauth2RegisterCode = Redis.OAUTH2_REGISTER_CODE;
            redisTemplate.opsForValue().set(
                    oauth2RegisterCode + ov.getRegisterCode(),
                    ov, oauth2RegisterCode.getTimeout(),
                    oauth2RegisterCode.getTimeUnit()
            );
            return Result.success(ResponseCode.PLEASE_REGISTER.getValue(),
                    ov, ResponseCode.PLEASE_REGISTER.getDesc());

        }else {
            //代表原来已经注册过三方,直接登录
            return Result.success(getLoginData(userService.getById(oi.getUserId())));
        }
    }

    @GetMapping("/oauth2")
    @Operation(summary = "OAuth2 获取授权URL接口")
    public Result oauth(@RequestParam String oauth2Type){
        OAuth2Strategy strategy = oAuth2StrategyFactory.getStrategy(oauth2Type);
        String url = strategy.prepare();
        return Result.success(Map.of("oauth2Url", url));
    }

    /**
     * 扫码登录-二维码数据生成
     * @return 二维码数据
     */
    @GetMapping("/scan/generate")
    @Operation(summary = "扫码登录-1.二维码数据生成")
    public Result<ScanVO> scanGenerate(){
        ScanVO sv = userService.scanGenerate();
        return Result.success(sv);
    }

    /**
     * 扫码登录-已扫码待确认
     * @param sceneId
     * @return
     */
    @PostMapping("/scan/scanned")
    @Operation(summary = "扫码登录-2.已扫码待确认")
    public Result scanned(@RequestParam String sceneId) {
        String key = Redis.LOGIN_SCAN + sceneId;
        ScanData scanData = (ScanData) redisTemplate.opsForValue().get(key);

        if (scanData == null) {
            return Result.error(MessageConstant.SCAN_EXPIRED);
        }

        if (scanData.getStatus() != ScanStatus.WAITING) {
            return Result.error(MessageConstant.SCAN_INVALID);
        }

        // 更新为“已扫码”，等待确认
        Integer userId = BaseContext.getCurrentId();
        scanData.setStatus(ScanStatus.SCANNED);
        scanData.setUserId(userId);
        redisTemplate.opsForValue().set(key, scanData, Duration.ofMinutes(2)); // 再给2分钟确认

        // 通知Web端：“已被扫码，请等待确认”
        ScanWebSocket.send(sceneId, WsScanVO.builder()
                .status(ScanStatus.SCANNED)
                .metaData(userService.getProfilePictureById(userId))
                .msg(MessageConstant.SCAN_SCANNED)
                .build()
        );

        return Result.success();
    }

    /**
     * 扫码登录-确认登录
     * @param scanDTO
     * @return
     */
    @PostMapping("/scan/confirm")
    @Operation(summary = "扫码登录-3.确认或取消登录")
    public Result scanConfirm(@RequestBody ScanDTO scanDTO) {
        Integer userId = BaseContext.getCurrentId();
        String key = Redis.LOGIN_SCAN + scanDTO.getSceneId();
        ScanData scanData = (ScanData) redisTemplate.opsForValue().get(key);

        if (scanData == null) {
            throw new BaseException(MessageConstant.SCAN_EXPIRED);
        }
        //状态是否符合
        if (scanData.getStatus() != ScanStatus.SCANNED) {
            throw new BaseException(MessageConstant.SCAN_STATUS_ERROR);
        }
        //用户是否符合
        if (!Objects.equals(scanData.getUserId(), userId)) {
            throw new BaseException(MessageConstant.DATA_ACCESS_DENIED);
        }

        if (scanDTO.getIsConfirm()){
            //确认登录
            //设置为已经确认
            scanData.setStatus(ScanStatus.CONFIRMED);
            // 通知ws
            ScanWebSocket.send(scanDTO.getSceneId(), WsScanVO.builder()
                    .status(ScanStatus.CONFIRMED)
                    .userId(userId)
                    .msg(MessageConstant.SCAN_CONFIRMED)
                    .build()
            );
            //存入数据
            redisTemplate.opsForValue().set(key, scanData, Duration.ofMinutes(1));
        }else {
            //取消登录
            redisTemplate.delete(key);

            ScanWebSocket.send(scanDTO.getSceneId(), WsScanVO.builder()
                    .status(ScanStatus.CANCELLED)
                    .userId(userId)
                    .msg(MessageConstant.SCAN_CANCELLED)
                    .build()
            );
        }

        return Result.success();
    }

    @PutMapping()
    @Operation(summary = "修改用户信息")
    public Result update(@RequestBody UserUpdateDTO userUpdateDTO){
        userService.updateUser(userUpdateDTO, BaseContext.getCurrentId());
        return Result.success();
    }

    @GetMapping
    @Operation(summary = "获取用户详细信息")
    public Result<User> get(){
        User u = userService.getUserInfo(BaseContext.getCurrentId());
        return Result.success(u);
    }

    /**
     * 根据登录User类获取登录数据
     * @param user 登录成功查询用户数据
     * @return
     */
    private UserLoginVO getLoginData(User user) {
        //判断账号是否被锁定
        if (user.getStatus() == UserStatus.DISABLE){
            throw new LoginException(MessageConstant.ACCOUNT_LOCKED);
        }

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        claims.put(JwtClaimsConstant.USERNAME, user.getUsername());
        claims.put(JwtClaimsConstant.ROLE, user.getRole().name());

        StpUtil.login(user.getId(), new SaLoginParameter().setExtraData(claims));

        return UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .token(StpUtil.getTokenInfo().tokenValue)
                .build();
    }
}
