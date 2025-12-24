package com.ecode;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class VecTest {
    @Autowired
    VectorStore vectorStore;

    @Test
    public void get(){
        List<Document> results = this.vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("*") // ← 匹配所有文本（RediSearch 语法）
                        .topK(5)
                        .filterExpression("classId == 43")
                        .filterExpression("file_name == \"子集和.pdf\"")
                        .build()
        );
        System.out.println(results);
    }
}
