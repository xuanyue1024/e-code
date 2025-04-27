package com.ecode.service;

import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.dto.ProblemAddDTO;
import com.ecode.dto.ProblemUpdateDTO;
import com.ecode.dto.SetTagsDTO;
import com.ecode.vo.PageVO;
import com.ecode.vo.ProblemPageVO;

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
    Integer add(ProblemAddDTO problemAddDTO);

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
    PageVO<ProblemPageVO> pageQuery(GeneralPageQueryDTO generalPageQueryDTO);

    /**
     * 更新题目
     *
     * @param problemUpdateDTO 问题更新
     */
    void updateProblem(ProblemUpdateDTO problemUpdateDTO);

    /**
     * 获取问题内容
     *
     * @param id id
     * @return 问题详细内容VO
     */
    <E> E getProblem(Integer id, Class<E> clazz);

    /**
     * 设置标签s
     *
     * @param setTagsDTO 设置标签dto
     */
    void setTags(SetTagsDTO setTagsDTO);
}
