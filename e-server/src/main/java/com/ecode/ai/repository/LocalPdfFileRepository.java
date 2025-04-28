package com.ecode.ai.repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalPdfFileRepository implements FileRepository {

    private final VectorStore vectorStore;

    // 会话id 与 文件名的对应关系，方便查询会话历史时重新加载文件
    private final Properties chatFiles = new Properties();

    @Override
    public boolean save(String chatId, Resource resource) {

        // 2.保存到本地磁盘
        String filename = resource.getFilename();
        File target = new File("e-server/" + Objects.requireNonNull(filename));
        if (!target.exists()) {
            try {
                Files.copy(resource.getInputStream(), target.toPath());
            } catch (IOException e) {
                log.error("Failed to save PDF resource.", e);
                return false;
            }
        }
        // 3.保存映射关系
        chatFiles.put(chatId, filename);
        return true;
    }

    @Override
    public Resource getFile(String chatId) {
        String property = chatFiles.getProperty(chatId);
        log.info("getFile: {}", property);
        if (property == null) {
            return null;
        }
        return new FileSystemResource("e-server/" + property);
    }

    @PostConstruct
    private void init() {
        FileSystemResource pdfResource = new FileSystemResource("e-server\\chat-pdf.properties");
        if (pdfResource.exists()) {
            try {
                chatFiles.load(new BufferedReader(new InputStreamReader(pdfResource.getInputStream(), StandardCharsets.UTF_8)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileSystemResource vectorResource = new FileSystemResource("e-server\\chat-pdf.json");
        if (vectorResource.exists()) {
            SimpleVectorStore simpleVectorStore = (SimpleVectorStore) vectorStore;
            simpleVectorStore.load(vectorResource);
            log.info("向量存储加载完成，包文档");
        }else {
            log.warn("向量存储文件不存在: {}", vectorResource.getFilename());
        }
    }

    @PreDestroy
    private void persistent() {
        try {
            chatFiles.store(new FileWriter("e-server\\chat-pdf.properties"), LocalDateTime.now().toString());
            SimpleVectorStore simpleVectorStore = (SimpleVectorStore) vectorStore;
            simpleVectorStore.save(new File("e-server\\chat-pdf.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}