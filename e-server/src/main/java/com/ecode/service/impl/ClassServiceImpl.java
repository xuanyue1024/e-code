package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.AdminClassCreateDTO;
import com.ecode.dto.AdminClassStudentDTO;
import com.ecode.dto.AdminClassUpdateDTO;
import com.ecode.dto.ClassProblemDTO;
import com.ecode.dto.ClassProblemPageQueryDTO;
import com.ecode.dto.ClassStudentDTO;
import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.entity.Class;
import com.ecode.entity.*;
import com.ecode.enumeration.UserRole;
import com.ecode.exception.BaseException;
import com.ecode.exception.ClassException;
import com.ecode.mapper.*;
import com.ecode.service.ClassService;
import com.ecode.utils.ExcelUtil;
import com.ecode.utils.InvitationCodeUtil;
import com.ecode.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 班级service实现类
 *
 * @author 竹林听雨
 * @date 2024/11/24
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    private static final List<String> CLASS_EXCEL_HEADERS = List.of(
            "id", "teacherId", "name", "invitationCode", "joinNumber");

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

    @Override
    public void adminAddClass(AdminClassCreateDTO createDTO) {
        verifyTeacher(createDTO.getTeacherId());
        Class c = Class.builder()
                .name(createDTO.getName())
                .teacherId(createDTO.getTeacherId())
                .joinNumber(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        classMapper.insert(c);
        c.setInvitationCode(InvitationCodeUtil.inviCodeGenerator(c.getId()));
        classMapper.updateById(c);
    }

    @Override
    public Class getAdminClassById(Integer id) {
        Class c = classMapper.selectById(id);
        if (c == null) {
            throw new ClassException(MessageConstant.DATA_NOT_FOUND);
        }
        return c;
    }

    @Override
    public void adminUpdateClass(AdminClassUpdateDTO updateDTO) {
        verifyTeacher(updateDTO.getTeacherId());
        Class c = Class.builder()
                .id(updateDTO.getId())
                .teacherId(updateDTO.getTeacherId())
                .name(updateDTO.getName())
                .updateTime(LocalDateTime.now())
                .build();
        int rows = classMapper.updateById(c);
        if (rows <= 0) {
            throw new ClassException(MessageConstant.UPDATE_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminDeleteBatch(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ClassException(MessageConstant.PARAMETER_ERROR);
        }

        List<StudentClass> studentClasses = studentClassMapper.selectList(new LambdaQueryWrapper<StudentClass>()
                .in(StudentClass::getClassId, ids));
        List<Integer> scIds = studentClasses.stream().map(StudentClass::getId).toList();
        if (!scIds.isEmpty()) {
            classScoreMapper.delete(new LambdaQueryWrapper<ClassScore>().in(ClassScore::getScId, scIds));
        }

        List<ClassProblem> classProblems = classProblemMapper.selectList(new LambdaQueryWrapper<ClassProblem>()
                .in(ClassProblem::getClassId, ids));
        List<Integer> classProblemIds = classProblems.stream().map(ClassProblem::getId).toList();
        if (!classProblemIds.isEmpty()) {
            classScoreMapper.delete(new LambdaQueryWrapper<ClassScore>().in(ClassScore::getClassProblemId, classProblemIds));
        }

        studentClassMapper.delete(new LambdaQueryWrapper<StudentClass>().in(StudentClass::getClassId, ids));
        classProblemMapper.delete(new LambdaQueryWrapper<ClassProblem>().in(ClassProblem::getClassId, ids));
        classMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminAddStudents(AdminClassStudentDTO dto) {
        getAdminClassById(dto.getClassId());
        for (Integer studentId : dto.getStudentIds()) {
            verifyStudent(studentId);
            StudentClass existing = studentClassMapper.selectOne(new LambdaQueryWrapper<StudentClass>()
                    .eq(StudentClass::getClassId, dto.getClassId())
                    .eq(StudentClass::getStudentId, studentId));
            if (existing != null) {
                throw new ClassException(MessageConstant.ALREADY_EXISTS);
            }
            studentClassMapper.insert(StudentClass.builder()
                    .classId(dto.getClassId())
                    .studentId(studentId)
                    .joinTime(LocalDateTime.now())
                    .build());
        }

        classMapper.update(null, new UpdateWrapper<Class>().lambda()
                .setSql("join_number = join_number + " + dto.getStudentIds().size())
                .eq(Class::getId, dto.getClassId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminRemoveStudents(AdminClassStudentDTO dto) {
        List<StudentClass> studentClasses = studentClassMapper.selectList(new LambdaQueryWrapper<StudentClass>()
                .eq(StudentClass::getClassId, dto.getClassId())
                .in(StudentClass::getStudentId, dto.getStudentIds()));
        if (studentClasses.isEmpty()) {
            throw new ClassException(MessageConstant.DATA_NOT_FOUND);
        }

        List<Integer> scIds = studentClasses.stream().map(StudentClass::getId).toList();
        classScoreMapper.delete(new LambdaQueryWrapper<ClassScore>().in(ClassScore::getScId, scIds));
        studentClassMapper.deleteBatchIds(scIds);
        classMapper.update(null, new UpdateWrapper<Class>().lambda()
                .setSql("join_number = GREATEST(join_number - " + studentClasses.size() + ", 0)")
                .eq(Class::getId, dto.getClassId()));
    }

    @Override
    public byte[] exportClasses() {
        List<Class> classes = classMapper.selectList(new LambdaQueryWrapper<Class>().orderByDesc(Class::getUpdateTime));
        List<List<Object>> rows = classes.stream().map(c -> List.<Object>of(
                valueOrBlank(c.getId()),
                valueOrBlank(c.getTeacherId()),
                valueOrBlank(c.getName()),
                valueOrBlank(c.getInvitationCode()),
                valueOrBlank(c.getJoinNumber())
        )).toList();
        return ExcelUtil.write("班级", CLASS_EXCEL_HEADERS, rows);
    }

    @Override
    public byte[] exportClassTemplate() {
        return ExcelUtil.write("班级导入模板", CLASS_EXCEL_HEADERS, List.of(List.of(
                "", "1", "Java后端基础班", "", "0"
        )));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResultVO importClasses(MultipartFile file) {
        List<Map<String, String>> rows = ExcelUtil.read(file, List.of("teacherId", "name"));
        ImportResultVO result = new ImportResultVO();
        result.setTotal(rows.size());
        Set<String> seenClassKeys = new HashSet<>();
        Set<String> seenInvitationCodes = new HashSet<>();

        for (Map<String, String> row : rows) {
            int rowNumber = Integer.parseInt(row.get("__rowNumber"));
            try {
                Integer id = parseInteger(row.get("id"));
                Integer teacherId = parseInteger(row.get("teacherId"));
                String name = row.get("name");
                String invitationCode = row.get("invitationCode");
                if (teacherId == null || !StringUtils.hasText(name)) {
                    result.addFailed(rowNumber, "教师id和班级名称不能为空");
                    continue;
                }
                verifyTeacher(teacherId);
                String classKey = teacherId + ":" + name;
                if (!seenClassKeys.add(classKey)) {
                    result.addSkipped(rowNumber, "文件内教师和班级名称重复");
                    continue;
                }
                if (StringUtils.hasText(invitationCode) && !seenInvitationCodes.add(invitationCode)) {
                    result.addSkipped(rowNumber, "文件内邀请码重复");
                    continue;
                }
                if (id != null && classMapper.selectById(id) != null) {
                    result.addSkipped(rowNumber, "班级id已存在");
                    continue;
                }
                if (StringUtils.hasText(invitationCode)
                        && classMapper.selectCount(new LambdaQueryWrapper<Class>().eq(Class::getInvitationCode, invitationCode)) > 0) {
                    result.addSkipped(rowNumber, "邀请码已存在");
                    continue;
                }
                if (classMapper.selectCount(new LambdaQueryWrapper<Class>()
                        .eq(Class::getTeacherId, teacherId)
                        .eq(Class::getName, name)) > 0) {
                    result.addSkipped(rowNumber, "教师名下同名班级已存在");
                    continue;
                }

                Integer joinNumber = parseInteger(row.get("joinNumber"));
                Class c = Class.builder()
                        .id(id)
                        .teacherId(teacherId)
                        .name(name)
                        .invitationCode(StringUtils.hasText(invitationCode) ? invitationCode : null)
                        .joinNumber(joinNumber == null ? 0 : joinNumber)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                classMapper.insert(c);
                if (!StringUtils.hasText(c.getInvitationCode())) {
                    c.setInvitationCode(InvitationCodeUtil.inviCodeGenerator(c.getId()));
                    classMapper.updateById(c);
                }
                result.addCreated(rowNumber, "新增班级：" + name);
            } catch (NumberFormatException e) {
                result.addFailed(rowNumber, "数字格式错误");
            } catch (Exception e) {
                result.addFailed(rowNumber, e.getMessage());
            }
        }
        return result;
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

    private void verifyTeacher(Integer teacherId) {
        User teacher = userMapper.selectById(teacherId);
        if (teacher == null || teacher.getRole() != UserRole.TEACHER) {
            throw new BaseException(MessageConstant.USER_NOT_FOUND);
        }
    }

    private void verifyStudent(Integer studentId) {
        User student = userMapper.selectById(studentId);
        if (student == null || student.getRole() != UserRole.STUDENT) {
            throw new BaseException(MessageConstant.USER_NOT_FOUND);
        }
    }

    private Integer parseInteger(String value) {
        return StringUtils.hasText(value) ? Integer.valueOf(value) : null;
    }

    private Object valueOrBlank(Object value) {
        return value == null ? "" : value;
    }

}
