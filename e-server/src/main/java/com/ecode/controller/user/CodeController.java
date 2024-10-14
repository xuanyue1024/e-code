package com.ecode.controller.user;

import com.ecode.dto.DebugCodeDTO;
import com.ecode.result.Result;
import com.ecode.service.CodeService;
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
@Api(tags = "代码管理")
@RequestMapping("/user/code")
public class CodeController {

    @Autowired
    private CodeService codeService;
    @PostMapping("/debug")
    @ApiOperation("调试代码")
    public Result<String> run(@RequestBody DebugCodeDTO debugCodeDTO){
        String result = codeService.debugCode(debugCodeDTO);
        return Result.success(result);
    }
}
