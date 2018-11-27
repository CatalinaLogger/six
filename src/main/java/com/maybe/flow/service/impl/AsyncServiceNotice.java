package com.maybe.flow.service.impl;

import com.maybe.flow.model.FlowDefine;
import com.maybe.flow.service.IFlowService;
import com.maybe.flow.service.IWaitService;
import com.maybe.sys.common.config.SystemConfig;
import com.maybe.sys.model.SysDept;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 异步发送通知
 */

@Slf4j
@Service
public class AsyncServiceNotice {

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private IFlowService flowService;
    @Autowired
    private IWaitService waitService;
    @Autowired
    private ISysUserService sysUserService;

    @Async("asyncExecutor")
    public void sendNoticeOrLead(String username,  String defineId, List<SysUser> list) {
        try {
            SysUser starter = sysUserService.selectByUsername(username);
            FlowDefine define = flowService.selectByDefineId(defineId);
            String dept = "";
            for (SysDept item : starter.getDept()) {
                dept += "【" + item.getName() + "】";
            }
            for (SysUser item : list) {
                String message = "{\"name\": \"" + item.getName() + "\", \"message\": \"工作平台提示：\\n  " +
                        dept + "-- " +starter.getName() + " 发起了【"+ define.getName() +
                        "】，请及时处理！\"}";
                log.info("rabbit生产者发送消息>> {}", message);
                // 如果是生产环境则发起通知
                if (systemConfig.getActive().equals("pro")) {
                    /** 第一个参数：交换机的名字，我们在第二步，声明了topic交换机名称为spring-boot-topicExchange的交换机，
                     因此这里写spring-boot-topicExhchange
                     第二个参数：队列的名称，我们在第二步，配置了名字为queue.demo的队列，因此这里写的是queue.demo
                     第三个参数：要发送的消息数据。
                     说明：当生成这发送数据到mq后，首先会寻找与路由键匹配的绑定模式，而我们的匹配规则是queue.#
                     (其中#标示0个或多个，*标示至少有一个)，因此这里发送的数据会被被queue.demo的队列消费。*/
                    amqpTemplate.convertAndSend("spring-boot-topicExchange", "queue.wechat", message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async("asyncExecutor")
    public void sendNoticeAuth(String username, String defineId, String processId) {
        try {
            Thread.sleep(1000);
            SysUser starter = sysUserService.selectByUsername(username);
            FlowDefine define = flowService.selectByDefineId(defineId);
            List<String> userList = waitService.userListByProcessId(processId);
            String dept = "";
            for (SysDept item : starter.getDept()) {
                dept += "【" + item.getName() + "】";
            }
            for (String item : userList) {
                String message = "{\"name\": \"" + item + "\", \"message\": \"工作平台提示：\\n  " +
                        dept + "-- " +starter.getName() + " 发起了【"+ define.getName() +
                        "】，请及时审批！\"}";
                log.info("rabbit生产者发送消息>> {}", message);
                // 如果是生产环境则发起通知
                if (systemConfig.getActive().equals("pro")) {
                    amqpTemplate.convertAndSend("spring-boot-topicExchange", "queue.wechat", message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async("asyncExecutor")
    public void sendNoticeTask(String processId, String eventName) {
        try {
            Thread.sleep(1000);
            List<String> userList = waitService.userListByProcessId(processId);
            for (String item : userList) {
                String message = "{\"name\": \"" + item + "\", \"message\": \"工作平台提示：\\n  " +
                        "您有一单【"+ eventName +"】任务，请及时处理！\"}";
                log.info("rabbit生产者发送消息>> {}", message);
                // 如果是生产环境则发起通知
                if (systemConfig.getActive().equals("pro")) {
                    amqpTemplate.convertAndSend("spring-boot-topicExchange", "queue.wechat", message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async("asyncExecutor")
    public void sendNoticeBack(String username, String defineId) {
        SysUser starter = sysUserService.selectByUsername(username);
        FlowDefine define = flowService.selectByDefineId(defineId);
        String message = "{\"name\": \"" + starter.getName() + "\", \"message\": \"工作平台提示：\\n  " +
                " 您发起的【"+ define.getName() +
                "】已被驳回，请及时处理！\"}";
        log.info("rabbit生产者发送消息>> {}", message);
        // 如果是生产环境则发起通知
        if (systemConfig.getActive().equals("pro")) {
            amqpTemplate.convertAndSend("spring-boot-topicExchange", "queue.wechat", message);
        }
    }

    @Async("asyncExecutor")
    public void sendNoticePress(String name, String processId) {
        try {
            List<String> userList = waitService.userListByProcessId(processId);
            for (String item : userList) {
                String message = "{\"name\": \"" + item + "\", \"message\": \"工作平台提示：\\n  " +
                        name + " 提醒您办理TA的申请！\"}";
                log.info("rabbit生产者发送消息>> {}", message);
                // 如果是生产环境则发起通知
                if (systemConfig.getActive().equals("pro")) {
                    amqpTemplate.convertAndSend("spring-boot-topicExchange", "queue.wechat", message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
