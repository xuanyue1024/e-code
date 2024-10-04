package com.ecode.controller;

import com.ecode.dto.RunCodeDTO;
import com.ecode.properties.DockerProperties;
import com.ecode.result.Result;
import com.ecode.utils.RunCodeUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = "代码管理")
@RequestMapping("/code")
public class CodeController {
    @Autowired
    private DockerProperties dockerProperties;
    @PostMapping("/run")
    public Result<String> run(@RequestBody RunCodeDTO runCodeDTO){
        String result = RunCodeUtil.runCode(dockerProperties.getUrl(),dockerProperties.getTimeout(),runCodeDTO.getCode(),runCodeDTO.getInput() + "\n");
        return Result.success(result);
    }
}
