package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.dto.ProblemAddDTO;
import com.ecode.entity.Problem;
import com.ecode.mapper.ProblemMapper;
import com.ecode.service.ProblemService;
import com.ecode.vo.PageVO;
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

    @Override
    public void add(ProblemAddDTO problemAddDTO) {
        Problem p = new Problem();
        BeanUtils.copyProperties(problemAddDTO, p);
        p.setCreateTime(LocalDateTime.now());
        p.setUpdateTime(LocalDateTime.now());
        problemMapper.insert(p);
    }

    @Transactional
    @Override
    public void deleteProblemBatch(List<Integer> ids) {
        LambdaQueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<Problem>()
                .lambda()
                .in(Problem::getId, ids);
        problemMapper.delete(problemQueryWrapper);
    }

    @Override
    public PageVO<ProblemVO> pageQuery(GeneralPageQueryDTO generalPageQueryDTO) {
        Page<Problem> page = generalPageQueryDTO.nullToDefault();

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
        List<ProblemVO> list = records.stream().map(r -> {
            ProblemVO pv = new ProblemVO();
            BeanUtils.copyProperties(r, pv);
            return pv;
        }).collect(Collectors.toList());

        return new PageVO<>(problemPage.getTotal(), problemPage.getPages(), list);
    }
}
