package com.engine.solelyr.common.modeExpand;

import com.engine.solelyr.common.dao.WorkflowActionSetDao;
import com.engine.solelyr.common.entity.WorkflowActionSet;
import com.engine.solelyr.common.utils.RequestInfoUtil;
import com.engine.solelyr.common.utils.WorkflowUtil;
import weaver.conn.RecordSet;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCodeNew;
import weaver.general.BaseBean;
import weaver.general.StaticObj;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @DESCRIPTION: 手动执行指定流程指定节点的action
 * @USER: solelyr
 * @DATE: 2026/1/11 下午4:25
 */
public class ModeWorkflowAction extends AbstractModeExpandJavaCodeNew {
    /**
     * 执行模块扩展动作
     * @param param
     *  param包含(但不限于)以下数据
     *  user 当前用户
     *  importtype 导入方式(仅在批量导入的接口动作会传输) 1 追加，2覆盖,3更新，获取方式(int)param.get("importtype")
     *  导入链接中拼接的特殊参数(仅在批量导入的接口动作会传输)，比如a=1，可通过param.get("a")获取参数值
     *  页面链接拼接的参数，比如b=2,可以通过param.get("b")来获取参数
     * @return
     */
    BaseBean log = new BaseBean();
    public Map<String, String> doModeExpand(Map<String, Object> param) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            User user = (User)param.get("user");
            int billid = -1;//数据id
            int modeid = -1;//模块id
            RequestInfo requestInfo = (RequestInfo)param.get("RequestInfo");
            if(requestInfo!=null){
                billid = Util.getIntValue(requestInfo.getRequestid());
                modeid = Util.getIntValue(requestInfo.getWorkflowid());
                if(billid>0&&modeid>0){
                    //------请在下面编写业务逻辑代码------
                    Map<String,Object> mainData = WorkflowUtil.getMainData(requestInfo);
                    String zdlc = Util.null2String(mainData.get("zdlc")) ; // 指定的流程
                    String jkdz = Util.null2String(mainData.get("jkdz")) ; // 接口动作
                    WorkflowActionSet workflowActionSet = new WorkflowActionSetDao().selectById(jkdz);
//                    ActionSetting actionSetting = new ActionSettingDao().selectByName(workflowActionSet.getInterfaceId());
                    Action action = (Action)StaticObj.getServiceByFullname("action."+workflowActionSet.getInterfaceId(), Action.class);
                    RequestInfoUtil requestInfoUtil = new RequestInfoUtil();
                    RequestInfo reqInfo = requestInfoUtil.getRequestInfo(zdlc);
                    String s = action.execute(reqInfo);
                    String message = Util.null2String(reqInfo.getRequestManager().getMessagecontent());
                    String sql = "update uf_lczdyjk set zxjg = ?,xxms = ? where id = ?";
                    RecordSet rs = new RecordSet();
                    rs.executeUpdate(sql,s,message,billid);
                    log.writeLog("手动执行指定接口执行===>");
                    log.writeLog("流程id："+zdlc+"，接口动作："+jkdz);
                    log.writeLog("执行结果："+("0".equals(s) ? "失败" : "成功") + "，描述信息："+message);
                }
            }
        } catch (Exception e) {
            result.put("errmsg","自定义出错信息");
            result.put("flag", "false");
        }
        return result;
    }

}