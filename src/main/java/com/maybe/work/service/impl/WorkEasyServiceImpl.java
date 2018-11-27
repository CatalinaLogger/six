package com.maybe.work.service.impl;

import com.maybe.flow.service.IProxyService;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.common.util.SixUtil;
import com.maybe.work.common.param.WorkParam;
import com.maybe.work.dao.WorkEasyMapper;
import com.maybe.work.model.WorkEasy;
import com.maybe.work.service.IWorkEasyService;
import org.flowable.task.api.Task;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkEasyServiceImpl implements IWorkEasyService {

    @Autowired
    private IProxyService proxyService;
    @Autowired
    private WorkEasyMapper workEasyMapper;

    @Override
    @Transactional
    public void start(String content, WorkParam param) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject(content);
        if (jsonObject.has("exist")) {
            map.put("exist", jsonObject.getInt("exist"));
        }
        if (jsonObject.has("leader")) {
            map.put("leader", jsonObject.getString("leader"));
        }
        if (jsonObject.has("helper")) {
            map.put("helper", jsonObject.getString("helper"));
        } else {
            map.put("helper", "");
        }
        if (ObjectUtils.isEmpty(param.getTaskId())) {
            String processId = proxyService.startProcess(param, map);
            WorkEasy workEasy = new WorkEasy();
            workEasy.setProcInstId(processId);
            workEasy.setCode(param.getDefineId());
            workEasy.setContent(content);
            workEasy.setCarbon(content);
            workEasy.setUserId(SessionLocal.getUser().getId());
            workEasy.setUserName(SessionLocal.getUser().getName());
            workEasy.setStartTime(new Date());
            workEasy.setStatus(0);
            SixUtil.setOperate(workEasy);
            workEasyMapper.insert(workEasy);
        } else {
            Task task = proxyService.solveProcess(param, map);
            WorkEasy workEasy = workEasyMapper.selectByPrimaryKey(task.getProcessInstanceId());
            workEasy.setContent(content);
            workEasy.setCarbon(content);
            SixUtil.setOperate(workEasy);
            workEasyMapper.updateByPrimaryKey(workEasy);
        }

    }

    @Override
    @Transactional
    public void solve(WorkParam param) {
        proxyService.solveProcess(param);
    }

    @Override
    public WorkEasy select(String processId) {
        return workEasyMapper.selectByPrimaryKey(processId);
    }

    @Override
    public void update(String processId, String carbon) {
        WorkEasy workEasy = workEasyMapper.selectByPrimaryKey(processId);
        workEasy.setCarbon(carbon);
        workEasyMapper.updateByPrimaryKey(workEasy);
    }
}
