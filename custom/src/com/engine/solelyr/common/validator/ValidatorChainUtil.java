package com.engine.solelyr.common.validator;

import java.util.List;

/**
 * @DESCRIPTION: 简化责任链的调用
 * @USER: solelyr
 * @DATE: 2025/12/1 11:37:22
 */
public class ValidatorChainUtil {

    /**
     * 静态方法的校验工具类
     * @param handlers 校验的责任链条
     * @param context 入参
     */
    public static <T> void validate(List<ValidatorHandler<T>> handlers, T context){
        ValidatorChain chain = new ValidatorChain(handlers);
        chain.validate(context);
    }
}
