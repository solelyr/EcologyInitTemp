package com.engine.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.api.formmode.cache.CubeInterfaceConfigComInfo;
import com.api.formmode.cache.ModeComInfo;

import com.engine.common.entity.WorkflowBillField;
import com.engine.common.entity.EcologyRestEntity;
import com.engine.common.service.EcologyRestPk;
import com.engine.common.util.ServiceUtil;
import com.engine.cube.service.CubeOutsideInterfaceService;
import com.engine.cube.service.impl.CubeOutsideInterfaceServiceImpl;
import weaver.conn.RecordSet;
import weaver.hrm.User;
import weaver.workflow.workflow.WorkflowBillComInfo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @DESCRIPTION: 建模接口封装工具 
 * @USER: solelyr
 * @DATE: 2025/11/6 下午10:56
 */
public class EcologyRestUtil {
    private static final String systemId = "ecology";
    private static final String passWord = "igiFy0xoxqA6GrBXa3vZ";
    private String interfaceConfigId = "0";
    private String modeId = "0";
    private String formId = "0";

    private CubeOutsideInterfaceService getService(User user) {
        return ServiceUtil.getService(CubeOutsideInterfaceServiceImpl.class, user);
    }

    public Map<String,Object> saveOrUpdate(EcologyRestPk pk, EcologyRestEntity data){
        return saveOrUpdate(1,pk,Arrays.asList(data));
    }

    public Map<String,Object> saveOrUpdate(EcologyRestPk pk, List<EcologyRestEntity> data){
        return saveOrUpdate(1,pk,data);
    }

    public List<Map<String,Object>> saveOrUpdateBatch(EcologyRestPk pk, List<EcologyRestEntity> data){
        return saveOrUpdateBatch(pk,data,50);
    }

    public List<Map<String,Object>> saveOrUpdateBatch(EcologyRestPk pk, List<EcologyRestEntity> dataList, int batchSize){
        List<Map<String,Object>> resultList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<EcologyRestEntity> subList = dataList.subList(i, end);
            resultList.add(saveOrUpdate(1,pk,subList));
        }
        return resultList;
    }

    public Map<String,Object> saveOrUpdate(int operator, EcologyRestPk pk, EcologyRestEntity data){
        EcologyRestUtil ecologyRestUtil = new EcologyRestUtil();
        return ecologyRestUtil.doAction(operator,pk, Arrays.asList(data));
    }

    public Map<String,Object> saveOrUpdate(int operator, EcologyRestPk pk, List<EcologyRestEntity> data){
        return doAction(operator,pk,data);
    }

    private Map<String,Object> doAction(int operator, EcologyRestPk pk, List<EcologyRestEntity> data){
        List<Map<String,Object>> list = data.stream().map(e->dataConvert(pk,e)).collect(Collectors.toList());
        User user = new User(operator);
        Map<String,Object> dataJson = new HashMap<>();
        dataJson.put("data",list); // 数据数组
        dataJson.put("header",getHeader());

        Map<String,Object> params = new HashMap();
        params.put("datajson",new JSONObject(dataJson));
        Map<String,Object> rest = getService(user).saveOrUpdateModeData(pk.getPK(), params, user);
//        String status = Util.null2String(rest.get("status"));
//        if(!"1".equals(status)) throw new ALRuntimeException("插入建模数据异常！");
        return rest;
    }


    private String getMD5Str(String plainText){
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(plainText.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("md5加密失败！");
        }
        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);
        // 如果生成数字未满32位，需要前面补0
        // 不能把变量放到循环条件，值改变之后会导致条件变化。如果生成30位 只能生成31位md5
        int tempIndex = 32 - md5code.length();
        for (int i = 0; i < tempIndex; i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    private Map<String,Object> getHeader(){
        String currentDateTime = (new Timestamp(new Date().getTime()).toString()).substring(0, 19).replace("-", "").replace(":", "").replace(" ", "");
        Map<String,Object> header = new HashMap<>();
        header.put("systemid",systemId);
        header.put("currentDateTime",currentDateTime);
        header.put("Md5",getMD5Str(systemId + passWord + currentDateTime).toLowerCase());
        return header;
    }

    private Map<String,Object> dataConvert(EcologyRestPk pk, EcologyRestEntity data){
//        this.pk = pk;
        RecordSet rs = new RecordSet();
        rs.executeQuery("select id from CubeInterfaceConfig where interfacePK = ?",pk.getPK());
        if(rs.next()){
            this.interfaceConfigId = rs.getString("id");
        }

        ModeComInfo modeComInfo = new ModeComInfo();
        CubeInterfaceConfigComInfo configComInfo = new CubeInterfaceConfigComInfo();
        this.modeId = configComInfo.getModeid(this.interfaceConfigId);
        this.formId = modeComInfo.getFormId(this.modeId);
        WorkflowBillComInfo comInfo = new WorkflowBillComInfo();
        String mainTable = comInfo.getTablename(this.formId);

        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("mainTable",setMainData(data.getMainTable(),mainTable)); // 转换完成的主表
        dataMap.put("operationinfo",setOperationInfo(data.getUserid())); // 操作者
        dataMap.putAll(setDetailData(data));
        return dataMap;
    }

    // 转换系统数据库字段大小写问题
    private Map<String,Object> setMainData(Map<String,Object> data,String mainTable){
        List<WorkflowBillField> workflowBillFieldList = selectTableFields(this.interfaceConfigId,mainTable);
        Map<String,String> sysFieldNameMap = workflowBillFieldList.stream().collect(Collectors.toMap(e->e.getFieldName().toLowerCase(), e->e.getFieldName()));// 将系统中的 fieldName 全部转换成小写key,原值为value
        Map<String,Object> newDataMap = new HashMap<>();
        for (String key : data.keySet()){
            String key_low = key.toLowerCase();
            if(sysFieldNameMap.containsKey(key_low)){
                newDataMap.put(sysFieldNameMap.get(key_low),data.get(key)); // 如果这个字段在系统中存在，则已系统中的key作为新的key
            }else {
                newDataMap.put(key,data.get(key)); // 如果系统中不存在则保留之前的key
            }
        }
        return newDataMap;
    }

    // 转换明细表字段
    private Map<String,List<Map<String,Object>>> setDetailData(EcologyRestEntity ecData){
        Map<Integer,String> detailTableMap = new HashMap<>(); // 明细表表名信息
        RecordSet rs = new RecordSet();
        rs.executeQuery("select * from workflow_billdetailtable where billid = ? order by id",this.formId);
        while (rs.next()) {
            // 明细表序号 ,明细表表名
            detailTableMap.put(rs.getInt("orderid"),rs.getString("tablename"));
        }

        Map<Integer,List<Map<String,Object>>> detailListMap = ecData.getDetailMap(); // 先获取所有的明细表
        Map<String,List<Map<String,Object>>> newDetailListMap = new HashMap<>(); // 转换后的明细数据集合
        for (int i : detailListMap.keySet()){
            List<Map<String,Object>> detailList = new ArrayList<>();
            boolean sysHas = detailTableMap.containsKey(i); // 系统中是否存在该明细表
            for (Map<String,Object> detailDataMap : detailListMap.get(i)){
                Map<String,Object> data = sysHas ? setMainData(detailDataMap,detailTableMap.get(i)) : detailDataMap;// 系统中存在则明细，则需要转换每个map的字段key
                Map<String,Object> operate = new HashMap<>();
                operate.put("action","SaveOrUpdate");

                Map<String,Object> detailMap = new HashMap<>();
                detailMap.put("operate",operate);
                detailMap.put("data",data);

                detailList.add(detailMap);
            }
            newDetailListMap.put("detail" + i,detailList);
        }
        return newDetailListMap;
    }

    private Map<String,Object> setOperationInfo(int userId){
        Map<String,Object> operationInfo = new HashMap<>();
        operationInfo.put("operator",userId);
        return operationInfo;
    }


    //查询表单字段信息
    private List<WorkflowBillField> selectTableFields(String interfaceConfigId, String tableName){
        String sql = "select b.* from CubeinterfaceConfigDetail d LEFT JOIN workflow_billfield b  on d.fieldid=b.id where d.mainid= ? and d.tablename = ? order by d.tablename, d.isPK";
        List<Map<String,Object>> list = new RecordSetUtil().selectData(sql,interfaceConfigId,tableName);
        return Util.parseListObject(list, WorkflowBillField.class);
    }
}
