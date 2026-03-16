package com.engine.solelyr.common.service;

import com.engine.solelyr.common.annotation.ActionName;
import com.engine.solelyr.common.utils.Util;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @DESCRIPTION: 自定义改造的流程抽象对象，统一处理异常和日志。
 * @USER: solelyr
 * @DATE: 2025/11/24 11:22:47
 */
public abstract class WorkflowAction implements Action {
    BaseBean log = new BaseBean();
    
    @Override
    public String execute(RequestInfo requestInfo){
        try{
            log.writeLog("自定义流程接口====>start"+requestInfo.getRequestid());
            if(!doExecute(requestInfo)) return FAILURE_AND_CONTINUE;
            log.writeLog("自定义流程接口====>end");
            return Action.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            String content = Util.null2String(requestInfo.getRequestManager().getMessagecontent());
            String errorMsg = getActionName(this.getClass());

            if(e.getCause() != null ){
                // 属于OA的自义定异常
                errorMsg += e.getCause().getMessage();
            }else{
                errorMsg += e.getMessage();
            }

            requestInfo.getRequestManager().setMessagecontent(errorMsg+"<br>"+content);
//            errorLog.error("自定义流程接口执行发生异常====>" + e.getMessage() + System.lineSeparator() + "    " + e);
//            log.error("自定义流程接口执行发生异常！====>" + e.getMessage() + System.lineSeparator() +  "    " + e);
            log.writeLog("自定义流程接口====>end,异常");
            return Action.FAILURE_AND_CONTINUE;
        }

    }

    private String getActionName(Class<?> clazz){
        ActionName actionName = clazz.getAnnotation(ActionName.class);
        if (actionName != null) return actionName.value()+"===>";
        return "";
    }

    public abstract Boolean doExecute(RequestInfo requestInfo);
}
