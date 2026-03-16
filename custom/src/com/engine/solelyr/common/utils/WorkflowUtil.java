package com.engine.solelyr.common.utils;

import com.alibaba.fastjson.JSON;

import com.engine.solelyr.common.dao.WorkflowBillDao;
import com.engine.solelyr.common.dao.WorkflowBillDetailTableDao;
import com.engine.solelyr.common.entity.WorkflowBill;
import com.engine.solelyr.common.entity.WorkflowBillDetailTable;
import com.engine.solelyr.common.entity.WorkflowRepeatField;
import com.engine.solelyr.common.validator.ALRuntimeException;
import com.engine.workflow.entity.publicApi.WorkflowDetailTableInfoEntity;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.hrm.attendance.dao.WorkflowBaseDao;
import weaver.hrm.attendance.dao.WorkflowRequestbaseDao;
import weaver.hrm.attendance.domain.WorkflowBase;
import weaver.hrm.attendance.domain.WorkflowRequestbase;
import weaver.soa.workflow.request.*;
import weaver.workflow.webservices.WorkflowRequestTableField;
import weaver.workflow.webservices.WorkflowRequestTableRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @DESCRIPTION: 流程接口的工具类
 * @USER: solelyr
 * @DATE: 2025/9/23 上午11:03
 */
public class WorkflowUtil {
    /**
     * 获取主表数据
     * @param requestInfo 流程基本信息
     * @return
     */
    public static Map<String, Object> getMainData(RequestInfo requestInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("id","".equals(Util.null2String(requestInfo.getRequestManager())) ? Util.null2String(requestInfo.getRequestid()) : Util.null2String(requestInfo.getRequestManager().getBillid()));
        map.put("requestid",Util.null2String(requestInfo.getRequestid()));
        for (Property property : requestInfo.getMainTableInfo().getProperty()) map.put( property.getName().toLowerCase(), property.getValue());
        return map;
    }

    /**
     * 获取明细表数据 从0开始代表明细表1
     * @param requestInfo 流程基本信息
     * @param detailNum 明细表序号 从0开始
     * @return
     */
    public static List<Map<String, Object>> getDetailData(RequestInfo requestInfo, int detailNum) {
        Map<String, Object> map;
        List<Map<String, Object>> list = new ArrayList<>();
        DetailTable[] detailtable = requestInfo.getDetailTableInfo().getDetailTable();
        if (detailtable.length > 0) {
            DetailTable dt = detailtable[detailNum];
            Row[] s = dt.getRow();
            for (int j = 0; j < s.length; j++) {
                map = new HashMap<>();
                Row r = s[j];
                map.put("id",r.getId());
                for (Cell cell : r.getCell()) map.put(cell.getName().toLowerCase(),cell.getValue());
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 获取明细表数据 从0开始代表明细表1
     * @param requestInfo 流程基本信息
     * @param detailTableName 明细表序号
     * @return
     */
    public static List<Map<String, Object>> getDetailDataByTableName(RequestInfo requestInfo, String detailTableName) {
        DetailTable[] detailtable = requestInfo.getDetailTableInfo().getDetailTable();
        if(detailtable == null || detailtable.length == 0) throw new ALRuntimeException("当前明细表"+detailTableName+"未填写内容，请先增加明细数据！");
        for (int i = 0; i < detailtable.length; i++) {
            DetailTable dt = detailtable[i];
            if (detailTableName.equals(Util.null2String(dt.getTableDBName()))) return getDetailData(requestInfo,i);
        }
        return new ArrayList<>();
//        return list;
    }

    /**
     * 通过sql查询明细表数据。
     * @param requestInfo 流程信息
     * @param detailNum 明细表序号，从0开始
     * @return
     */
    public static List<Map<String, Object>> getDetailDataBySql(RequestInfo requestInfo, int detailNum) {
        String tableName = requestInfo.getRequestManager().getBillTableName();
        String sql = "select * from " + tableName + "_dt" + (detailNum+1) + " where mainId = ? ";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,requestInfo.getRequestManager().getBillid());
        List<Map<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            for(String col:rs.getColumnName()){
                col = col.toLowerCase();
                map.put(col,Util.null2String(rs.getString(col)));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 获取主表数据直接转换成实体对象
     * @param requestInfo 流程基本信息
     * @return
     */
    public static <T> T parseMainData(RequestInfo requestInfo, Class<T> cls){
        return Util.parseMapObject(getMainData(requestInfo), cls);
    }

    /**
     * 获取明细表数据,并直接转换成实体对象 从0开始代表明细表1
     * @param requestInfo
     * @param tableNum
     * @param cls
     * @return
     * @param <T>
     */
    public static <T> List<T> parseDetailData(RequestInfo requestInfo,int tableNum, Class<T> cls){
        return Util.parseListObject(getDetailData(requestInfo, tableNum), cls);
    }


    /**
     * 更新明细表序号
     * @param requestInfo 流程信息
     * @param detailTableNum 明细序号
     * @param startNum 起始序号
     * @param increment 每次的增量
     * @return
     */
    public static Boolean createDetailSerial(RequestInfo requestInfo,int detailTableNum,String detailField,int startNum,int increment){
        RecordSetTrans rst = new RecordSetTrans(); // 使用事务类
        try{
            // 申请类别等于新建，需要重新进行明细表序号编号
            List<Map<String,Object>> list = WorkflowUtil.getDetailData(requestInfo,detailTableNum);
            String tableName = requestInfo.getRequestManager().getBillTableName();

            rst.setAutoCommit(false);
            for (Map<String,Object> map : list) {
                String sql = "update " + tableName +"_dt"+(detailTableNum+1)+" set "+detailField+" = " + startNum + " where id = " + map.get("id");
                rst.executeUpdate(sql);
                startNum = startNum + increment;
            }
            // 提交事务
            rst.commit();
        }catch (Exception e) {
            requestInfo.getRequestManager().setMessageid("10000");
            requestInfo.getRequestManager().setMessagecontent("生成SAP序列号发生错误，请联系管理员！"+e.getMessage());
            e.printStackTrace();
            rst.rollback(); // 事务回滚
            return false;
        }
        return true;
    }


    /**
     * 通过表单id，获取需要校验的字段类别
     * @param formId
     * @return
     */
    public static List<WorkflowRepeatField> getRepeatFieldList(int formId){
        String sql = "select a.tableName,a.formId,b.fieldId,c.fieldname,d.labelname from uf_mxcfjy a inner join uf_mxcfjy_dt1 b on a.id = b.mainid inner join workflow_billfield c on b.fieldId = c.id and a.tableName = c.detailtable inner join htmllabelinfo d on c.fieldlabel = d.indexid and d.languageid = 7 where a.formId = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,formId);
        List<WorkflowRepeatField> list = new ArrayList<>();
        while(rs.next()){
            Map<String,Object> map = new HashMap<>();
            for (String col:rs.getColumnName()){
                col = col.toLowerCase();
                map.put(col,rs.getString(col));
            }
            list.add(JSON.parseObject(JSON.toJSONString(map),WorkflowRepeatField.class));
        }
        return list;
    }

    /**
     * 通过流程id，查询指定的表单数据
     * @param requestId
     * @return
     */
    public static Map<String,Object> selectMainDataByRequestId(String requestId){
        String tableName = selectTableNameByRequestId(requestId); // 查询主表表名
        return selectMainData(tableName,requestId);
    }

    public static String selectTableNameByRequestId(String requestId){
        WorkflowRequestbase base = new WorkflowRequestbaseDao().get(requestId);
        WorkflowBase workflow = new WorkflowBaseDao().get(base.getWorkflowid());
        WorkflowBill bill = new WorkflowBillDao().selectById(Util.null2String(workflow.getFormid()));
        return bill.getTableName(); // 查询主表表名
    }

    /**
     * 通过流程id，主表id，查询明细表单数据
     * @param requestId
     * @return Map中的key从0开始
     */
    public static Map<Integer,List<Map<String,Object>>> selectDetailDataByRequestId(String requestId){
        WorkflowRequestbase base = new WorkflowRequestbaseDao().get(requestId);
        WorkflowBase workflow = new WorkflowBaseDao().get(base.getWorkflowid());
        List<WorkflowBillDetailTable> detailTables = new WorkflowBillDetailTableDao().selectByBillId(workflow.getFormid());
        Map<String,Object> mainData = selectMainDataByRequestId(requestId);
        String mainId = Util.null2String(mainData.get("id"));
        Map<Integer,List<Map<String,Object>>> map = new HashMap<>();
        for(WorkflowBillDetailTable detailTable:detailTables){
            map.put(detailTable.getOrderId()-1,selectDetailData(detailTable.getTableName(),mainId));
        }
        return map;
    }

    private static Map<String,Object> selectMainData(String tableName,String requestId){
        String sql = "select * from "+tableName + " where requestId = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,requestId);
        Map<String,Object> map = new HashMap<>();
        if(rs.next()){
            for (String col:rs.getColumnName()){
                col = col.toLowerCase();
                map.put(col,rs.getString(col));
            }
        }
        return map;
    }

    private static List<Map<String,Object>> selectDetailData(String tableName,String mainId){
        String sql = "select * from "+tableName + " where mainId = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,mainId);
        List<Map<String,Object>> list = new ArrayList<>();
        while (rs.next()){
            Map<String,Object> map = new HashMap<>();
            for (String col:rs.getColumnName()){
                col = col.toLowerCase();
                map.put(col,rs.getString(col));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 获取流程编号
     * @param requestId
     * @return
     */
    public static String selectRequestCode(String requestId){
        String sql = "SELECT requestmark from workflow_requestbase where requestid = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,requestId);
        if(rs.next()) return Util.null2String(rs.getString("requestmark"));
        return "";
    }

    /**
     * 通过表单formId，查询流程当前启用的workflowId
     * @param formId
     * @return
     */
    public static String selectWorkflowIdByFormId(String formId){
        String sql = "select isnull(activeVersionID,id) id from workflow_base where formid= ? group by isnull(activeVersionID,id)";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,formId);
        if(rs.next()){
            return Util.null2String(rs.getString("id"));
        }
        return "";
    }

    /**
     * Map转成OA流程需要的主表数据格式,没有设置附件的转换
     * @param dataMap
     * @return 返回mainData对应的list
     */
    public static List<WorkflowRequestTableField> map2WorkflowMain(Map<String,Object> dataMap){
        List<WorkflowRequestTableField> list = new ArrayList<>();
        for(String key:dataMap.keySet()){
            WorkflowRequestTableField field = new WorkflowRequestTableField();
            field.setFieldName(key);
            field.setFieldValue(Util.null2String(dataMap.get(key)));
            list.add(field);
        }
        return list;
    }

    /**
     * Map转成OA流程需要的明细表数据，不包含附件字段
     * @param dataList
     * @return 返回workflowRequestTableRecords对应的list
     */
    public static WorkflowDetailTableInfoEntity map2WorkflowDetail(String tableName,int index,List<Map<String,Object>> dataList){
        WorkflowRequestTableRecord[] list = new WorkflowRequestTableRecord[dataList.size()];
        for(int i = 0; i < dataList.size(); i++){
            Map<String,Object> dataMap = dataList.get(i);
            WorkflowRequestTableRecord record = new WorkflowRequestTableRecord();
            List<WorkflowRequestTableField> fields = map2WorkflowMain(dataMap);
            record.setRecordOrder(0);
            record.setWorkflowRequestTableFields(fields.toArray(new WorkflowRequestTableField[fields.size()]) );
            list[i] = record ;
        }
        WorkflowDetailTableInfoEntity entity = new WorkflowDetailTableInfoEntity();
        entity.setDeleteAll(true);
        entity.setTableDBName(tableName+"_dt"+index);
        entity.setWorkflowRequestTableRecords(list);
        return entity;
    }
}
