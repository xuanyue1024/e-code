package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.ClassAddProblemDTO;
import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.entity.Class;
import com.ecode.entity.ClassProblem;
import com.ecode.entity.StudentClass;
import com.ecode.exception.ClassException;
import com.ecode.mapper.ClassMapper;
import com.ecode.mapper.ClassProblemMapper;
import com.ecode.mapper.StudentClassMapper;
import com.ecode.service.ClassService;
import com.ecode.utils.InvitationCodeUtil;
import com.ecode.vo.ClassVO;
import com.ecode.vo.PageVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public void addProblem(ClassAddProblemDTO classAddProblemDTO) {
        verifyClassStudent(BaseContext.getCurrentId(), classAddProblemDTO.getClassId());

        List<Integer> problemIds = classAddProblemDTO.getProblemIds();
        problemIds.forEach(pi -> {
            ClassProblem cp = ClassProblem.builder()
                    .classId(classAddProblemDTO.getClassId())
                    .problemId(pi)
                    .build();
            classProblemMapper.insert(cp);
        });
    }

    /**
     * 验证教师是否在指定班级授课
     * 此方法用于确保指定的教师与班级之间存在关联，防止教师对非自身班级的学生执行操作
     *
     * @param teacherId 教师ID
     * @param classId 班级ID
     * @throws ClassException 如果未找到指定教师与班级的关联，则抛出班级异常
     */
    private void verifyClassStudent(Integer teacherId, Integer classId){
        // 查询指定班级ID和教师ID的班级信息，以验证教师与班级的关联
        Class c = classMapper.selectOne(new LambdaQueryWrapper<Class>()
                .eq(Class::getId, classId)
                .eq(Class::getTeacherId, teacherId)
        );

        // 如果查询结果为空，说明指定的教师与班级关联不存在，抛出异常
        if (c == null){
            throw new ClassException(MessageConstant.CLASS_AND_TEACHER_NOT_FOUND);
        }
    }

}
