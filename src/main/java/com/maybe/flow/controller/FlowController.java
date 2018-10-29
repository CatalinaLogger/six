package com.maybe.flow.controller;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.model.FlowDefine;
import com.maybe.flow.model.FlowMine;
import com.maybe.flow.model.FlowTask;
import com.maybe.flow.service.IFlowService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Api(tags="流程管理", description="流程部署、流程状态、流程发起记录")
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/flow")
public class FlowController {

    @Autowired
    private IFlowService flowService;

    @ApiOperation("获取流程定义列表")
    @GetMapping("/list")
    public JsonData flowList(
            @ApiParam("流程名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam("流程分类编码") @RequestParam(value = "category", required = false) String category,
            @ApiParam("是否只获取最新版本") @RequestParam(value = "last", defaultValue = "false") Boolean last) {
        List<FlowDefine> deployments = flowService.flowListByNameOrCategory(name, category, last);
        return JsonData.success(deployments);
    }

    @ApiOperation("部署流程文件")
    @PostMapping("/deploy")
    public JsonData deployFlow(
            @ApiParam(value = "流程名称", required = true) @RequestParam("name") String name,
            @ApiParam(value = "流程分类编码", required = true) @RequestParam("category") String category,
            @ApiParam(value = "流程定义文件", required = true) @RequestParam("file") MultipartFile file){
        flowService.deploy(name, category, file);
        return JsonData.successOperate();
    }

    @ApiOperation("启用流程")
    @PostMapping("/activate")
    public JsonData activateFlow(@ApiParam(value = "流程定义ID", required = true) @RequestParam("processId") String processId) {
        flowService.activate(processId);
        return JsonData.successOperate();
    }

    @ApiOperation("暂停流程")
    @PostMapping("/suspend")
    public JsonData suspendFlow(@ApiParam(value = "流程定义ID", required = true) @RequestParam("processId") String processId) {
        flowService.suspend(processId);
        return JsonData.successOperate();
    }

    @ApiOperation("根据流程实例ID获取历史任务记录列表")
    @GetMapping("/task")
    public JsonData flowList(@ApiParam(value = "流程实例ID", required = true) @RequestParam("processId") String processId) {
        List<FlowTask> taskList = flowService.taskListByProcessId(processId);
        return JsonData.success(taskList);
    }

    @ApiOperation("分页获取当前用户发起的流程列表")
    @GetMapping("/mine/page")
    public JsonData minePage(@Valid PageParam pageParam, BindingResult pageResult,
                             @Valid FlowParam flowParam, BindingResult bindingResult) {
        if (pageResult.hasErrors()) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), pageResult.getFieldError().getDefaultMessage());
        }
        PageDto<FlowMine> minePage = flowService.minePage(flowParam, pageParam);
        return JsonData.success(minePage);
    }
}
