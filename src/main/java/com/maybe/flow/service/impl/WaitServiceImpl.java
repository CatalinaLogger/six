package com.maybe.flow.service.impl;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.dao.FlowWaitMapper;
import com.maybe.flow.model.FlowWait;
import com.maybe.flow.service.IWaitService;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.SysRoleUserMapper;
import com.maybe.sys.model.SysRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WaitServiceImpl implements IWaitService {

    @Autowired
    private FlowWaitMapper flowWaitMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    /**
     * 获取当前用户代办任务
     * @return
     */
    @Override
    public PageDto<FlowWait> waitPage(FlowParam param, PageParam page) {
        List<SysRole> roles = sysRoleUserMapper.roleListByUserId(SessionLocal.getUser().getId());
        int total = flowWaitMapper.waitCountByUserAndRole(SessionLocal.getUser().getUsername(), roles, param, page);
        List<FlowWait> list = flowWaitMapper.waitPageByUserAndRole(SessionLocal.getUser().getUsername(), roles, param, page);
        return new PageDto<>(page.getPage(), page.getSize(), total, list);
    }
}
