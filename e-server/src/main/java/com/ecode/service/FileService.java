package com.ecode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecode.entity.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 竹林听雨
 * @since 2025-12-27
 */
public interface FileService extends IService<File> {
    /**
     * 上传文件到服务器
     *
     * @param file 需要上传的文件
     * @param path 文件路径,开头不加/
     * @return 文件在服务器上的访问路径，上传失败时返回null
     */
    String uploadFile(MultipartFile file, String path);

    /**
     * 通过objectName删除文件
     * @param objectName 文件路径+名称
     */
    void deleteFileByObjectName(String objectName);

    /**
     * 上传文件到服务器
     *
     * @param file 需要上传的文件
     * @return 文件在服务器上的访问路径，上传失败时返回null
     */
    String uploadFile(MultipartFile file);

}
