package com.engine.common.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * @DESCRIPTION: 责任链本体
 * @USER: solelyr
 * @DATE: 2025/12/1 11:33:56
 */
public class ValidatorChain<T> {
    private final List<ValidatorHandler> handlers = new ArrayList<>();

    public ValidatorChain(List<ValidatorHandler> handlers) {
        this.handlers.addAll(handlers);
    }

    public ValidatorChain addHandler(ValidatorHandler handler) {
        handlers.add(handler);
        return this;
    }

    /**
     * 按顺序执行每个校验器
     */
    public void validate(T context) {
        for (ValidatorHandler handler : handlers) {
            handler.validate(context);
        }
    }
}
