package com.maybe.sys.common.exception;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonData handle(Exception e){
        if (e instanceof SixException) {
            SixException me = (SixException) e;
            return JsonData.error(me.getCode(), me.getMessage());
        } else {
            e.printStackTrace();
            log.error(e.getMessage());
            return JsonData.error(ResultEnum.EXCEPTION.getCode(), e.getMessage());
        }
    }
}
