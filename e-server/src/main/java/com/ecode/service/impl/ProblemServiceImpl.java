package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecode.constant.MessageConstant;
import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.dto.ProblemAddDTO;
import com.ecode.dto.ProblemUpdateDTO;
import com.ecode.dto.SetTagsDTO;
import com.ecode.entity.ClassProblem;
import com.ecode.entity.Problem;
import com.ecode.entity.ProblemTag;
import com.ecode.exception.ProblemException;
import com.ecode.mapper.ClassProblemMapper;
import com.ecode.mapper.ProblemMapper;
import com.ecode.mapper.ProblemTagMapper;
import com.ecode.service.ProblemService;
import com.ecode.vo.PageVO;
import com.ecode.vo.ProblemPageVO;
import com.ecode.vo.ProblemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-28
 */
@Service
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemTagMapper problemTagMapper;
    @Autowired
    private ClassProblemMapper classProblemMapper;

    @Override
    public Integer add(ProblemAddDTO problemAddDTO) {
        Problem p = new Problem();
        BeanUtils.copyProperties(problemAddDTO, p);
        p.setCreateTime(LocalDateTime.now());
        p.setUpdateTime(LocalDateTime.now());
        problemMapper.insert(p);
        return p.getId();
    }

    @Transactional
    @Override
    public void deleteProblemBatch(List<Integer> ids) {
        LambdaQueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<Problem>()
                .lambda()
                .in(Problem::getId, ids);
        problemMapper.delete(problemQueryWrapper);
        //todo 班级问题得删除
        classProblemMapper.delete(new QueryWrapper<ClassProblem>().lambda().in(ClassProblem::getProblemId, ids));
    }

    @Override
    public PageVO<ProblemPageVO> pageQuery(GeneralPageQueryDTO generalPageQueryDTO) {
        Page<Problem> page = generalPageQueryDTO.nullToDefault();
        //判断是否有name
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        if (generalPageQueryDTO.getName() != null && !generalPageQueryDTO.getName().isEmpty()){
            queryWrapper.lambda().like(Problem::getTitle, generalPageQueryDTO.getName());
        }


        Page<Problem> problemPage = problemMapper.selectPage(page, queryWrapper);
        List<Problem> records = problemPage.getRecords();
        //没数据，返回空结果
        if (records == null || records.isEmpty()){
            return new PageVO<>(problemPage.getTotal(), problemPage.getPages(), Collections.emptyList());
        }
        //有数据，转换
        List<ProblemPageVO> list = records.stream().map(r -> {
            ProblemPageVO pv = new ProblemPageVO();
            BeanUtils.copyProperties(r, pv);
            //查询并设置单个题目的标签集合
            //todo 性能可优化
            List<ProblemTag> problemTags = problemTagMapper.selectList(new LambdaQueryWrapper<ProblemTag>().eq(ProblemTag::getProblemId, r.getId()));
            pv.setTagIds(problemTags.stream().map(ProblemTag::getTagId).collect(Collectors.toList()));

            return pv;
        }).collect(Collectors.toList());

        return new PageVO<>(problemPage.getTotal(), problemPage.getPages(), list);
    }

    @Override
    public void updateProblem(ProblemUpdateDTO problemUpdateDTO) {
        Problem p = new Problem();
        BeanUtils.copyProperties(problemUpdateDTO, p);
        p.setUpdateTime(LocalDateTime.now());
        problemMapper.updateById(p);
    }

    @Override
    public ProblemVO getProblem(Integer id) {
        Problem p = problemMapper.selectById(id);
        if (p == null){
            throw new ProblemException(MessageConstant.DATA_NOT_FOUND);
        }

        ProblemVO pv = new ProblemVO();
        BeanUtils.copyProperties(p, pv);
        return pv;
    }

    @Override
    public void setTags(SetTagsDTO setTagsDTO) {
        Problem p = problemMapper.selectById(setTagsDTO.getProblemId());
        if (p == null){
            throw new ProblemException(MessageConstant.PROBLEM_NOT_FOUND);
        }
        //先删除所有
        problemTagMapper.delete(new LambdaQueryWrapper<ProblemTag>().eq(ProblemTag::getProblemId, setTagsDTO.getProblemId()));
        //增加
        List<Integer> tagIds = setTagsDTO.getTagIds();
        tagIds.forEach(ts -> {
            ProblemTag pt = ProblemTag.builder()
                    .problemId(setTagsDTO.getProblemId())
                    .tagId(ts)
                    .build();
            problemTagMapper.insert(pt);
        });
    }
}
