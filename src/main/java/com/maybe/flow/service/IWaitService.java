package com.maybe.flow.service;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.model.FlowWait;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;

import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/4/27
 */
public interface IWaitService {

    PageDto<FlowWait> waitPage(FlowParam param, PageParam page);

    List<String> userListByProcessId(String processId);
}
