package com.maybe.flow.service.impl;

import com.maybe.flow.dao.FlowCheckMapper;
import com.maybe.flow.model.FlowCheck;
import com.maybe.flow.service.IProxyService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.work.common.param.WorkParam;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.impl.identity.Authentication;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ProxyServiceImpl implements IProxyService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FlowCheckMapper flowCheckMapper;

    /**
     * 封装通用发起流程接口
     * @return 流程实例ID
     */
    @Override
    public String startProcess(WorkParam param) {
        return startProcess(param, null);
    }

    @Override
    public String startProcess(WorkParam param, Map<String, Object> varMap) {
        Map<String, Object> map = new HashMap<>();
        map.put("starter", SessionLocal.getUser().getUsername()); // 设置第一个用户任务的用户
        map.put("taskCode", param.getTaskCode());
        if (!ObjectUtils.isEmpty(varMap)) {
            map.putAll(varMap);
        }
        Authentication.setAuthenticatedUserId(SessionLocal.getUser().getUsername()); // 保存流程发起人
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(param.getDefineId(), param.getLevel().toString(), map);
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        taskService.setAssignee(task.getId(), SessionLocal.getUser().getUsername());
        taskService.complete(task.getId());
        Authentication.setAuthenticatedUserId(null);
        // 保存用户设置的流程变量
        flowCheckMapper.insertSelective(new FlowCheck(task.getId(), param.getTaskCode(), param.getTaskName(), param.getTaskNote()));
        return processInstance.getProcessInstanceId();
    }

    /**
     * 封装通用用户任务处理接口
     * @return 处理的任务
     */
    @Override
    public Task solveProcess(WorkParam param) {
        return solveProcess(param, null);
    }

    @Override
    public Task solveProcess(WorkParam param, Map<String, Object> varMap) {
        Task task = taskService.createTaskQuery().taskId(param.getTaskId()).singleResult();
        if (ObjectUtils.isEmpty(task)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "该任务不存在或已处理");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("taskCode", param.getTaskCode());
        if (!ObjectUtils.isEmpty(varMap)) {
            map.putAll(varMap);
        }
        taskService.setAssignee(task.getId(), SessionLocal.getUser().getUsername());
        taskService.complete(param.getTaskId(), map);
        // 保存用户设置的流程变量
        flowCheckMapper.insertSelective(new FlowCheck(param.getTaskId(), param.getTaskCode(), param.getTaskName(), param.getTaskNote()));
        return task;
    }
}
