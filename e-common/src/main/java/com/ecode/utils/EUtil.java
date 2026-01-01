package com.ecode.utils;

/**
 * 一些杂项工具类
 */
public class EUtil {
    /**
     * MYSQL 判断是否唯一索引冲突
     * @param e 异常
     * @return
     */
    public static boolean isDuplicateKeyException(Exception e) {
        // MySQL: Duplicate entry 'xxx' for key 'uk_hash_size'
        return e.getMessage() != null &&
                (e.getMessage().contains("Duplicate entry"));
    }

    /**
     * 获取文件扩展名
     * @param filename
     * @return
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) return "";
        return filename.substring(filename.lastIndexOf('.')).toLowerCase();
    }
}
