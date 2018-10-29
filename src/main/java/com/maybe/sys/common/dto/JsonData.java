package com.maybe.sys.common.dto;

import com.maybe.sys.common.config.ResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jin
 * @description: 自定义restful接口
 * @date 2018/4/25
 */
@ApiModel(description = "统一数据返回格式")
@Data
public class JsonData {
    @ApiModelProperty(notes = "状态码")
    private Integer code;
    @ApiModelProperty(notes = "提示消息")
    private String msg;
    @ApiModelProperty(notes = "返回数据")
    private Object data;

    public static JsonData success() {
        JsonData jd = new JsonData();
        jd.code = ResultEnum.SUCCESS.getCode();
        jd.msg = ResultEnum.SUCCESS.getMsg();
        return jd;
    }

    public static JsonData successOperate() {
        JsonData jd = new JsonData();
        jd.code = ResultEnum.SUCCESS_OPERATE.getCode();
        jd.msg = ResultEnum.SUCCESS_OPERATE.getMsg();
        return jd;
    }

    public static JsonData success(Object obj) {
        JsonData jd = new JsonData();
        jd.code = ResultEnum.SUCCESS.getCode();
        jd.msg = ResultEnum.SUCCESS.getMsg();
        jd.data = obj;
        return jd;
    }

    public static JsonData success(String msg, Object obj) {
        JsonData jd = new JsonData();
        jd.code = ResultEnum.SUCCESS.getCode();
        jd.msg = msg;
        jd.data = obj;
        return jd;
    }

    public static JsonData success(Integer code, String msg, Object obj) {
        JsonData jd = new JsonData();
        jd.code = code;
        jd.msg = msg;
        jd.data = obj;
        return jd;
    }

    public static JsonData error() {
        JsonData jd = new JsonData();
        jd.code = ResultEnum.ERROR.getCode();
        jd.msg = ResultEnum.ERROR.getMsg();
        return jd;
    }

    public static JsonData error(String msg) {
        JsonData jd = new JsonData();
        jd.code = ResultEnum.ERROR.getCode();
        jd.msg = msg;
        return jd;
    }

    public static JsonData error(Integer code, String msg) {
        JsonData jd = new JsonData();
        jd.code = code;
        jd.msg = msg;
        return jd;
    }

}
