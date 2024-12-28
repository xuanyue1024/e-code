package com.ecode.service;

import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.dto.ProblemAddDTO;
import com.ecode.vo.PageVO;
import com.ecode.vo.ProblemVO;

import java.util.List;

/**
 * <p>
 *  问题服务类
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-28
 */
public interface ProblemService {

    /**
     * 添加问题
     *
     * @param problemAddDTO 问题加到
     */
    void add(ProblemAddDTO problemAddDTO);

    /**
     * 删除题目批量
     *
     * @param ids id
     */
    void deleteProblemBatch(List<Integer> ids);

    /**
     * 分页查询
     *
     * @param generalPageQueryDTO 一般页面查询
     * @return 页vo<问题vo>
     */
    PageVO<ProblemVO> pageQuery(GeneralPageQueryDTO generalPageQueryDTO);

}
