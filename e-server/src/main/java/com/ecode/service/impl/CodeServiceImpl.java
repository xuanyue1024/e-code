package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.DebugCodeDTO;
import com.ecode.dto.RunCodeDTO;
import com.ecode.entity.ClassScore;
import com.ecode.entity.Problem;
import com.ecode.entity.StudentClass;
import com.ecode.enumeration.UserRole;
import com.ecode.exception.ClassException;
import com.ecode.mapper.ClassScoreMapper;
import com.ecode.mapper.ProblemMapper;
import com.ecode.mapper.StudentClassMapper;
import com.ecode.properties.DockerProperties;
import com.ecode.service.CodeService;
import com.ecode.utils.RunCodeUtil;
import com.ecode.utils.TextDiffUtil;
import com.ecode.vo.RunCodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CodeServiceImpl implements CodeService {

    @Autowired
    private DockerProperties dockerProperties;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private ClassScoreMapper classScoreMapper;

    @Override
    public String debugCode(DebugCodeDTO debugCodeDTO) {
        return RunCodeUtil.runCode(dockerProperties.getUrl(),
                dockerProperties.getTimeout(), debugCodeDTO.getType(),
                debugCodeDTO.getCode(), debugCodeDTO.getInput() + "\n");
    }

    @Override
    public RunCodeVO runCode(RunCodeDTO runCodeDTO) {

        Integer score = 0;
        Integer passCount = 0;
        //获取原始结果
        Problem problem = problemMapper.selectById(runCodeDTO.getProblemId());

        List<String> list = new ArrayList<>();
        list.add(runCodeUtil(
                runCodeDTO.getCode(), runCodeDTO.getType(),
                problem.getInputTest1(), problem.getOutputTest1())
        );
        list.add(runCodeUtil(
                runCodeDTO.getCode(), runCodeDTO.getType(),
                problem.getInputTest2(), problem.getOutputTest2())
        );
        list.add(runCodeUtil(
                runCodeDTO.getCode(), runCodeDTO.getType(),
                problem.getInputTest3(), problem.getOutputTest3())
        );
        list.add(runCodeUtil(
                runCodeDTO.getCode(), runCodeDTO.getType(),
                problem.getInputTest4(), problem.getOutputTest4())
        );

        for (String s : list) {
            if (s.isEmpty()){
                score++;
                passCount++;
            }
        }
        //如果是学生，新增分数
        log.info("当前运行代码用户角色:{}", BaseContext.getCurrentRole());
        if (BaseContext.getCurrentRole() == UserRole.STUDENT){
            //判断学生是否在改班级，顺便获取sc_id
            StudentClass sc = studentClassMapper.selectOne(new LambdaQueryWrapper<StudentClass>()
                    .eq(StudentClass::getClassId, runCodeDTO.getClassId())
                    .eq(StudentClass::getStudentId, BaseContext.getCurrentId())
            );
            if (sc == null){
                throw new ClassException(MessageConstant.CLASS_AND_STUDENT_NOT_FOUND);
            }
            //不管有没有，先删除，后增加，少走两年弯路
            int i =classScoreMapper.delete(new LambdaQueryWrapper<ClassScore>().eq(ClassScore::getScId, sc.getId()));
            log.info("删除个数:{}", i);
            classScoreMapper.insert(ClassScore.builder()
                        .classProblemId(runCodeDTO.getClassProblemId())
                        .scId(sc.getId())
                        .score(score)
                        .build());
        }
        return RunCodeVO.builder()
                .diff(list)
                .passCount(passCount)
                .score(score)
                .build();
    }

    /**
     * 运行代码工具方法
     *
     * @param code   代码：用户输入的运行代码
     * @param type   类型 代码类型
     * @param input  输入：答案输入数据
     * @param output 输出：答案输出数据
     * @return 字符串 统一差异格式结果
     */
    //todo 可优化
    private String runCodeUtil(String code, String type, String input,String output){
        //获得代码运行输出结果
        String runResult = RunCodeUtil.runCode(dockerProperties.getUrl(),
                dockerProperties.getTimeout(), type,
                code, input + "\n");

        return TextDiffUtil.generateUnifiedDiff(output,runResult);
    }
}
