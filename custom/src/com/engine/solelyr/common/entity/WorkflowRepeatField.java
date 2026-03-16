package com.engine.solelyr.common.entity;

import lombok.Data;

/**
 * @DESCRIPTION: 明细数据重复校验配置字段明细，对应建模 uf_mxcfjy 表单
 * @USER: solelyr
 * @DATE: 2025/11/15 下午3:28
 */
@Data
public class WorkflowRepeatField {
    /** 主表表单id */
    private int formId;

    /** 主表表单名称 */
    private String tableName;

    /** 明细字段id */
    private String fieldId;

    /** 明细字段名称 */
    private String fieldName;

    /** 明细字段显示名称 */
    private String labelName;

}
