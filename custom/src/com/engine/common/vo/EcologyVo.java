package com.engine.common.vo;

import lombok.Data;

/**
 * @DESCRIPTION: 自定义api接口返回的对象
 * @USER: solelyr
 * @DATE: 2025/9/21 下午9:44
 */
@Data
public class EcologyVo {
    private Integer code;
    private String errMsg;
    private Object data;

    public EcologyVo(Integer code, String errMsg, Object data) {
        this.code = code;
        this.errMsg = errMsg;
        this.data = data;
    }

    public static EcologyVo success() {
        return new EcologyVo(200,"请求成功!",null);
    }

    public static EcologyVo success(Object data) {
        return new EcologyVo(200,"请求成功!",data);
    }

    public static EcologyVo error(int code, String errorMessage) {
        if(code == 200) code = 500;
        return new EcologyVo(code,errorMessage,null);
    }

    public static EcologyVo error(String errorMessage) {
        return new EcologyVo(500,errorMessage,null);
    }

    public static EcologyVo error() {
        return new EcologyVo(500,"接口执行失败!",null);
    }

    public boolean isSuccess( ) {
        return this.code == 200;
    }
}
