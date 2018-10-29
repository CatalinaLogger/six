package com.maybe.sys.common.config;

/**
 * @author jin
 * @description:
 * @date 2018/4/25
 */
public enum SocketEnum {

    CONNECT_SUCCESS("success", "连接成功"),
    ONLINE_NOTICE("online", "上线通知"),
    OFFLINE_NOTICE("offline", "离线通知");

    private String code;
    private String msg;

    SocketEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
