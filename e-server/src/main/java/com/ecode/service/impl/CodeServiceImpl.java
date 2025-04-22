package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.DebugCodeDTO;
import com.ecode.dto.RunCodeDTO;
import com.ecode.entity.ClassScore;
import com.ecode.entity.Problem;
import com.ecode.entity.StudentClass;
import com.ecode.entity.po.CodeSubmission;
import com.ecode.enumeration.UserRole;
import com.ecode.exception.BaseException;
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

import java.time.LocalDateTime;
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

        //得分
        Integer score = 0;
        //通过例题数
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
            //判断学生是否在该班级，顺便获取sc_id
            StudentClass sc = studentClassMapper.selectOne(new LambdaQueryWrapper<StudentClass>()
                    .eq(StudentClass::getClassId, runCodeDTO.getClassId())
                    .eq(StudentClass::getStudentId, BaseContext.getCurrentId())
            );
            if (sc == null){
                throw new ClassException(MessageConstant.CLASS_AND_STUDENT_NOT_FOUND);
            }

            // todo 性能可优化
            // 1.先查询现有历史提交记录
            ClassScore existingScore = classScoreMapper.selectOne(new LambdaQueryWrapper<ClassScore>()
                    .eq(ClassScore::getScId, sc.getId())
                    .eq(ClassScore::getClassProblemId, runCodeDTO.getClassProblemId()));

            // 2.添加新的代码提交记录
            CodeSubmission submission = CodeSubmission.builder()
                    .codeText(runCodeDTO.getCode())
                    .submitTime(LocalDateTime.now())
                    .languageType(runCodeDTO.getType())
                    .passCount(passCount)
                    .build();
            List<CodeSubmission> codeSubmissionList = new ArrayList<>();
            if (existingScore != null && existingScore.getCodeSubmission() != null) {
                codeSubmissionList = new ArrayList<>(existingScore.getCodeSubmission()); // 将查询的历史记录列表添加到新的列表中
            }
            codeSubmissionList.add(submission); // 添加新的提交记录

            // 3.更新代码历史提交记录
            //https://github.com/baomidou/mybatis-plus/issues/6519
            int i = classScoreMapper.update(null, new LambdaUpdateWrapper<ClassScore>()
                    .set(ClassScore::getScore, score)
                    .set(ClassScore::getCodeSubmission, codeSubmissionList, "typeHandler=com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler") // 更新代码历史提交记录
                    .setSql("submit_number = submit_number + 1")
                    .setSql(passCount == 4, "pass_number = pass_number + 1")
                    .eq(ClassScore::getScId, sc.getId())
                    .eq(ClassScore::getClassProblemId, runCodeDTO.getClassProblemId())
            );

            if (i <= 0){

                int passInt = 0;
                if (passCount == 4) passInt = 1;

                //如果没有过此成绩，就增加
                classScoreMapper.insert(ClassScore.builder()
                        .classProblemId(runCodeDTO.getClassProblemId())
                        .scId(sc.getId())
                        .score(score)
                        .passNumber(passInt)
                        .submitNumber(1)
                        .codeSubmission(codeSubmissionList)  // 如果是首次提交直接插入
                        .build());
            }
        }
        return RunCodeVO.builder()
                .diff(list)
                .passCount(passCount)
                .score(score)
                .build();
    }

    @Override
    public List<CodeSubmission> getCodeSubmissions(Integer studentId, Integer classId, Integer classProblemId) {

        // 根据当前学生id获取班级学生id
        StudentClass studentClass = studentClassMapper.selectOne(new LambdaQueryWrapper<StudentClass>()
                .eq(StudentClass::getStudentId, studentId)
                .eq(StudentClass::getClassId, classId)
        );

        Integer scId = studentClass.getId();

        // 根据scId和classProblemId查询学生的代码提交记录
        ClassScore classScore = classScoreMapper.selectOne(new LambdaQueryWrapper<ClassScore>()
                .eq(ClassScore::getScId, scId)
                .eq(ClassScore::getClassProblemId, classProblemId)
        );
        if (classScore != null) {
            // 返回代码提交记录
            return classScore.getCodeSubmission();
        }
        return List.of();
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

        return TextDiffUtil.generateUnifiedDiff(runResult,output);
    }
}
