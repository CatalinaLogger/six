package com.maybe.flow;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Api(tags="流程图", description="查看流程图")
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @ApiIgnore
    @GetMapping("/account")
    public String restAccount() {
        return "{\"id\":\"admin\",\"firstName\":\"Test\",\"lastName\":\"Administrator\",\"email\":\"admin@flowable.org\",\"fullName\":\"Test Administrator\",\"groups\":[],\"privileges\":[\"access-idm\",\"access-rest-api\",\"access-task\",\"access-modeler\",\"access-admin\"]}";
    }

    @ApiOperation("根据流程定义ID获取流程图")
    @GetMapping("/define")
    public void processDefine(HttpServletResponse response, @ApiParam(value = "流程定义ID", required = true) @RequestParam("defineId") String defineId) throws Throwable {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(defineId);
        DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new DefaultProcessDiagramGenerator();
        List<String> highLightedActivities = new ArrayList<>();
        List<String> highLightedFlows = new ArrayList<String>();
        InputStream in = defaultProcessDiagramGenerator.generateDiagram(bpmnModel, "PNG", highLightedActivities, highLightedFlows,"宋体","宋体","宋体",null,1.0);
        OutputStream out = response.getOutputStream();
        try {
            IOUtils.copy(in, out);
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }

    @ApiOperation("根据流程实例ID获取流程图")
    @GetMapping("/follow")
    public void processFollow(HttpServletResponse response, @ApiParam(value = "流程实例ID", required = true) @RequestParam("processId") String processId) throws IOException {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        //流程走完的不显示图
        if (pi == null) {return;}
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(InstanceId).list();
        //得到正在执行的Activity的Id
        List<String> highLightedActivities = new ArrayList<>();
        List<String> highLightedFlows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            highLightedActivities.addAll(ids);
        }
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new DefaultProcessDiagramGenerator();
        InputStream in = defaultProcessDiagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivities, highLightedFlows, "宋体","宋体","宋体",null,1.0);
        OutputStream out = response.getOutputStream();
        try {
            IOUtils.copy(in, out);
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }


}
