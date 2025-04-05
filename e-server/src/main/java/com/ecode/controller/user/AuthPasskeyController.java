package com.ecode.controller.user;

import com.ecode.context.BaseContext;
import com.ecode.dto.PasskeyRegistrationDTO;
import com.ecode.result.Result;
import com.ecode.service.PasskeyAuthorizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 身份验证控制器
 *
 * @author 竹林听雨
 * @date 2025/04/04
 */
@RestController
@Slf4j
@Api(tags = "身份验证管理")
@RequestMapping("/auth/passkey")
public class AuthPasskeyController {

    @Autowired
    private PasskeyAuthorizationService passkeyAuthorizationService;

    @GetMapping(path = "/registration")
    @ApiOperation("获取注册凭证信息")
    public Result getPasskeyRegistrationOptions() throws JsonProcessingException {
        String option = passkeyAuthorizationService.startPasskeyRegistration(BaseContext.getCurrentId());
        JsonNode jsonNode = new ObjectMapper().readTree(option);//避免json字符串被转义
        return Result.success(jsonNode);
    }

    @PostMapping("/registration")
    @ApiOperation("注册凭证验证")
    public Result verifyPasskeyRegistration(@RequestBody PasskeyRegistrationDTO passkeyRegistrationDTO) throws RegistrationFailedException, IOException {
        passkeyAuthorizationService.finishPasskeyRegistration(
                BaseContext.getCurrentId(),
                passkeyRegistrationDTO.getName(),
                passkeyRegistrationDTO.getCredential());
        return Result.success();
    }

    @GetMapping("/assertion")
    @ApiOperation("登录凭证信息")
    public Result getPasskeyAssertionOptions(String identifier) throws JsonProcessingException {
        String option = passkeyAuthorizationService.startPasskeyAssertion(identifier);
        JsonNode jsonNode = new ObjectMapper().readTree(option);//避免json字符串被转义
        return Result.success(jsonNode);
    }

    @GetMapping()
    @ApiOperation("获取用户凭证列表")
    public Result getPasskeyList() {
        return Result.success(passkeyAuthorizationService.getPasskeyList(BaseContext.getCurrentId()));
    }

    @DeleteMapping()
    @ApiOperation("移除密钥")
    public Result deletePasskey(@ApiParam("班级id集合") @RequestParam String id) {
        passkeyAuthorizationService.deletePasskey(BaseContext.getCurrentId(), id);
        return Result.success();
    }
}
