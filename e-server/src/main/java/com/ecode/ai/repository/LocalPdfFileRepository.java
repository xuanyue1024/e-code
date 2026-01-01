package com.ecode.ai.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ecode.entity.Class;
import com.ecode.entity.po.RepositoryFile;
import com.ecode.mapper.ClassMapper;
import com.ecode.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalPdfFileRepository implements FileRepository {

    private final FileService fileService;

    private final ClassMapper classMapper;

    private final VectorStore vectorStore;


    @Override
    public boolean save(Integer classId, MultipartFile file) {
        deleteFile(classId);

        String filename = file.getOriginalFilename();
        String url = fileService.uploadFile(file);

        if (url == null){
            return false;
        }
        // 3.保存映射关系
        classMapper.updateById(Class.builder()
                .id(classId)
                .repositoryFile(
                        RepositoryFile.builder()
                                .createTime(LocalDateTime.now())
                                .name(filename)
                                .url(url)
                                .build()
                ).build());
        return true;
    }

    @Override
    public RepositoryFile getFile(Integer classId) {
        RepositoryFile repositoryFile = classMapper.selectById(classId).getRepositoryFile();
        log.info("getFile: {}", repositoryFile);
        return repositoryFile;
    }

    @Override
    public void deleteFile(Integer classId) {
        Class c = classMapper.selectById(classId);
        RepositoryFile repositoryFile = c.getRepositoryFile();
        if (repositoryFile != null){
            //删除远程文件
            fileService.deleteFileByObjectName(repositoryFile.getUrl());

            //创建过滤条件
            Filter.Expression expr = new FilterExpressionBuilder()
                    .eq("classId", classId)
                    .build();
            vectorStore.delete(expr);

            //删除数据库内容
            classMapper.update(
                    new LambdaUpdateWrapper<Class>().eq(Class::getId, classId)
                    .set(Class::getRepositoryFile, null)
            );
        }
    }
}