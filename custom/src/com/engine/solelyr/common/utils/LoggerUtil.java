package com.engine.solelyr.common.utils;

import weaver.integration.logging.Log4JLogger;
import weaver.integration.logging.Logger;

/**
 * @DESCRIPTION: 自定义输出日志文件
 * @USER: solelyr
 * @DATE: 2025/9/24 下午3:45
 */
public class LoggerUtil {

    public LoggerUtil() {
    }

    public static Logger getLogger(String loggerName, String className) {
        if ("".equals(loggerName)) {
            loggerName = "cusLog";
        }

        Log4JLogger logger = new Log4JLogger();
        logger.setClassname(className);
        logger.init(loggerName);
        return logger;
    }

    public static Logger getLogger(Class clazz) {
        return getLogger("", clazz.getCanonicalName());
    }

    public static Logger getLogger(String className) {
        return getLogger("", className);
    }

    public static Logger getLogger() {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        return getLogger("", className);
    }
}
