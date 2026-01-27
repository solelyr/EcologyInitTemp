package com.engine.common.entity;

import com.engine.common.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @DESCRIPTION: 工作流单据字段表
 * @TABLENAME: workflow_billfield
 * @USER: solelyr
 * @DATE: 2025/11/12 23:37:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("workflow_billfield")
public class WorkflowBillField {

    /** ID */
    private Integer id;

    /** 单据ID */
    private Integer billId;

    /** 数据库表字段名称 */
    private String fieldName;

    /** 字段显示名称 */
    private Integer fieldLabel;

    /** 单据字段数据库类型 */
    private String fieldDbType;

    /** 单据字段页面类型
     * 1：单行文本框
     * 2：多行文本框
     * 3：浏览按钮
     * 4：check框
     * 5：选择框
     */
    private Integer fieldHtmlType;

    /** 特殊字段类型（浏览按钮）*/
    private Integer type;

    /**
     * 0：主表
     * 1：从表
     */
    private Integer viewType;

    /** 明细表 */
    private String detailTable;

    /** 用户表单 */
    private String fromUser;

    /** 文本高度 */
    private Integer textHeight;

    /** 显示顺序 */
    private Double dspOrder;

    /** 子字段ID */
    private Integer childFieldId;

    /** 图片高度 */
    private Integer imgHeight;

    /** 图片宽度 */
    private Integer imgWidth;

    /** 位置 */
    private Integer places;

    /** 小数位数 */
    private String qfws;

    /** 文本高度_2 */
    private String textHeight2;

    /** 选择条目 */
    private Integer selectItem;

    /** 连接字段 */
    private Integer linkField;

    /** 公共选择框 */
    private String selectItemType;

    /** 公共选择框ID */
    private Integer pubChoiceId;

    /** 公共选择框子项ID */
    private Integer pubChilChoiceId;

    /** 选择框级数 */
    private Integer stateLev;

    /** 定位类型
     * 2：自动
     * 1：手动
     */
    private String locateType;

    /** 显示类型 */
    private Integer fieldShowTypes;

    /** 字段显示名称 */
    private String labelName;
}
