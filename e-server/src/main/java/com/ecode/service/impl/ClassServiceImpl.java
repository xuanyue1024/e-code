package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.ClassProblemDTO;
import com.ecode.dto.ClassProblemPageQueryDTO;
import com.ecode.dto.ClassStudentDTO;
import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.entity.Class;
import com.ecode.entity.*;
import com.ecode.enumeration.UserRole;
import com.ecode.exception.ClassException;
import com.ecode.mapper.*;
import com.ecode.service.ClassService;
import com.ecode.utils.InvitationCodeUtil;
import com.ecode.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 班级service实现类
 *
 * @author 竹林听雨
 * @date 2024/11/24
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private ClassProblemMapper classProblemMapper;

    @Autowired
    private ProblemTagMapper problemTagMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClassScoreMapper classScoreMapper;

    @Override
    public void addClass(String name) {
        Class c = Class.builder()
                .name(name)
                .teacherId(BaseContext.getCurrentId())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        classMapper.insert(c);
        //生成邀请码
        c.setInvitationCode(InvitationCodeUtil.inviCodeGenerator(c.getId()));
        //修改数据。更新邀请码
        classMapper.updateById(c);
    }

    @Override
    public PageVO<ClassVO> pageQuery(Integer teacherId, Integer studentId, GeneralPageQueryDTO generalPageQueryDTO) {

        PageHelper.startPage(Math.toIntExact(generalPageQueryDTO.getPageNo()), Math.toIntExact(generalPageQueryDTO.getPageSize()));
        //1.3 查询条件
        List<ClassVO> classVOList = classMapper.pageQueryByName(
                generalPageQueryDTO.getName(),
                teacherId, studentId,
                generalPageQueryDTO.getSortBy(),
                generalPageQueryDTO.getIsAsc());

        Page<ClassVO> p = (Page<ClassVO>) classVOList;

        return new PageVO<ClassVO>(p.getTotal(), (long) p.getPages(), p.getResult());
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        QueryWrapper<Class> wrapper = new QueryWrapper<>();
        //判断教师id是否符合且ids符合
        wrapper.lambda()
                .in(Class::getId, ids)
                .eq(Class::getTeacherId, BaseContext.getCurrentId());
        classMapper.delete(wrapper);
    }

    @Override
    public void updateNameByIdAndTeacherId(Integer id, Integer teacherId, String name) {
        UpdateWrapper<Class> wrapper = new UpdateWrapper<>();
        wrapper.lambda()
                .set(Class::getName, name)
                .set(Class::getUpdateTime, LocalDateTime.now())
                .eq( Class::getId, id)
                .eq(Class::getTeacherId, teacherId);
        int i = classMapper.update(null, wrapper);
        if (i <= 0){
            throw new ClassException(MessageConstant.UPDATE_FAILED);
        }
    }

    @Override
    @Transactional
    public void joinClass(Integer studentId, String invitationCode) {
        QueryWrapper<Class> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Class::getInvitationCode, invitationCode);
        Class c = classMapper.selectOne(wrapper);
        if (c == null){
            throw new ClassException(MessageConstant.INVITATIONCODE_NOT_FOUND);
        }
        //班级存在，插入班级学生信息
        StudentClass sc = StudentClass.builder()
                .classId(c.getId())
                .studentId(studentId)
                .joinTime(LocalDateTime.now())
                .build();
        studentClassMapper.insert(sc);

        //更新班级人数
        UpdateWrapper<Class> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .setSql("join_number = join_number + 1")
                .eq(Class::getId, c.getId());
        this.update(updateWrapper);
    }

    @Override
    @Transactional
    public void exitBatch(Integer studentId, List<Integer> classIds) {
        //删除学生id与班级id集合符合的信息
        QueryWrapper<StudentClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(StudentClass::getClassId, classIds)
                .eq(StudentClass::getStudentId, studentId);
        int i = studentClassMapper.delete(queryWrapper);
        if (i != classIds.size()){
            throw new ClassException(MessageConstant.EXIT_FAILURE_NOT_EXIST_CLASS);
        }
        //修改班级人数
        UpdateWrapper<Class> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .setSql("join_number = join_number - 1")
                .in(Class::getId, classIds);
        classMapper.update(null, updateWrapper);
    }

    @Override
    public void addProblemBatch(ClassProblemDTO classProblemDTO) {
        verifyClassStudent(classProblemDTO.getClassId());

        List<Integer> problemIds = classProblemDTO.getProblemIds();
        problemIds.forEach(pi -> {
            ClassProblem cp = ClassProblem.builder()
                    .classId(classProblemDTO.getClassId())
                    .problemId(pi)
                    .build();
            classProblemMapper.insert(cp);
        });
    }

    @Transactional
    @Override
    public void deleteProblemBatch(ClassProblemDTO classProblemDTO) {
        verifyClassStudent(classProblemDTO.getClassId());

        QueryWrapper<ClassProblem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(ClassProblem::getProblemId, classProblemDTO.getProblemIds())
                .eq(ClassProblem::getClassId, classProblemDTO.getClassId());
        classProblemMapper.delete(queryWrapper);

    }

    @Override
    public PageVO<ProblemPageVO> problemPage(ClassProblemPageQueryDTO classProblemPageQueryDTO) {
        verifyClassStudent(classProblemPageQueryDTO.getClassId());
        // 设置分页参数
        PageHelper.startPage(
                Math.toIntExact(classProblemPageQueryDTO.getPageNo()),
                Math.toIntExact(classProblemPageQueryDTO.getPageSize()));

        // 执行分页查询
        //todo 查询排序方式可优化
        Page<ProblemPageVO> page =(Page<ProblemPageVO>) classProblemMapper.getClassProblem(
                classProblemPageQueryDTO.getClassId(),
                classProblemPageQueryDTO.getName()
        );

        page.getResult().forEach(p -> {
            //查询并设置单个题目的标签集合
            //todo 性能可优化
            List<ProblemTag> problemTags = problemTagMapper.selectList(new LambdaQueryWrapper<ProblemTag>().eq(ProblemTag::getProblemId, p.getId()));
            p.setTagIds(problemTags.stream().map(ProblemTag::getTagId).collect(Collectors.toList()));
        });

        return new PageVO<>(page.getTotal(), (long) page.getPages(), page.getResult());
    }

    @Override
    public PageVO<UserVO> studentPage(ClassStudentDTO classStudentDTO) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<StudentClass> page = classStudentDTO.toMpPage("join_time",false);

        //判断是否有name
        //todo 可优化
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<StudentClass> studentClassPage = studentClassMapper.selectPage(page, new QueryWrapper<StudentClass>().lambda().eq(StudentClass::getClassId, classStudentDTO.getClassId()));

        List<StudentClass> records = studentClassPage.getRecords();

        //没数据，返回空结果
        if (records == null || records.isEmpty()){
            return new PageVO<>(studentClassPage.getTotal(), studentClassPage.getPages(), Collections.emptyList());
        }
        //有数据，转换
        List<UserVO> collect = records.stream().map(s -> {
            UserVO uv = new UserVO();
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getId, s.getStudentId())
                    .like(User::getName, classStudentDTO.getName())
            );
            if (user == null) {
                return null;
            }
            BeanUtils.copyProperties(user, uv);
            List<ClassScore> classScores = classScoreMapper.selectList(new LambdaQueryWrapper<ClassScore>().eq(ClassScore::getScId, s.getId()));

            int sumScore = classScores.stream().mapToInt(ClassScore::getScore).sum();
            uv.setTotalScore(sumScore);
            return uv;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return new PageVO<>(studentClassPage.getTotal(), studentClassPage.getPages(), collect);
    }

    @Override
    public ProblemStuInfoVO problemStuInfo(Integer stuId, Integer classProblemId) {
        ClassScore classScore = classScoreMapper.problemStuInfo(stuId,classProblemId);
        if (classScore == null){
            return ProblemStuInfoVO.builder()
                    .score(0)
                    .passNumber(0)
                    .submitNumber(0)
                    .build();
        }
        ProblemStuInfoVO psv = new ProblemStuInfoVO();
        BeanUtils.copyProperties(classScore, psv);
        return psv;
    }



    @Override
    public List<ClassProblemSubmissionsVO> submissionsInfo(Integer classId) {
        return classMapper.selectClassProblemSubmissionsByClassId(classId);
    }

    @Override
    public List<ClassStudentRankVO> getClassStudentRank(Integer classId) {
        verifyClassStudent(classId);
        return classMapper.selectClassStudentRank(classId);
    }

    @Override
    public List<ClassDifficultyDistributionVO> getClassStudentDifficulty(Integer classId) {
        return classMapper.selectClassStudentDifficulty(classId);
    }

    @Override
    public List<ClassProblemPassRateVO> getClassProblemPassRate(Integer classId) {
        return classMapper.selectClassProblemPassRate(classId);
    }

    @Override
    public List<ClassProblemDifficultyNumVO> getClassProblemDifficultyNum(Integer classId) {
        return classMapper.getDifficultyNum(classId);
    }

    @Override
    public List<ClassProblemTagNumVO> getClassProblemTagNum(Integer classId) {
        return classMapper.getClassProblemTagNum(classId);
    }

    /**
     * 验证教师是否在指定班级授课
     * 验证学生是否在指定班级
     *
     * @param classId 班级ID
     */
    private void verifyClassStudent(Integer classId){
        //获取当前用户id与用户角色
        Integer currentId = BaseContext.getCurrentId();
        UserRole currentRole = BaseContext.getCurrentRole();

        switch (currentRole){
            case STUDENT: {//学生
                StudentClass studentClass = studentClassMapper.selectOne(new LambdaQueryWrapper<StudentClass>()
                        .eq(StudentClass::getStudentId, currentId)
                        .eq(StudentClass::getClassId, classId)
                );
                // 如果查询结果为空，说明指定的学生与班级关联不存在，抛出异常
                if (studentClass == null){
                    throw new ClassException(MessageConstant.CLASS_AND_STUDENT_NOT_FOUND);
                }
                break;
            }
            case TEACHER:{//老师
                // 查询指定班级ID和教师ID的班级信息，以验证教师与班级的关联
                Class c = classMapper.selectOne(new LambdaQueryWrapper<Class>()
                        .eq(Class::getId, classId)
                        .eq(Class::getTeacherId, currentId)
                );

                // 如果查询结果为空，说明指定的教师与班级关联不存在，抛出异常
                if (c == null){
                    throw new ClassException(MessageConstant.CLASS_AND_TEACHER_NOT_FOUND);
                }
                break;
            }
        }

    }

}
