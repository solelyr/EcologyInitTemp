package com.engine.solelyr.common.entity;

import com.engine.solelyr.common.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @DESCRIPTION: 流程action配置表
 * @USER: solelyr
 * @DATE: 2026/1/11 下午4:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("workflowactionset")
public class WorkflowActionSet {
    /** 退回时触发 */
    private Integer drawbackFlag;

    /** ID */
    private Long id;

    /** action 名称 */
    private String actionName;

    /** 流程 ID */
    private Long workflowId;

    /** 节点 ID */
    private Long nodeId;

    /** 出口 ID */
    private Long nodeLinkId;

    /** 节点前后判断 */
    private Integer isPreOperator;

    /** action 顺序 */
    private Integer actionOrder;

    /** 接口类型 */
    private Integer interfaceType;

    /** 类型名称 */
    private String typeName;

    /** 接口 ID */
    private String interfaceId;

    /** 是否启用 */
    private Integer isUsed;
}
