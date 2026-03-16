package com.engine.solelyr.common.utils;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.*;
import weaver.workflow.request.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @DESCRIPTION: 生成流程基本信息，用于自定义执行指定节点的接口使用
 * @USER: solelyr
 * @DATE: 2026/1/10 下午6:15
 */
public class RequestInfoUtil {

    public RequestInfo getRequestInfo(String requestId) {
        try {
            {
                int formid = 0;
                int workflowId = 0;
                int userId = 0;
                if ("".equals(requestId)) {
                    return null;
                } else {
                    String sql = "";
                    RecordSet rs = new RecordSet();
                    RecordSet rs1 = new RecordSet();
                    String tableName = "";
                    String detailKeyField = "";
                    int createrId = 0;
                    new HashMap();
                    sql = "SELECT a.*,b.formid,c.tablename,c.detailkeyfield from workflow_requestbase a inner join workflow_base b on a.workflowid = b.id inner join workflow_bill c on b.formid = c.id where a.requestid = ?";
                    rs.executeQuery(sql,requestId);

                    while(rs.next()) {
                        tableName = Util.null2String(rs.getString("tablename"));
                        detailKeyField = Util.null2String(rs.getString("detailKeyfield"));
                        formid = rs.getInt("formid");
                        createrId = rs.getInt("creater");
                        workflowId = rs.getInt("workflowid");
                    }

                    if (detailKeyField.equals("")) {
                        detailKeyField = "mainid";
                    }

                    RequestInfo requestInfo = new RequestInfo();
                    RequestManager manager = new RequestManager();
                    manager.setWorkflowid(workflowId);
                    manager.setFormid(formid);
                    manager.setRequestid(Util.getIntValue(requestId));
                    manager.setBilltablename(tableName);
                    manager.setCreater(createrId);

                    requestInfo.setRequestManager(manager);
                    requestInfo.setWorkflowid(Util.null2String(workflowId));
                    requestInfo.setRequestid(requestId);
                    requestInfo.setLastoperator(Util.null2String(userId));
                    requestInfo.setCreatorid(Util.null2String(createrId));
                    rs.isReturnDecryptData(true);
                    sql = "select * from " + tableName + " where requestId = ?";
                    rs.executeQuery(sql, requestId);

                    String fieldname_value;
                    while(rs.next()) {
                        rs1.executeQuery("select * from workflow_billfield where viewtype=0 and billid=" + formid);
                        MainTableInfo mainTableInfo = new MainTableInfo();
                        ArrayList list = new ArrayList();

                        while(rs1.next()) {
                            Property property = new Property();
                            String fieldName = Util.null2String(rs1.getString("fieldname"));
                            fieldname_value = Util.null2String(rs.getString(fieldName));
                            String fieldDbType = Util.null2String(rs1.getString("fielddbtype"));
                            property.setName(fieldName);
                            property.setValue(fieldname_value);
                            property.setType(fieldDbType);
                            list.add(property);
                        }

                        Property[] properties = new Property[list.size()];
                        list.toArray(properties);
                        mainTableInfo.setProperty(properties);
                        requestInfo.setMainTableInfo(mainTableInfo);
                    }

                    DetailTableInfo detailTableInfo = new DetailTableInfo();
                    RecordSet rs2 = new RecordSet();
                    sql = "select tablename as detailtablename from workflow_billdetailtable where billid = ? order by orderid";
                    rs2.executeQuery(sql,formid);
                    byte i = 0;
                    ArrayList detailTableList = new ArrayList();

                    while(rs2.next()) {
                        fieldname_value = rs2.getString("detailtablename");
                        DetailTable detailTable = new DetailTable();
                        String dt = fieldname_value.substring(fieldname_value.lastIndexOf("dt") + 2);
                        if (!"".equals(dt)) {
                            detailTable.setId(dt);
                        } else {
                            detailTable.setId("" + (i + 1));
                        }

                        detailTable.setTableDBName(fieldname_value);
                        sql = "select b.* from " + tableName + " a," + fieldname_value + " b where a.id=b." + detailKeyField + " and a.requestId = " + requestId + " order by b.id";
                        rs.executeQuery(sql);
                        sql = "select * from workflow_billfield where billid=" + formid + " and viewtype='1' and detailtable='" + fieldname_value + "'";
                        rs1.executeQuery(sql);
                        ArrayList detailList = new ArrayList();

                        while(rs.next()) {
                            Row row = new Row();
                            row.setId(rs.getString("id"));
                            ArrayList cellsList = new ArrayList();
                            rs1.beforFirst();

                            while(rs1.next()) {
                                Cell cell = new Cell();
                                String fieldName = Util.null2String(rs1.getString("fieldname"));
                                String value = Util.null2String(rs.getString(fieldname_value, fieldName));
                                String fieldDbType = Util.null2String(rs1.getString("fielddbtype"));
                                cell.setName(fieldName);
                                cell.setValue(value);
                                cell.setType(fieldDbType);
                                cellsList.add(cell);
                            }

                            Cell[] cells = new Cell[cellsList.size()];
                            cellsList.toArray(cells);
                            row.setCell(cells);
                            detailList.add(row);
                        }

                        Row[] detailRow = new Row[detailList.size()];
                        detailList.toArray(detailRow);
                        detailTable.setRow(detailRow);
                        detailTableList.add(detailTable);
                    }

                    DetailTable[] detailTables = new DetailTable[detailTableList.size()];
                    detailTableList.toArray(detailTables);
                    detailTableInfo.setDetailTable(detailTables);
                    requestInfo.setDetailTableInfo(detailTableInfo);
                    return requestInfo;
                }
            }
        } catch (Exception e) {

            return null;
        }
    }
}
