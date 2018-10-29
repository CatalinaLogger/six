package com.maybe.work.service.impl;

import com.maybe.flow.service.IProxyService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.SysDeptMapper;
import com.maybe.sys.model.SysDept;
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
    @Autowired
    private SysDeptMapper sysDeptMapper;

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
        } else {
            SysDept dept = sysDeptMapper.selectByPrimaryKey(SessionLocal.getUser().getDeptId());
            map.put("leader", dept.getLeaderUsername());
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
            workEasy.setUserId(SessionLocal.getUser().getId());
            workEasy.setUserName(SessionLocal.getUser().getName());
            workEasy.setDeptId(SessionLocal.getUser().getDeptId());
            workEasy.setDeptName(SessionLocal.getUser().getDeptName());
            workEasy.setStartTime(new Date());
            workEasy.setStatus(0);
            workEasy.setOperateIp(SessionLocal.getUser().getOperateIp());
            workEasy.setOperateId(SessionLocal.getUser().getId());
            workEasy.setOperateName(SessionLocal.getUser().getName());
            workEasy.setOperateTime(new Date());
            workEasyMapper.insert(workEasy);
        } else {
            Task task = proxyService.solveProcess(param, map);
            WorkEasy workEasy = workEasyMapper.selectByPrimaryKey(task.getProcessInstanceId());
            workEasy.setContent(content);
            workEasy.setOperateIp(SessionLocal.getUser().getOperateIp());
            workEasy.setOperateId(SessionLocal.getUser().getId());
            workEasy.setOperateName(SessionLocal.getUser().getName());
            workEasy.setOperateTime(new Date());
            workEasyMapper.updateByPrimaryKeyWithBLOBs(workEasy);
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
}
