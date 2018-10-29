package com.maybe.flow.service;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.model.FlowPast;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;

/**
 * @author jin
 * @description:
 * @date 2018/4/27
 */
public interface IPastService {

    PageDto<FlowPast> pastPage(FlowParam param, PageParam page);
}
