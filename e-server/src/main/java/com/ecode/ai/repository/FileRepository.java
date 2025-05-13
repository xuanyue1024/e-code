package com.ecode.ai.repository;

import com.ecode.entity.po.RepositoryFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {
    /**
     * 保存文件,还要记录chatId与文件的映射关系
     * @param classId 班级id
     * @param file 文件
     * @return 上传成功，返回true； 否则返回false
     */
    boolean save(Integer classId, MultipartFile file);

    /**
     * 根据指定的会话ID获取相应的文件。
     *
     * @param classId 班级ID
     * @return 对应于会话ID的文件，若找到则返回，否则可能返回null
     */
    RepositoryFile getFile(Integer classId);


    /**
     * 根据班级ID删除知识库文件
     *
     * @param classId 班级ID
     */
    void deleteFile(Integer classId);
}