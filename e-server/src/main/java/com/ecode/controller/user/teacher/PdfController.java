package com.ecode.controller.user.teacher;


import com.ecode.ai.repository.FileRepository;
import com.ecode.annotation.DataAccessCheck;
import com.ecode.entity.po.RepositoryFile;
import com.ecode.enumeration.OperationType;
import com.ecode.result.Result;
import com.ecode.vo.RepositoryFileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/teacher/ai/pdf")
@Tag(name = "AI管理")
public class PdfController {

    private final FileRepository fileRepository;

    private final VectorStore vectorStore;
    /**
     * 文件上传
     */
    @PostMapping("/upload/{classId}")
    @Operation(summary = "知识库文件上传")
    @DataAccessCheck(value = OperationType.TEACHER_TO_CLASS)
    public Result uploadPdf(@PathVariable Integer classId,
                            @RequestPart("file") MultipartFile file) {
        try {
            // 1. 校验文件是否为PDF格式
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                return Result.error("只能上传PDF文件！");
            }
            // 2.保存文件
            boolean success = fileRepository.save(classId, file);
            if(!success) {
                return Result.error("保存文件失败！");
            }
            // 3.写入向量库
            this.writeToVectorStore(file.getResource(), classId);
            return Result.success();
        } catch (Exception e) {
            log.error("Failed to upload PDF.", e);
            return Result.error("上传文件失败！");
        }
    }

    /**
     * 文件下载
     */
    @GetMapping("/file/{classId}")
    @Operation(summary = "知识库文件下载",description = "如果文件不存在,data返回null")
    @DataAccessCheck(value = OperationType.TEACHER_TO_CLASS)
    public Result<RepositoryFileVO> download(@PathVariable Integer  classId)  {
        RepositoryFile file = fileRepository.getFile(classId);
        RepositoryFileVO rfv = new RepositoryFileVO();
        BeanUtils.copyProperties(file, rfv);
        return Result.success(rfv);
    }

    @DeleteMapping("/delete/{classId}")
    @Operation(summary = "知识库文件删除")
    @DataAccessCheck(value = OperationType.TEACHER_TO_CLASS)
    public Result delete(@PathVariable Integer classId){
        // 删除文件
        fileRepository.deleteFile(classId);
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        vectorStore.delete(b.eq("classId", classId).build());

        return Result.success();
    }

    /**
     * 根据查询字符串检索最相似的文档列表
     *
     * @param classId 班级ID，用于指定查询的知识库范围
     * @param query   查询字符串，用户输入的搜索文本
     * @return 与查询字符串最相似的一系列文档
     */
    @GetMapping("/query/{classId}")
    @Operation(summary = "知识库文件内容查询")
    @DataAccessCheck(value = OperationType.TEACHER_TO_CLASS)
    public Result query(@PathVariable Integer classId, @RequestParam String query) {
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .filterExpression("classId == " + classId)
                .build();
        List<Document> results = vectorStore.similaritySearch(request);
        return Result.success(results);
    }

    private void writeToVectorStore(Resource resource, Integer  classId) {
        // 1.创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        // 2.读取PDF文档，拆分为Document
        List<Document> documents = reader.read();

        documents.forEach(doc -> {
            doc.getMetadata().put("classId", classId);
        });

        // 3.写入向量库
        vectorStore.add(documents);
    }
}