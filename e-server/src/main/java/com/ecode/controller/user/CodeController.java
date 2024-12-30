package com.ecode.controller.user;

import com.ecode.dto.DebugCodeDTO;
import com.ecode.dto.RunCodeDTO;
import com.ecode.result.Result;
import com.ecode.service.CodeService;
import com.ecode.vo.RunCodeVO;
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
    public Result<String> debug(@RequestBody DebugCodeDTO debugCodeDTO){
        String result = codeService.debugCode(debugCodeDTO);
        return Result.success(result);
    }

    @PostMapping("/run")
    @ApiOperation(value = "运行代码(判断题目代码)",notes = "data里面的文本列表是生成的统一差异格式unifiedDiff列表")
    public Result<RunCodeVO> run(@RequestBody RunCodeDTO runCodeDTO){
        RunCodeVO result = codeService.runCode(runCodeDTO);
        return Result.success(result);
    }
}
