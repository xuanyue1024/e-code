package com.ecode.controller.user.teacher;

import com.ecode.result.Result;
import com.ecode.service.ClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "班级管理-老师")
@RestController
@RequestMapping("/teacher")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping("/addClass")
    @ApiOperation("增加班级")
    public Result addClass(@RequestBody String name){
        classService.addClass(name);
        return Result.success();
    }
}
