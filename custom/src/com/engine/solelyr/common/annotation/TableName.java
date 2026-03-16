package com.engine.solelyr.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @DESCRIPTION: 用于自定义表单的实体对象上注解数据库表名
 * @USER: solelyr
 * @DATE: 2025/12/12 上午11:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableName {
    String value();
}
