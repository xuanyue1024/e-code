package com.ecode.controller.user.student;

import com.ecode.result.Result;
import com.ecode.service.ProblemService;
import com.ecode.vo.ProblemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "题目管理")
@RestController
@RequestMapping("/student/problem")
@Component("studentProblemController")
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    /**
     * 根据ID获取题目详细信息
     *
     * @param id 题目ID，用于唯一标识一个题目
     * @return 返回一个Result对象，其中包含ProblemVO类型的题目详细信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取题目详细信息")
    public Result<ProblemVO> get(@PathVariable Integer id){
        ProblemVO p = problemService.getProblem(id, ProblemVO.class);
        return Result.success(p);
    }

    /**
     * 处理题目分页查询请求
     *
     * @param generalPageQueryDTO 分页查询参数封装对象，包含页码、页面大小等信息
     * @return 返回封装了分页查询结果的Result对象，其中包含ProblemVO类型的PageVO对象
     */
    /*@GetMapping("/page")
    @Operation(summary = "题目分页查询")
    public Result<PageVO<ProblemPageVO>> page(GeneralPageQueryDTO generalPageQueryDTO){
        // 调用problemService的分页查询方法，获取分页查询结果
        PageVO<ProblemPageVO> pv = problemService.pageQuery(generalPageQueryDTO);
        return Result.success(pv);
    }*/


}
