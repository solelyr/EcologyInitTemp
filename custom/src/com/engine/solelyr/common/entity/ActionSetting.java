package com.engine.solelyr.common.entity;

import com.engine.solelyr.common.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @DESCRIPTION: 自定义接口设置表
 * @USER: solelyr
 * @DATE: 2026/1/11 下午4:42
 */
@TableName("ActionSetting")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionSetting {

    /** 主键 */
    private Long id;

    /** 接口动作名称 */
    private String actionName;

    /** 接口动作类文件 */
    private String actionClass;

    /** 接口类型名称 */
    private String typeName;

    /** 接口动作显示名称 */
    private String actionShowName;

    /** 创建日期 */
    private String createDate;

    /** 创建时间 */
    private String createTime;

    /** 修改日期 */
    private String modifyDate;

    /** 修改时间 */
    private String modifyTime;

    /** Java 代码 */
    private String javaCode;
}
