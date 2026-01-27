package com.engine.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @DESCRIPTION: 提供给第三方下载的文件格式
 * @USER: solelyr
 * @DATE: 2025/9/23 上午11:29
 */
@Data
@AllArgsConstructor
public class FileEntity {
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件下载地址
     */
    private String filePath;
}
