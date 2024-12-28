package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.entity.Tag;
import com.ecode.mapper.TagMapper;
import com.ecode.service.TagService;
import com.ecode.vo.TagVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
