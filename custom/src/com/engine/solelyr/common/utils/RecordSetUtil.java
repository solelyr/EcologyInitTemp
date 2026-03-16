package com.engine.solelyr.common.utils;

import com.engine.solelyr.common.validator.ALRuntimeException;
import weaver.conn.RecordSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @DESCRIPTION: 简化一下通过sql查询的操作
 * @USER: solelyr
 * @DATE: 2025/11/24 19:22:46
 */
public class RecordSetUtil {
    public static List<Map<String,Object>> selectData(String sql,Object... args){
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            RecordSet rs = new RecordSet();
            if(args.length>0){
                rs.executeQuery(sql,args);
            }else {
                rs.executeQuery(sql);
            }
            while(rs.next()){
                Map<String,Object> map = new HashMap<>();
                for(String col:rs.getColumnName()){
                    col = col.toLowerCase();
                    map.put(col,Util.null2String(rs.getString(col)));
                }
                list.add(map);
            }
        }catch (Exception e) {
            throw new ALRuntimeException("查询失败: " + sql, e);
        }
        return list;
    }
}
