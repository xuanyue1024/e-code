package com.ecode.controller.user;

import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.PasskeyRegistrationDTO;
import com.ecode.exception.LoginException;
import com.ecode.result.Result;
import com.ecode.service.PasskeyAuthorizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 身份验证控制器
 *
 * @author 竹林听雨
 * @date 2025/04/04
 */
@RestController
@Slf4j
@Tag(name = "身份验证管理")
@RequestMapping("/auth/passkey")
public class AuthPasskeyController {

    @Autowired
    private PasskeyAuthorizationService passkeyAuthorizationService;

    @GetMapping(path = "/registration")
    @Operation(summary = "获取注册凭证信息")
    public Result getPasskeyRegistrationOptions() throws JsonProcessingException {
        String option = passkeyAuthorizationService.startPasskeyRegistration(BaseContext.getCurrentId());
        JsonNode jsonNode = new ObjectMapper().readTree(option);//避免json字符串被转义
        return Result.success(jsonNode);
    }

    @PostMapping("/registration")
    @Operation(summary = "注册凭证验证")
    public Result verifyPasskeyRegistration(@RequestBody PasskeyRegistrationDTO passkeyRegistrationDTO) {
        try {
            passkeyAuthorizationService.finishPasskeyRegistration(
                    BaseContext.getCurrentId(),
                    passkeyRegistrationDTO.getName(),
                    passkeyRegistrationDTO.getCredential());
        }catch (Exception e){
            log.error("{}: {}",MessageConstant.WEBAUTHN_ORIGIN_ERROR, e.getMessage());
            throw new LoginException(MessageConstant.WEBAUTHN_ORIGIN_ERROR);
        }

        return Result.success();
    }

    @GetMapping("/assertion")
    @Operation(summary = "登录凭证信息")
    public Result getPasskeyAssertionOptions(String identifier) throws JsonProcessingException {
        String option = passkeyAuthorizationService.startPasskeyAssertion(identifier);
        JsonNode jsonNode = new ObjectMapper().readTree(option);//避免json字符串被转义
        return Result.success(jsonNode);
    }

    @GetMapping()
    @Operation(summary = "获取用户凭证列表")
    public Result getPasskeyList() {
        return Result.success(passkeyAuthorizationService.getPasskeyList(BaseContext.getCurrentId()));
    }

    @DeleteMapping()
    @Operation(summary = "移除密钥")
    public Result deletePasskey(@Parameter(name = "班级id集合") @RequestParam String id) {
        passkeyAuthorizationService.deletePasskey(BaseContext.getCurrentId(), id);
        return Result.success();
    }
}
