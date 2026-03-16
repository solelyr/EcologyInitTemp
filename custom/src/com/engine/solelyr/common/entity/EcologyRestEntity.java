package com.engine.solelyr.common.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @NAME: CubeData
 * @DESCRIPTION: 封装的建模接口数据类
 * @USER: solelyr
 * @DATE: 2024/7/8 17:36
 */
public class EcologyRestEntity {
    private int userid;
    private Map<String,Object> mainTable;
    private Map<Integer,List<Map<String,Object>>> detailMap;

    private void init(int userId){
        this.userid = userId;
        this.detailMap = new HashMap<>();
        this.mainTable = new HashMap<>();
    }

    public EcologyRestEntity(int userId){
        init(userId);
    }

    public EcologyRestEntity(){
        init(1);
    }

    /**
     * 增加明细表
     * @param row，明细表序号从1开始
     * @param detailData 明细表对应的数据数组
     * @return
     */
    public EcologyRestEntity addDetail(int row, List detailData){
        if(!this.detailMap.containsKey(row)) this.detailMap.put(row,new ArrayList<>());
        detailData.forEach(item -> this.detailMap.get(row).add(JSONObject.parseObject(JSONObject.toJSONString(item), Map.class)));
        return this;
    }

    public EcologyRestEntity setMainTable(Object mainTable){
        this.mainTable = JSONObject.parseObject(JSONObject.toJSONString(mainTable), Map.class);
        return this;
    }

    public void put(String key, Object value){
       this.mainTable.put(key,value);
    }

    public void put(Map<String,Object> mainTable){
        if(this.mainTable == null) this.mainTable = new HashMap<>();
        this.mainTable.putAll(mainTable);
    }

    public String get(String key){
        return (this.mainTable.get(key)).toString();
    }

    public void clearDetailData(){
        this.detailMap.clear();
    }

    public Map<String, Object> getMainTable() {
        return mainTable;
    }

    public Map<Integer, List<Map<String, Object>>> getDetailMap() {
        return detailMap;
    }

    public int getUserid() {
        return userid;
    }

    public static List<EcologyRestEntity> list2EcRestData(List<? extends Object> infoList){
        return infoList.stream().map(e-> new EcologyRestEntity().setMainTable(e)).collect(Collectors.toList());
    }
}
