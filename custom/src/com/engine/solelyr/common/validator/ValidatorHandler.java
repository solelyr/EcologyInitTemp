package com.engine.solelyr.common.validator;


/**
 * @DESCRIPTION: 责任链-校验处理器 Handler
 * @USER: solelyr
 * @DATE: 2025/12/1 11:33:35
 */
public interface ValidatorHandler<T> {
    /**
     * 执行校验
     * @param context 校验对象
     */
    void validate(T context);
}
