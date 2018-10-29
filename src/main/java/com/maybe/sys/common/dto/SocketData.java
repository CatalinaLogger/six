package com.maybe.sys.common.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maybe.sys.common.config.SocketEnum;
import lombok.Data;

/**
 * @author jin
 * @description: 自定义restful接口
 * @date 2018/4/25
 */
@Data
public class SocketData {
    private String code;
    private String msg;
    private Object data;

    public static String send(SocketEnum socketEnum, Object data) {
        ObjectMapper mapper = new ObjectMapper();
        SocketData socketData = new SocketData();
        socketData.code = socketEnum.getCode();
        socketData.msg = socketEnum.getMsg();
        socketData.data = data;
        try {
            return mapper.writeValueAsString(socketData);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
