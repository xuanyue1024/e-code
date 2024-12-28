package com.ecode.controller.user.student;

import com.ecode.context.BaseContext;
import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.result.Result;
import com.ecode.service.ClassService;
import com.ecode.vo.ClassVO;
import com.ecode.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "班级管理")
@RestController
@RequestMapping("/student/class")
@Component("studentClassController")
public class ClassController {
    @Autowired
    private ClassService classService;

    @GetMapping
    @ApiOperation("加入班级")
    public Result join(@ApiParam("邀请码") String invitationCode){
        classService.joinClass(BaseContext.getCurrentId(), invitationCode);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("班级分页查询")
    public Result<PageVO<ClassVO>> page(GeneralPageQueryDTO classPageQueryDTO){
        PageVO<ClassVO> pv = classService.pageQuery(null, BaseContext.getCurrentId(), classPageQueryDTO);
        return Result.success(pv);
    }

    @DeleteMapping
    @ApiOperation("批量退出班级")
    public Result exit(@ApiParam("班级id集合") @RequestParam List<Integer> ids){
        classService.exitBatch(BaseContext.getCurrentId(), ids);
        return Result.success();
    }

}
