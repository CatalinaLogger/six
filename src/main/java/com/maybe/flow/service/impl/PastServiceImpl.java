package com.maybe.flow.service.impl;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.dao.FlowPastMapper;
import com.maybe.flow.model.FlowPast;
import com.maybe.flow.service.IPastService;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.util.SessionLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PastServiceImpl implements IPastService {

    @Autowired
    private FlowPastMapper flowPastMapper;

    /**
     * 根据当前用户获取已办任务列表
     * @return
     */
    @Override
    public PageDto<FlowPast> pastPage(FlowParam param, PageParam page) {
        int total = flowPastMapper.pastCountByUser(SessionLocal.getUser().getUsername(), param, page);
        List<FlowPast> list = flowPastMapper.pastPageByUser(SessionLocal.getUser().getUsername(), param, page);
        return new PageDto<>(page.getPage(), page.getSize(), total, list);
    }
}
