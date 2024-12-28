package com.ecode.controller.user.teacher;

import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.dto.ProblemAddDTO;
import com.ecode.dto.ProblemUpdateDTO;
import com.ecode.dto.SetTagsDTO;
import com.ecode.result.Result;
import com.ecode.service.ProblemService;
import com.ecode.vo.PageVO;
import com.ecode.vo.ProblemPageVO;
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
    @ApiOperation(value = "增加题目",notes = "返回新增题目id,用于设置标签")
    public Result<Integer> add(@RequestBody ProblemAddDTO problemAddDTO){
        Integer id = problemService.add(problemAddDTO);
        return Result.success(id);
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
     * 更新问题信息的接口方法
     *
     * @param problemUpdateDTO 包含要更新的问题信息的数据传输对象
     * @return 返回操作结果，成功则返回成功结果
     */
    @PutMapping
    @ApiOperation("修改题目信息")
    public Result update(@RequestBody ProblemUpdateDTO problemUpdateDTO){
        problemService.updateProblem(problemUpdateDTO);
        return Result.success();
    }

    /**
     * 根据ID获取题目详细信息
     *
     * @param id 题目ID，用于唯一标识一个题目
     * @return 返回一个Result对象，其中包含ProblemVO类型的题目详细信息
     */
    @GetMapping("/{id}")
    @ApiOperation("获取题目详细信息")
    public Result<ProblemVO> get(@PathVariable Integer id){
        ProblemVO p = problemService.getProblem(id);
        return Result.success(p);
    }

    /**
     * 处理题目分页查询请求
     *
     * @param generalPageQueryDTO 分页查询参数封装对象，包含页码、页面大小等信息
     * @return 返回封装了分页查询结果的Result对象，其中包含ProblemVO类型的PageVO对象
     */
    @GetMapping("/page")
    @ApiOperation("题目分页查询")
    public Result<PageVO<ProblemPageVO>> page(GeneralPageQueryDTO generalPageQueryDTO){
        // 调用problemService的分页查询方法，获取分页查询结果
        PageVO<ProblemPageVO> pv = problemService.pageQuery(generalPageQueryDTO);
        return Result.success(pv);
    }

    @PutMapping("/tag")
    @ApiOperation(value = "为题目设置标签集合", notes = "用于为问题设置标签,如果原来有标签,会覆盖原有标签")
    public Result setTags(@RequestBody SetTagsDTO setTagsDTO){
        problemService.setTags(setTagsDTO);
        return Result.success();
    }

}
