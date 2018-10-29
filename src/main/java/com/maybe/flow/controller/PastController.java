package com.maybe.flow.controller;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.model.FlowPast;
import com.maybe.flow.service.IPastService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags="已办任务", description="查看和追踪已办任务")
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/task/past")
public class PastController {

    @Autowired
    private IPastService pastService;

    @ApiOperation("分页获取当前用户已办任务列表")
    @GetMapping("/page")
    public JsonData pastPage(@Valid PageParam pageParam, BindingResult pageResult,
                             @Valid FlowParam flowParam, BindingResult bindingResult) {
        if (pageResult.hasErrors()) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), pageResult.getFieldError().getDefaultMessage());
        }
        PageDto<FlowPast> page = pastService.pastPage(flowParam, pageParam);
        return JsonData.success(page);
    }

}
