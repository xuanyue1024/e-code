package com.ecode.controller.user.student;

import com.ecode.result.Result;
import com.ecode.service.TagService;
import com.ecode.vo.TagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "题目标签管理")
@RestController
@RequestMapping("/student/tag")
@Component("studentTagController")
public class TagController {

    @Autowired
    private TagService tagService;


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
