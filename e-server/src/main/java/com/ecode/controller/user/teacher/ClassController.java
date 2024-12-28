package com.ecode.controller.user.teacher;

import com.ecode.context.BaseContext;
import com.ecode.dto.ClassProblemDTO;
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
    public Result<PageVO<ClassVO>> page(GeneralPageQueryDTO generalPageQueryDTO){
        PageVO<ClassVO> pv = classService.pageQuery(BaseContext.getCurrentId(), null, generalPageQueryDTO);
        return Result.success(pv);
    }

    @DeleteMapping
    @ApiOperation("批量删除班级")
    public Result delete(@ApiParam("班级id集合") @RequestParam List<Integer> ids){
        classService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改班级信息")
    public Result update(@ApiParam("班级id") Integer id,
                         @ApiParam("班级名称") String name){
        classService.updateNameByIdAndTeacherId(id, BaseContext.getCurrentId(), name);
        return Result.success();
    }

    /**
     * 为班级增加题目
     *
     * @param classProblemDTO 类添加问题
     * @return 后端统一返回结果
     */
    @PostMapping("/problem")
    @ApiOperation("班级增加题目")
    public Result addProblemBatch(@RequestBody ClassProblemDTO classProblemDTO){
        classService.addProblemBatch(classProblemDTO);
        return Result.success();
    }

    /**
     * 批量删除班级题目
     *
     * @param classProblemDTO 类问题d
     * @return 后端统一返回结果
     */
    @DeleteMapping("/problem")
    @ApiOperation("批量移除班级题目")
    public Result deleteProblemBatch(@RequestBody ClassProblemDTO classProblemDTO){
        classService.deleteProblemBatch(classProblemDTO);
        return Result.success();
    }
}
