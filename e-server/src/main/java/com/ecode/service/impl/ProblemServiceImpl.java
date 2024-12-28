package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecode.dto.ProblemAddDTO;
import com.ecode.entity.Problem;
import com.ecode.mapper.ProblemMapper;
import com.ecode.service.ProblemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public void deleteProblemBatch(List<Integer> ids) {
        LambdaQueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<Problem>()
                .lambda()
                .in(Problem::getId, ids);
        problemMapper.delete(problemQueryWrapper);
    }
}
