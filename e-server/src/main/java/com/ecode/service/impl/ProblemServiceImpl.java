package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecode.constant.MessageConstant;
import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.dto.ProblemAddDTO;
import com.ecode.dto.ProblemUpdateDTO;
import com.ecode.dto.SetTagsDTO;
import com.ecode.entity.ClassProblem;
import com.ecode.entity.Problem;
import com.ecode.entity.ProblemTag;
import com.ecode.entity.Tag;
import com.ecode.enumeration.ProblemGrade;
import com.ecode.exception.ProblemException;
import com.ecode.mapper.ClassProblemMapper;
import com.ecode.mapper.ProblemMapper;
import com.ecode.mapper.ProblemTagMapper;
import com.ecode.mapper.TagMapper;
import com.ecode.service.ProblemService;
import com.ecode.utils.ExcelUtil;
import com.ecode.vo.ImportResultVO;
import com.ecode.vo.PageVO;
import com.ecode.vo.ProblemPageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;
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

    private static final List<String> PROBLEM_EXCEL_HEADERS = List.of(
            "id", "title", "content", "answer", "grade", "maxMemory", "maxTime",
            "inputTest1", "outputTest1", "inputTest2", "outputTest2",
            "inputTest3", "outputTest3", "inputTest4", "outputTest4", "tags");

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemTagMapper problemTagMapper;
    @Autowired
    private ClassProblemMapper classProblemMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public Integer add(ProblemAddDTO problemAddDTO) {
        Problem p = new Problem();
        BeanUtils.copyProperties(problemAddDTO, p);
        p.setCreateTime(LocalDateTime.now());
        p.setUpdateTime(LocalDateTime.now());
        problemMapper.insert(p);
        return p.getId();
    }

    @Transactional
    @Override
    public void deleteProblemBatch(List<Integer> ids) {
        LambdaQueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<Problem>()
                .lambda()
                .in(Problem::getId, ids);
        problemMapper.delete(problemQueryWrapper);
        //todo 班级问题得删除
        classProblemMapper.delete(new QueryWrapper<ClassProblem>().lambda().in(ClassProblem::getProblemId, ids));
    }

    @Override
    public PageVO<ProblemPageVO> pageQuery(GeneralPageQueryDTO generalPageQueryDTO) {
        Page<Problem> page = generalPageQueryDTO.nullToDefault();
        //判断是否有name
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
        List<ProblemPageVO> list = records.stream().map(r -> {
            ProblemPageVO pv = new ProblemPageVO();
            BeanUtils.copyProperties(r, pv);
            //查询并设置单个题目的标签集合
            //todo 性能可优化
            List<ProblemTag> problemTags = problemTagMapper.selectList(new LambdaQueryWrapper<ProblemTag>().eq(ProblemTag::getProblemId, r.getId()));
            pv.setTagIds(problemTags.stream().map(ProblemTag::getTagId).collect(Collectors.toList()));

            return pv;
        }).collect(Collectors.toList());

        return new PageVO<>(problemPage.getTotal(), problemPage.getPages(), list);
    }

    @Override
    public void updateProblem(ProblemUpdateDTO problemUpdateDTO) {
        Problem p = new Problem();
        BeanUtils.copyProperties(problemUpdateDTO, p);
        p.setUpdateTime(LocalDateTime.now());
        problemMapper.updateById(p);
    }

   @Override
   public <E> E getProblem(Integer id, Class<E> clazz) {
       Problem p = problemMapper.selectById(id);
       if (p == null){
           throw new ProblemException(MessageConstant.DATA_NOT_FOUND);
       }
       List<Tag> tags = tagMapper.selectTagByProblemId(id);

       try {
           E result = clazz.getDeclaredConstructor().newInstance();
           BeanUtils.copyProperties(p, result);

          //设置标签
          if (tags != null && !tags.isEmpty()) {
              clazz.getMethod("setTags", List.class).invoke(result, tags);
          }
           return result;
       } catch (Exception e) {
           throw new RuntimeException("无法创建目标类实例: " + e.getMessage());
       }
   }

    @Override
    public void setTags(SetTagsDTO setTagsDTO) {
        Problem p = problemMapper.selectById(setTagsDTO.getProblemId());
        if (p == null){
            throw new ProblemException(MessageConstant.PROBLEM_NOT_FOUND);
        }
        //先删除所有
        problemTagMapper.delete(new LambdaQueryWrapper<ProblemTag>().eq(ProblemTag::getProblemId, setTagsDTO.getProblemId()));
        //增加
        List<Integer> tagIds = setTagsDTO.getTagIds();
        tagIds.forEach(ts -> {
            ProblemTag pt = ProblemTag.builder()
                    .problemId(setTagsDTO.getProblemId())
                    .tagId(ts)
                    .build();
            problemTagMapper.insert(pt);
        });
    }

    @Override
    public byte[] exportProblems() {
        List<Problem> problems = problemMapper.selectList(new LambdaQueryWrapper<Problem>().orderByDesc(Problem::getUpdateTime));
        // 题目导出把多对多标签压成逗号分隔文本，方便 Excel 人工编辑后再导入。
        List<List<Object>> rows = problems.stream().map(problem -> List.<Object>of(
                valueOrBlank(problem.getId()),
                valueOrBlank(problem.getTitle()),
                valueOrBlank(problem.getContent()),
                valueOrBlank(problem.getAnswer()),
                valueOrBlank(problem.getGrade()),
                valueOrBlank(problem.getMaxMemory()),
                valueOrBlank(problem.getMaxTime()),
                valueOrBlank(problem.getInputTest1()),
                valueOrBlank(problem.getOutputTest1()),
                valueOrBlank(problem.getInputTest2()),
                valueOrBlank(problem.getOutputTest2()),
                valueOrBlank(problem.getInputTest3()),
                valueOrBlank(problem.getOutputTest3()),
                valueOrBlank(problem.getInputTest4()),
                valueOrBlank(problem.getOutputTest4()),
                tagNames(problem.getId())
        )).toList();
        return ExcelUtil.write("题目", PROBLEM_EXCEL_HEADERS, rows);
    }

    @Override
    public byte[] exportProblemTemplate() {
        return ExcelUtil.write("题目导入模板", PROBLEM_EXCEL_HEADERS, List.of(List.of(
                "", "两数之和", "请输出两个整数之和", "读取两个整数后输出和", "EASY", "512", "5",
                "1 2", "3", "", "", "", "", "", "", "基础,输入输出"
        )));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResultVO importProblems(MultipartFile file) {
        List<Map<String, String>> rows = ExcelUtil.read(file, List.of("title", "grade"));
        ImportResultVO result = new ImportResultVO();
        result.setTotal(rows.size());
        // 标题是题库中最容易人工识别的唯一键，文件内重复标题直接跳过。
        Set<String> seenTitles = new HashSet<>();

        for (Map<String, String> row : rows) {
            // 保留 Excel 原始行号，便于管理员按导入结果回到表格修正。
            int rowNumber = Integer.parseInt(row.get("__rowNumber"));
            try {
                String title = row.get("title");
                // 标题和难度是题目能被列表展示、筛选和评测分组的最低必填项。
                if (!StringUtils.hasText(title) || !StringUtils.hasText(row.get("grade"))) {
                    result.addFailed(rowNumber, "标题和难度不能为空");
                    continue;
                }
                if (!seenTitles.add(title)) {
                    result.addSkipped(rowNumber, "文件内题目标题重复");
                    continue;
                }
                Integer id = parseInteger(row.get("id"));
                // 有 id 时先按主键判断，避免导入误覆盖数据库已有题目。
                if (id != null && problemMapper.selectById(id) != null) {
                    result.addSkipped(rowNumber, "题目id已存在");
                    continue;
                }
                // 无 id 或 id 不存在时再按标题精确匹配，符合“已存在跳过”的导入策略。
                if (problemMapper.selectCount(new LambdaQueryWrapper<Problem>().eq(Problem::getTitle, title)) > 0) {
                    result.addSkipped(rowNumber, "题目标题已存在");
                    continue;
                }

                // Entity 没有 Lombok Builder，这里显式赋值以保持字段映射清晰。
                Problem problem = new Problem();
                problem.setId(id);
                problem.setTitle(title);
                problem.setContent(row.get("content"));
                problem.setAnswer(row.get("answer"));
                problem.setGrade(ProblemGrade.valueOf(row.get("grade")));
                problem.setMaxMemory(StringUtils.hasText(row.get("maxMemory")) ? row.get("maxMemory") : "512");
                problem.setMaxTime(StringUtils.hasText(row.get("maxTime")) ? Integer.valueOf(row.get("maxTime")) : 5);
                problem.setInputTest1(row.get("inputTest1"));
                problem.setOutputTest1(row.get("outputTest1"));
                problem.setInputTest2(row.get("inputTest2"));
                problem.setOutputTest2(row.get("outputTest2"));
                problem.setInputTest3(row.get("inputTest3"));
                problem.setOutputTest3(row.get("outputTest3"));
                problem.setInputTest4(row.get("inputTest4"));
                problem.setOutputTest4(row.get("outputTest4"));
                problem.setCreateTime(LocalDateTime.now());
                problem.setUpdateTime(LocalDateTime.now());
                problemMapper.insert(problem);
                // 题目插入成功后再绑定标签，确保 problemId 已由数据库回填。
                bindTags(problem.getId(), row.get("tags"));
                result.addCreated(rowNumber, "新增题目：" + title);
            } catch (IllegalArgumentException e) {
                result.addFailed(rowNumber, "难度或数字格式错误");
            } catch (Exception e) {
                result.addFailed(rowNumber, e.getMessage());
            }
        }
        return result;
    }

    private void bindTags(Integer problemId, String tagNames) {
        if (!StringUtils.hasText(tagNames)) {
            return;
        }
        // 标签列允许人工用英文逗号维护多个标签，缺失标签会自动创建后再建立关联。
        Arrays.stream(tagNames.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(this::getOrCreateTagId)
                .filter(Objects::nonNull)
                .forEach(tagId -> problemTagMapper.insert(ProblemTag.builder()
                        .problemId(problemId)
                        .tagId(tagId)
                        .build()));
    }

    private Integer getOrCreateTagId(String name) {
        Tag tag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name));
        if (tag != null) {
            return tag.getId();
        }
        Tag newTag = Tag.builder()
                .name(name)
                .createTime(LocalDateTime.now())
                .build();
        tagMapper.insert(newTag);
        return newTag.getId();
    }

    private String tagNames(Integer problemId) {
        List<Tag> tags = tagMapper.selectTagByProblemId(problemId);
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return tags.stream().map(Tag::getName).collect(Collectors.joining(","));
    }

    private Integer parseInteger(String value) {
        return StringUtils.hasText(value) ? Integer.valueOf(value) : null;
    }

    private Object valueOrBlank(Object value) {
        return value == null ? "" : value;
    }
}
