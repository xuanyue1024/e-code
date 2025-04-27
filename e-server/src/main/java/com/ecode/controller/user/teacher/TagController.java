package com.ecode.controller.user.teacher;

import com.ecode.entity.Tag;
import com.ecode.result.Result;
import com.ecode.service.TagService;
import com.ecode.vo.TagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "题目标签管理")
@RestController
@RequestMapping("/teacher/tag")
@Component("teacherTagController")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/{name}")
    @Operation(summary = "增单个加标签", description = "返回值data里存放添加的标签id")
    public Result<Integer> add(@Parameter(name = "标签名称") @PathVariable String name){
        Tag tag = new Tag(null, name, LocalDateTime.now());
        tagService.save(tag);
        return Result.success(tag.getId());
    }

    @PostMapping("/batch")
    @Operation(summary = "批量添加标签", description = "根据标签名称列表批量添加标签，返回添加的标签ID列表")
    public Result<List<Integer>> addBatch(@Parameter(name = "标签名称集合", description = "要添加的标签名称列表") @RequestBody List<String> names) {
        List<Integer> list = tagService.addTags(names);
        return Result.success(list);
    }


    @DeleteMapping
    @Operation(summary = "批量删除标签")
    public Result deleteBatch(@Parameter(name = "标签id集合") @RequestParam List<Integer> ids){
        tagService.removeBatchByIds(ids);
        return Result.success();
    }

    @GetMapping
    @Operation(summary = "模糊查询标签")
    public Result<List<TagVO>> getByName(String name){
        List<TagVO> list = tagService.getByName(name);
        return Result.success(list);
    }

    @GetMapping("/getByIds")
    @Operation(summary = "通过获取标签集合", description = "根据标签id集合返回标签集合,也可传入单个")
    public Result<List<TagVO>> getByIds(@Parameter(name = "标签id集合") @RequestParam List<Integer> ids){
        List<TagVO> list = tagService.getByIds(ids);
        return Result.success(list);
    }

}
