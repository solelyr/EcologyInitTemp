package com.engine.solelyr.common.dao;


import com.engine.solelyr.common.entity.ActionSetting;
import com.engine.solelyr.common.service.BaseQuery;

/**
 * @DESCRIPTION: 自定义接口设置表 sql查询工具
 * @USER: solelyr
 * @DATE: 2026/1/11 下午4:43
 */
public class ActionSettingDao implements BaseQuery<ActionSetting> {
    public ActionSetting selectByName(String name) {
        return this.selectOne("actionname = ?",name);
    }
}
