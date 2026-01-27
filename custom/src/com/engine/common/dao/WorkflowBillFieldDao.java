package com.engine.common.dao;


import com.engine.common.entity.WorkflowBillField;
import com.engine.common.service.BaseQuery;
import com.engine.common.utils.Util;
import com.huawei.shade.com.alibaba.fastjson.JSON;
import weaver.conn.RecordSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @DESCRIPTION:
 * @USER: solelyr
 * @DATE: 2025/11/17 15:00:00
 */
public class WorkflowBillFieldDao implements BaseQuery<WorkflowBillField> {

    /**
     * 通过表单ID获取表单详细的字段列表
     * @param formId 表单id
     * @return
     */
    public List<WorkflowBillField> getMainBillFieldList(int formId){
        List<WorkflowBillField> workflowBillFieldList = new ArrayList<>();
        String sql = "select a.*,b.labelname from workflow_billfield a left join HtmlLabelInfo b on a.fieldlabel = b.indexid where b.languageid = 7 and a.billid = ? and viewtype = 0";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,formId);
        while(rs.next()){
            Map<String,String> map = new HashMap<>();
            for(String col : rs.getColumnName()){
                col = col.toLowerCase();
                map.put(col, Util.null2String(rs.getString(col)));
            }
            workflowBillFieldList.add(JSON.parseObject(JSON.toJSONString(map), WorkflowBillField.class));
        }
        return workflowBillFieldList;
    }

    /**
     * 通过主表表名+字段名查询字段信息
     * @param formId
     * @param fieldName
     * @return
     */
    public WorkflowBillField getMainBillField(int formId, String fieldName){
        String sql = "select a.*,b.labelName from workflow_billField a left join HtmlLabelInfo b on a.fieldLabel = b.indexId where b.languageid = 7 and a.billId = ? and viewType = 0 and a.fieldName = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,formId,fieldName);
        if(rs.next()){
            Map<String,String> map = new HashMap<>();
            for(String col : rs.getColumnName()){
                col = col.toLowerCase();
                map.put(col, Util.null2String(rs.getString(col)));
            }
            return JSON.parseObject(JSON.toJSONString(map), WorkflowBillField.class);
        }
        return new WorkflowBillField();
    }

    /**
     * 通过表单ID+明细表表名 查询详细的字段列表
     * @param formId 表单id
     * @param tableName 明细表数据库表名
     * @return
     */
    public List<WorkflowBillField> getDetailBillFieldList(int formId, String tableName){
        List<WorkflowBillField> workflowBillFieldList = new ArrayList<>();
        String sql = "select a.*,b.labelname from workflow_billfield a left join HtmlLabelInfo b on a.fieldlabel = b.indexid where b.languageid = 7 and a.billid = ? and viewtype = 1 and detailtable = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,formId,tableName);
        while(rs.next()){
            Map<String,String> map = new HashMap<>();
            for(String col : rs.getColumnName()){
                col = col.toLowerCase();
                map.put(col,Util.null2String(rs.getString(col)));
            }
            workflowBillFieldList.add(JSON.parseObject(JSON.toJSONString(map), WorkflowBillField.class));
        }
        return workflowBillFieldList;
    }
}
