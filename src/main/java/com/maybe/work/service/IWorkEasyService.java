package com.maybe.work.service;

import com.maybe.work.common.param.WorkParam;
import com.maybe.work.model.WorkEasy;

public interface IWorkEasyService {

    void start(String content, WorkParam param);

    void solve(WorkParam param);

    WorkEasy select(String processId);

    void update(String processId, String carbon);
}
