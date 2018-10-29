package com.maybe.flow.service;

import com.maybe.work.common.param.WorkParam;
import org.flowable.task.api.Task;

import java.util.Map;

public interface IProxyService {

    String startProcess(WorkParam param);

    String startProcess(WorkParam param, Map<String, Object> varMap);

    Task solveProcess(WorkParam param);

    Task solveProcess(WorkParam param, Map<String, Object> varMap);
}
