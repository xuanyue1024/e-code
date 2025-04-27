package com.ecode.service;

import com.ecode.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecode.vo.TagVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-28
 */
public interface TagService extends IService<Tag> {

    /**
     * 按名字模糊查询
     *
     * @param name 名字
     * @return 列表<tag vo>
     */
    List<TagVO> getByName(String name);

    /**
     * 按ids获取
     *
     * @param ids id
     * @return 列表<tag vo>
     */
    List<TagVO> getByIds(List<Integer> ids);

    /**
     * 按题目id获取
     *
     * @param problemId 题目id
     * @return 列表<tag vo>
     */
    List<TagVO> getByProblemId(Integer problemId);

    /**
     * 将字符串列表转换为Integer类型的标签列表。
     *
     * @param tags 字符串标签id列表
     * @return 转换后的Integer类型标签列表
     */
    List<Integer> addTags(List<String> tags);
}
