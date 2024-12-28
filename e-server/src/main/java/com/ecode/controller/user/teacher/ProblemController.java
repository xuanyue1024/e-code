package com.ecode.controller.user.teacher;

import com.ecode.dto.ProblemAddDTO;
import com.ecode.result.Result;
import com.ecode.service.ProblemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "题目管理")
@RestController
@RequestMapping("/teacher/problem")
@Component("teacherProblemController")
public class ProblemController {
    @Autowired
    private ProblemService problemService;
    /**
     * 处理POST请求以添加新问题
     * 该方法使用ProblemAddDTO对象作为请求体，用于增加题目
     *
     * @param problemAddDTO 包含要添加问题信息的数据传输对象
     * @return 返回表示操作结果的Result对象，成功则返回成功信息
     */
    @PostMapping
    @ApiOperation("增加题目")
    public Result add(@RequestBody ProblemAddDTO problemAddDTO){
        problemService.add(problemAddDTO);
        return Result.success();
    }

    /**
     * 删除指定的题目集合
     * 此方法通过接收一个题目ID列表来批量删除题目
     * 主要用途是减少客户端请求次数，提高效率
     *
     * @param ids 需要删除的题目ID列表
     * @return 返回删除操作的结果
     */
    @DeleteMapping()
    @ApiOperation("批量删除题目")
    public Result deleteBatch(@ApiParam("题目id集合") @RequestParam List<Integer> ids){
        problemService.deleteProblemBatch(ids);
        return Result.success();
    }




}
