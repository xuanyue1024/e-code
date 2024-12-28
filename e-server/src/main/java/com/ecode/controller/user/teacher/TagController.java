package com.ecode.controller.user.teacher;

import com.ecode.entity.Tag;
import com.ecode.result.Result;
import com.ecode.service.TagService;
import com.ecode.vo.TagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Api(tags = "题目标签管理")
@RestController
@RequestMapping("/teacher/tag")
@Component("teacherTagController")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/{name}")
    @ApiOperation("增加标签")
    public Result add(@ApiParam("标签名称") @PathVariable String name){
        tagService.save(new Tag(null,name, LocalDateTime.now()));
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除标签")
    public Result deleteBatch(@ApiParam("标签id集合") @RequestParam List<Integer> ids){
        tagService.removeBatchByIds(ids);
        return Result.success();
    }

    @GetMapping("/{name}")
    @ApiOperation("模糊查询标签")
    public Result<List<TagVO>> getByName(@PathVariable String name){
        List<TagVO> list = tagService.getByName(name);
        return Result.success(list);
    }

}
