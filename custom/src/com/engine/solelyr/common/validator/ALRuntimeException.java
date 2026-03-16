package com.engine.solelyr.common.validator;

/**
 * @DESCRIPTION: 自定义的异常处理
 * @USER: solelyr
 * @DATE: 2025/11/14 00:29:10
 */
public class ALRuntimeException extends RuntimeException {

    public ALRuntimeException() {
        super();
        saveLog("",null);
    }
    
    public ALRuntimeException(String message) {
        super(message);
        saveLog(message,null);
    }


    public ALRuntimeException(String message, Throwable cause) {
        super(message, cause);
        saveLog(message,cause);
    }
    
    public ALRuntimeException(Throwable cause) {
        super(cause);
        saveLog("",cause);
    }
    
    protected ALRuntimeException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        saveLog(message,cause);
    }

    private void saveLog(String message, Throwable cause){
        // 捕获并存储堆栈信息
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 提取线程和位置信息
        String threadName = Thread.currentThread().getName();
        String className = stackTrace[3].getClassName();
        String methodName = stackTrace[3].getMethodName();
        int lineNumber = stackTrace[3].getLineNumber();
//        Logger logger = AutoLinkLoggerFactory.getLogger("autoLinkErrorLog",this.getClass().getSimpleName());
//        // 打印异常信息
//        logger.error(className + "." + methodName + "() " + lineNumber + "\nmessage=====> " + (!"".equals(message) ? message : "") + (cause != null ? "\nmessage=====>"+cause : ""));
    }
}
