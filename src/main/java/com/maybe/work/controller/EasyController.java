package com.maybe.work.controller;

import com.maybe.sys.common.dto.JsonData;
import com.maybe.work.common.param.WorkParam;
import com.maybe.work.model.WorkEasy;
import com.maybe.work.service.IWorkEasyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags="简化版审批流程", description="流程发起，任务处理")
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/work/easy")
public class EasyController {

    @Autowired
    private IWorkEasyService workEasyService;

    @ApiOperation("发起流程")
    @PostMapping("/start")
    @Transactional
    public JsonData start(@ApiParam(value = "流程表单数据（json格式）", required = true) @RequestParam("content") String content,
                          @Valid WorkParam param, BindingResult bindingResult){
        workEasyService.start(content, param);
        return JsonData.successOperate();
    }

    @ApiOperation("处理任务")
    @PostMapping("/solve")
    @Transactional
    public JsonData solve(@Valid WorkParam param, BindingResult bindingResult){
        workEasyService.solve(param);
        return JsonData.successOperate();
    }

    @ApiOperation("根据流程实例ID获取表单数据")
    @GetMapping("/select")
    public JsonData select(@ApiParam(value = "流程实例ID", required = true) @RequestParam("processId") String processId){
        WorkEasy workEasy = workEasyService.select(processId);
        return JsonData.success(workEasy);
    }
}
