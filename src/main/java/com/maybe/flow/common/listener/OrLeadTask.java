package com.maybe.flow.common.listener;

import com.maybe.flow.common.util.JumpTaskCmd;
import com.maybe.flow.service.impl.AsyncServiceNotice;
import com.maybe.sys.common.util.SpringContextHolder;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ManagementService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置部门主管或签
 */
@Slf4j
@Component
public class OrLeadTask implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        ISysUserService sysUserService = SpringContextHolder.getBean(ISysUserService.class);
        String username = delegateTask.getVariable("starter", String.class);
        List<SysUser> leadList = sysUserService.findLeadListByUsername(username);

        List<String> list = new ArrayList<>();
        boolean jump = false;
        for (SysUser item : leadList) {
            if (item.getUsername().equals(username)) {
                jump = true;
            }
            list.add(item.getUsername());
        }
        if (jump) {
            ManagementService managementService = SpringContextHolder.getBean(ManagementService.class);
            managementService.executeCommand(new JumpTaskCmd(delegateTask.getId(), "manager"));
        } else {
            delegateTask.addCandidateUsers(list);
        }
        AsyncServiceNotice asyncService = SpringContextHolder.getBean(AsyncServiceNotice.class);
        asyncService.sendNoticeOrLead(username, delegateTask.getProcessDefinitionId(), leadList);
    }

}
