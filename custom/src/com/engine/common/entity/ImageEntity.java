package com.engine.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @DESCRIPTION:
 * @USER: solelyr
 * @DATE: 2025/9/23 下午1:04
 */
@Data
@AllArgsConstructor
public class ImageEntity {
    /**
     * 文档ID
     */
    private int docId;
    /**
     * 附件ID
     */
    private int imageFileId;
    /**
     * 附件名称
     */
    private String imageFileName;
    /**
     * 附件存储路径
     */
    private String imageFilePath;
}
