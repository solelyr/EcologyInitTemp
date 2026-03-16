package com.engine.solelyr.common.service;

import com.alibaba.fastjson.JSON;
import com.engine.solelyr.common.validator.ALRuntimeException;
import com.engine.solelyr.common.annotation.Id;
import com.engine.solelyr.common.annotation.TableName;
import com.engine.solelyr.common.utils.RecordSetUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @DESCRIPTION:
 * @USER: solelyr
 * @DATE: 2025/11/16 下午11:33
 */
public interface BaseQuery<T> {

    /**
     * 通过id查询数据
     * @param idValue
     * @return
     */
    default T selectById(Object idValue) {
        return selectOne(primaryKeyColumn() + " = ?", idValue);
    }

    /**
     * 根据条件查询满足条件的第一条数据
     * @param whereSql a = ? and b = ?
     * @param args a1,b1
     * @return
     */
    default T selectOne(String whereSql, Object... args) {
        List<T> list = selectList(whereSql, args);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 返回所有数据
     * @return
     */
    default List<T> selectList() {
        String sql = "SELECT * FROM " + tableName();
        return queryList(sql);
    }

    /**
     * 根据指定条件返回数据
     * @param whereSql a = ? and b = ?
     * @param args a1,b1
     * @return
     */
    default List<T> selectList(String whereSql, Object... args) {
        String sql = "SELECT * FROM " + tableName() + (whereSql == null || whereSql.isEmpty() ? "" : " WHERE " + whereSql);
        return queryList(sql, args);
    }

    /**
     * 根据 自定义sql+条件 查询所有数据
     * @param sql select * from a where a1 = ?
     * @param args a1value
     * @return
     */
    default List<T> queryList(String sql, Object... args) {
        Class<T> clazz = entityClass();
        List<Map<String, Object>> list = RecordSetUtil.selectData(sql,args);
        List<T> result = JSON.parseArray(JSON.toJSONString(list), clazz);
        return result;
    }

    /** 自动推断泛型 T 的 Class，不需要子类实现 */
    @SuppressWarnings("unchecked")
    default Class<T> entityClass() {
        Type generic = this.getClass().getGenericInterfaces()[0];
        if (generic instanceof ParameterizedType) {
            Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
            return (Class<T>) actual;
        }
        throw new ALRuntimeException("无法从泛型中推断实体类型，请使用 BaseQuery<T>");
    }

    /** 自动从实体类注解 @ALTableName上读取表名 */
    default String tableName() {
        Class<T> clazz = entityClass();
        TableName tn = clazz.getAnnotation(TableName.class);
        if (tn == null) {
            throw new ALRuntimeException("实体类 " + clazz.getName() + " 未加 @ALTableName 注解");
        }
        return tn.value();
    }

    /** 自动从实体类注解@ALId或表单id字段上读取主键字段 */
    default String primaryKeyColumn() {
        Class<T> clazz = entityClass();

        // 1. 查找有 @Id 的字段
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                return field.getName();
            }
        }
        // 2. 否则查找名为 "id" 的字段
        try {
            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            return idField.getName();
        } catch (NoSuchFieldException e) {
            throw new ALRuntimeException("实体类中未找到 @Id 标注字段，也未找到 id 字段：" + clazz.getName());
        }
    }
}
