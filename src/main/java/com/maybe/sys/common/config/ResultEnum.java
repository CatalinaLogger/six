package com.maybe.sys.common.config;

/**
 * @author jin
 * @description:
 * @date 2018/4/25
 */
public enum ResultEnum {

    EXCEPTION(-100, "未知异常"),
    ERROR_ACCESS(-10, "验证身份失败"),
    ERROR_PARAM(-2, "请求参数异常"),
    ERROR(-1, "请求失败" ),
    SUCCESS(0, "请求成功"),
    SUCCESS_OPERATE(1, "操作成功");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
