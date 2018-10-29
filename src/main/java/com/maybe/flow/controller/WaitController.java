package com.maybe.flow.controller;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.model.FlowWait;
import com.maybe.flow.service.IWaitService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags="待办任务", description="查看和处理待办任务")
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/task/wait")
public class WaitController {

    @Autowired
    private IWaitService waitService;

    @ApiOperation("分页获取当前用户待办任务列表")
    @GetMapping("/page")
    public JsonData waitPage(@Valid PageParam pageParam, BindingResult pageResult,
                             @Valid FlowParam flowParam, BindingResult bindingResult) {
        if (pageResult.hasErrors()) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), pageResult.getFieldError().getDefaultMessage());
        }
        PageDto<FlowWait> page = waitService.waitPage(flowParam, pageParam);
        return JsonData.success(page);
    }

}
