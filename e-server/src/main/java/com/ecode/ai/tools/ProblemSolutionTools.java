package com.ecode.ai.tools;

import com.ecode.entity.Problem;
import com.ecode.mapper.ProblemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ai调用此方法获得解决方法
 *
 * @author 竹林听雨
 * @date 2025/04/28
 */
@Component
@Slf4j
public class ProblemSolutionTools {
    @Autowired
    private ProblemMapper problemMapper;
    @Tool(description = "获取题目的解答过程")
    public String getAnswer(@ToolParam(description = "题目id") Integer problemId) {
        Problem problem = problemMapper.selectById(problemId);
        if (problem != null && problem.getAnswer() != null){
            return problem.getAnswer();
        }
        return "未发现解答过程";
    }
}
