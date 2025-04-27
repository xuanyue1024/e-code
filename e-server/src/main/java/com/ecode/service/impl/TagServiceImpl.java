package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.entity.ProblemTag;
import com.ecode.entity.Tag;
import com.ecode.mapper.ProblemTagMapper;
import com.ecode.mapper.TagMapper;
import com.ecode.service.TagService;
import com.ecode.vo.TagVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ProblemTagMapper problemTagMapper;

    @Override
    public List<TagVO> getByName(String name) {
        List<Tag> tags = tagMapper.selectList(new QueryWrapper<Tag>()
                .lambda()
                .like(name != null, Tag::getName, name));

        return tags.stream().map(t -> {
            TagVO tv = new TagVO();
            BeanUtils.copyProperties(t, tv);
            return tv;
        }).collect(Collectors.toList());

    }

    @Override
    public List<TagVO> getByIds(List<Integer> ids) {
        List<Tag> tags = tagMapper.selectBatchIds(ids);
        return tags.stream().map(t -> TagVO
                .builder()
                .id(t.getId())
                .name(t.getName())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<TagVO> getByProblemId(Integer problemId) {
        List<Integer> tagIds = problemTagMapper.selectList(
                new LambdaQueryWrapper<ProblemTag>().eq(ProblemTag::getProblemId, problemId)
        ).stream().map(ProblemTag::getTagId).toList();

        return getByIds(tagIds);
    }

    @Override
    public List<Integer> addTags(List<String> tags) {
        List<Integer> tagIds = new ArrayList<>();
        tags.forEach(tag -> {
            Tag t = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getName, tag));
            if (t == null) {
                t = Tag.builder()
                        .name(tag)
                        .createTime(LocalDateTime.now())
                        .build();
                tagMapper.insert(t);
            }
            tagIds.add(t.getId());
        });
        return tagIds;
    }
}
