package com.ecode.controller.user;


import com.ecode.ai.repository.FileRepository;
import com.ecode.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/ai/pdf")
@Tag(name = "AI管理")
public class PdfController {

    private final FileRepository fileRepository;

    private final VectorStore vectorStore;
    /**
     * 文件上传
     */
    @PostMapping("/upload/{classId}")
    @Operation(summary = "知识库文件上传")
    public Result uploadPdf(@PathVariable String classId,
                            @RequestParam("file") MultipartFile file) {
        try {
            // 1. 校验文件是否为PDF格式
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                return Result.error("只能上传PDF文件！");
            }
            // 2.保存文件
            boolean success = fileRepository.save(classId, file.getResource());
            if(! success) {
                return Result.error("保存文件失败！");
            }
            // 3.写入向量库
            this.writeToVectorStore(file.getResource());
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
    @Operation(summary = "知识库文件下载")
    public ResponseEntity<Resource> download(@PathVariable String classId)  {
        // 1.读取文件
        Resource resource = fileRepository.getFile(classId);
        if (resource == null || !resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        // 2.文件名编码，写入响应头
        String filename = URLEncoder.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8);
        // 3.返回文件
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
    @GetMapping("/exit/{classId}")
    @Operation(summary = "知识库文件是否存在")
    public Result exit(@PathVariable String classId)  {
        // 1.读取文件
        Resource resource = fileRepository.getFile(classId);
        if (resource == null || !resource.exists()) {
            return Result.success(false);
        }
        return Result.success(true);
    }

    private void writeToVectorStore(Resource resource) {
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
        // 3.写入向量库
        vectorStore.add(documents);
    }
}