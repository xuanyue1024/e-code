package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.ClassPageQueryDTO;
import com.ecode.entity.Class;
import com.ecode.exception.ClassException;
import com.ecode.mapper.ClassMapper;
import com.ecode.service.ClassService;
import com.ecode.utils.InvitationCodeUtil;
import com.ecode.vo.ClassVO;
import com.ecode.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
    public PageVO<ClassVO> pageQuery(ClassPageQueryDTO classPageQueryDTO) {
        Page<Class> page = Page.of(classPageQueryDTO.getPageNo(), classPageQueryDTO.getPageSize());
        // 1.2.排序条件
        if (classPageQueryDTO.getSortBy() != null && !classPageQueryDTO.getSortBy().isBlank()) {
            page.addOrder(new OrderItem(classPageQueryDTO.getSortBy(), classPageQueryDTO.getIsAsc()));
        }else{
            // 默认按照更新时间排序
            page.addOrder(new OrderItem("update_time", false));
        }
        //1.3 查询条件
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(classPageQueryDTO.getName() != null,Class::getName, classPageQueryDTO.getName())
                .eq(Class::getTeacherId, BaseContext.getCurrentId());
        // 2.查询
        this.page(page, queryWrapper);
        // 3.数据非空校验
        List<Class> records = page.getRecords();
        if (records == null || records.size() <= 0) {
            // 无数据，返回空结果
            return new PageVO<>(page.getTotal(), page.getPages(), Collections.emptyList());
        }
        // 4.有数据，转换
        List<ClassVO> list = records.stream().map(c -> {
            ClassVO cv = new ClassVO();
            BeanUtils.copyProperties(c, cv);
            return cv;
        }).collect(Collectors.toList());

        // 5.封装返回
        return new PageVO<ClassVO>(page.getTotal(), page.getPages(), list);
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
                .eq( Class::getId, id)
                .eq(Class::getTeacherId, teacherId);
        int i = classMapper.update(null, wrapper);
        if (i <= 0){
            throw new ClassException(MessageConstant.UPDATE_FAILED);
        }
    }
}
