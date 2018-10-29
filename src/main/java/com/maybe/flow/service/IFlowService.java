package com.maybe.flow.service;


import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.model.FlowDefine;
import com.maybe.flow.model.FlowMine;
import com.maybe.flow.model.FlowTask;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/4/27
 */
public interface IFlowService {

    void deploy(String name, String category, MultipartFile file);

    List<FlowDefine> flowListByNameOrCategory(String name, String category, Boolean last);

    void activate(String processId);

    void suspend(String processId);

    List<FlowTask> taskListByProcessId(String processId);

    PageDto<FlowMine> minePage(FlowParam param, PageParam page);
}
