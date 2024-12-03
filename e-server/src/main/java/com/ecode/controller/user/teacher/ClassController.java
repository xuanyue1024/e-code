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
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "班级管理")
@RestController
@RequestMapping("/teacher/class")
@Component("teacherClassController")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping
    @ApiOperation("增加班级")
    public Result add(String name){
        classService.addClass(name);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("班级分页查询")
    public Result<PageVO<ClassVO>> page(ClassPageQueryDTO classPageQueryDTO){
        PageVO<ClassVO> pv = classService.pageQuery(classPageQueryDTO);
        return Result.success(pv);
    }

    @DeleteMapping
    @ApiOperation("批量删除班级")
    public Result delete(@RequestParam List<Integer> ids){
        classService.deleteBatch(ids);
        return Result.success();
    }
}
