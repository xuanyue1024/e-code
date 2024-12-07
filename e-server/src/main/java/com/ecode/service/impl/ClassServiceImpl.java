package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.ClassPageQueryDTO;
import com.ecode.entity.Class;
import com.ecode.entity.StudentClass;
import com.ecode.exception.ClassException;
import com.ecode.mapper.ClassMapper;
import com.ecode.mapper.StudentClassMapper;
import com.ecode.service.ClassService;
import com.ecode.utils.InvitationCodeUtil;
import com.ecode.vo.ClassVO;
import com.ecode.vo.PageVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public PageVO<ClassVO> pageQuery(Integer teacherId, Integer studentId, ClassPageQueryDTO classPageQueryDTO) {

        PageHelper.startPage(Math.toIntExact(classPageQueryDTO.getPageNo()), Math.toIntExact(classPageQueryDTO.getPageSize()));
        //1.3 查询条件
        List<ClassVO> classVOList = classMapper.pageQueryByName(
                classPageQueryDTO.getName(),
                teacherId, studentId,
                classPageQueryDTO.getSortBy(),
                classPageQueryDTO.getIsAsc());

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
    }
}
