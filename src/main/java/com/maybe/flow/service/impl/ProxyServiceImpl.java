package com.maybe.flow.service.impl;

import com.maybe.flow.dao.FlowCheckMapper;
import com.maybe.flow.model.FlowCheck;
import com.maybe.flow.service.IProxyService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.SysUserMapper;
import com.maybe.sys.model.SysUser;
import com.maybe.work.common.param.WorkParam;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.impl.identity.Authentication;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProxyServiceImpl implements IProxyService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private FlowCheckMapper flowCheckMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 封装通用发起流程接口
     * @return 流程实例ID
     */
    @Override
    public String startProcess(WorkParam param) {
        Map<String, Object> map = new HashMap<>();
        map.put("taskCode", param.getTaskCode());
        map.put("startUser", SessionLocal.getUser().getUsername()); // 设置第一个用户任务的用户

        Authentication.setAuthenticatedUserId(SessionLocal.getUser().getId().toString()); // 保存流程发起人
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(param.getDefineId(), param.getLevel().toString(), map);
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        taskService.setOwner(task.getId(), SessionLocal.getUser().getId().toString());
        taskService.complete(task.getId());
        Authentication.setAuthenticatedUserId(null);

        // 保存用户设置的流程变量
        flowCheckMapper.insertSelective(new FlowCheck(task.getId(), param.getTaskCode(), param.getTaskNote()));

        sendNotice(processInstance.getProcessInstanceId());
        System.out.println("主线程");
        return processInstance.getProcessInstanceId();
    }

    @Override
    public String startProcess(WorkParam param, Map<String, Object> varMap) {
        Map<String, Object> map = new HashMap<>();
        map.put("taskCode", param.getTaskCode());
        map.put("startUser", SessionLocal.getUser().getUsername()); // 设置第一个用户任务的用户
        map.putAll(varMap);

        Authentication.setAuthenticatedUserId(SessionLocal.getUser().getId().toString()); // 保存流程发起人
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(param.getDefineId(), param.getLevel().toString(), map);
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        taskService.setOwner(task.getId(), SessionLocal.getUser().getId().toString());
        taskService.complete(task.getId());
        Authentication.setAuthenticatedUserId(null);

        // 保存用户设置的流程变量
        flowCheckMapper.insertSelective(new FlowCheck(task.getId(), param.getTaskCode(), param.getTaskNote()));

        sendNotice(processInstance.getProcessInstanceId());
        System.out.println("主线程");
        return processInstance.getProcessInstanceId();
    }

    /**
     * 封装通用用户任务处理接口
     * @return 处理的任务
     */
    @Override
    public Task solveProcess(WorkParam param) {
        Task task = taskService.createTaskQuery().taskId(param.getTaskId()).singleResult();
        if (ObjectUtils.isEmpty(task)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "该任务不存在或已处理");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("taskCode", param.getTaskCode());
        taskService.setOwner(param.getTaskId(), SessionLocal.getUser().getId().toString());
        taskService.complete(param.getTaskId(), map);

        // 保存用户设置的流程变量
        flowCheckMapper.insertSelective(new FlowCheck(param.getTaskId(), param.getTaskCode(), param.getTaskNote()));

        sendNotice(task.getProcessInstanceId());
        System.out.println("主线程");
        return task;
    }

    @Override
    public Task solveProcess(WorkParam param, Map<String, Object> varMap) {
        Task task = taskService.createTaskQuery().taskId(param.getTaskId()).singleResult();
        if (ObjectUtils.isEmpty(task)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "该任务不存在或已处理");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("taskCode", param.getTaskCode());
        map.putAll(varMap);
        taskService.setOwner(param.getTaskId(), SessionLocal.getUser().getId().toString());
        taskService.complete(param.getTaskId(), map);

        // 保存用户设置的流程变量
        flowCheckMapper.insertSelective(new FlowCheck(param.getTaskId(), param.getTaskCode(), param.getTaskNote()));

        sendNotice(task.getProcessInstanceId());
        System.out.println("主线程");
        return task;
    }

    /** 异步发起审批通知 */
    private void sendNotice(final String processId) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
                SysUser startUser = sysUserMapper.selectByPrimaryKey(Integer.valueOf(processInstance.getStartUserId()));
                List<SysUser> list = sysUserMapper.userListByProcessId(processId);
                for (SysUser item : list) {
                    String message = "{\"name\": \"" + item.getName() + "\", \"message\": \"工作平台提示：\\n  " +
                            startUser.getDeptName() + "--" + startUser.getName() + " 发起了【"+ processInstance.getProcessDefinitionName() +
                            "】，请及时处理！\"}";
                    System.out.println("rabbit生产者发送消息 : " + message);
                    /** 第一个参数：交换机的名字，我们在第二步，声明了topic交换机名称为spring-boot-topicExchange的交换机，
                     因此这里写spring-boot-topicExhchange
                     第二个参数：队列的名称，我们在第二步，配置了名字为queue.demo的队列，因此这里写的是queue.demo
                     第三个参数：要发送的消息数据。
                     说明：当生成这发送数据到mq后，首先会寻找与路由键匹配的绑定模式，而我们的匹配规则是queue.#
                     (其中#标示0个或多个，*标示至少有一个)，因此这里发送的数据会被被queue.demo的队列消费。*/
                    amqpTemplate.convertAndSend("spring-boot-topicExchange", "queue.wechat", message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
