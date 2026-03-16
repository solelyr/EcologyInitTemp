package com.engine.solelyr.common.entity;


import com.engine.solelyr.common.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @DESCRIPTION:
 * @TABLENAME: Workflow_billdetailtable
 * @USER: solelyr
 * @DATE: 2025/11/22 下午9:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("Workflow_billdetailtable")
public class WorkflowBillDetailTable {
    /** ID */
    private Integer id;

    /** 单号id */
    private Integer billId;

    /** 表名 */
    private String tableName;

    /** 标题 */
    private String title;

    /** 排序id */
    private Integer orderId;
}
