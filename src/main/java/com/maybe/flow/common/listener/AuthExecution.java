package com.maybe.flow.common.listener;

import com.maybe.flow.service.impl.AsyncServiceNotice;
import com.maybe.sys.common.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * 推送审批通知
 */
@Slf4j
@Component
public class AuthExecution implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        String username = execution.getVariable("starter", String.class);
        AsyncServiceNotice asyncService = SpringContextHolder.getBean(AsyncServiceNotice.class);
        asyncService.sendNoticeAuth(username, execution.getProcessDefinitionId(), execution.getProcessInstanceId());
    }

}
