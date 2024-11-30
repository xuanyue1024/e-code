package com.ecode.controller.user.teacher;

import com.ecode.dto.ClassPageQueryDTO;
import com.ecode.result.Result;
import com.ecode.service.ClassService;
import com.ecode.vo.ClassVO;
import com.ecode.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "班级管理-老师")
@RestController
@RequestMapping("/teacher/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping("/add")
    @ApiOperation("增加班级")
    public Result addClass(String name){
        classService.addClass(name);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("班级分页查询")
    public Result<PageVO<ClassVO>> page(ClassPageQueryDTO classPageQueryDTO){
        PageVO<ClassVO> pv = classService.pageQuery(classPageQueryDTO);
        return Result.success(pv);
    }


}
