package com.engine.common.entity;

import com.engine.common.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @DESCRIPTION: OA表单基本信息
 * @TABLENAME: workflow_bill
 * @USER: solelyr
 * @DATE: 2025/11/22 下午8:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("workflow_bill")
public class WorkflowBill {

    /** 系统单据继承类 */
    private String clazz;

    /** ID */
    private Integer id;

    /** 单据显示名称 */
    private String nameLabel;

    /** 对应的主表名称 */
    private String tableName;

    /** 创建请求页面url */
    private String createPage;

    /** 管理请求页面url */
    private String managePage;

    /** 查看请求页面url */
    private String viewPage;

    /** 对应的从表名称 */
    private String detailTableName;

    /** 从表链接主表的关键字 */
    private String detailKeyField;

    /** 后台处理请求页面url */
    private String operationPage;

    /** 已有文件上传 */
    private Integer hasFileUp;

    /** 无效标志 */
    private Integer invalid;

    /** 表单描述 */
    private String formDes;

    /** 子公司id */
    private Integer subCompanyId;

    /** 显示顺序 */
    private Integer dspOrder;

    /** 子公司id3 */
    private Integer subCompanyId3;

    /** 表单模块 */
    private String from_Module_;
}
