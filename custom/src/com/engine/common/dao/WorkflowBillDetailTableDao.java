package com.engine.common.dao;

import com.engine.common.entity.WorkflowBillDetailTable;
import com.engine.common.service.BaseQuery;
import com.engine.common.utils.Util;
import weaver.conn.RecordSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @DESCRIPTION:
 * @USER: solelyr
 * @DATE: 2025/11/22 下午9:27
 */
public class WorkflowBillDetailTableDao implements BaseQuery<WorkflowBillDetailTable> {
    public List<WorkflowBillDetailTable> selectByBillId(int billId) {
        List<WorkflowBillDetailTable> list = new ArrayList<>();
        String sql = "select * from " + tableName() + " where billId = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,billId);
        while (rs.next()) {
            Map<String,Object> map = new HashMap<>();
            for (String col:rs.getColumnName()){
                col = col.toLowerCase();
                map.put(col,rs.getString(col));
            }
            list.add(Util.parseMapObject(map,WorkflowBillDetailTable.class));
        }
        return list;
    }
}
