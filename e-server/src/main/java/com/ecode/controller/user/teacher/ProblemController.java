package com.ecode.controller.user.teacher;

import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.dto.ProblemAddDTO;
import com.ecode.result.Result;
import com.ecode.service.ProblemService;
import com.ecode.vo.PageVO;
import com.ecode.vo.ProblemVO;
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

    /**
     * 处理题目分页查询请求
     *
     * @param generalPageQueryDTO 分页查询参数封装对象，包含页码、页面大小等信息
     * @return 返回封装了分页查询结果的Result对象，其中包含ProblemVO类型的PageVO对象
     */
    @GetMapping("/page")
    @ApiOperation("题目分页查询")
    public Result<PageVO<ProblemVO>> page(GeneralPageQueryDTO generalPageQueryDTO){
        // 调用problemService的分页查询方法，获取分页查询结果
        PageVO<ProblemVO> pv = problemService.pageQuery(generalPageQueryDTO);
        // 返回成功结果，包含分页查询结果
        return Result.success(pv);
    }


}
