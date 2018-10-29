package com.maybe.ding.service.impl;

import com.maybe.ding.common.util.DingApi;
import com.maybe.ding.service.IDingDeptService;
import com.maybe.sys.common.util.LevelUtil;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.SysDeptMapper;
import com.maybe.sys.model.SysDept;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DingDeptServiceImpl implements IDingDeptService {

    @Autowired
    private DingApi dingApi;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public void synchronize() {
        List<SysDept> deptList = new ArrayList<>();
        SysDept rootDept = sysDeptMapper.selectByPrimaryKey(1);
        LinkedMultiValueMap<Integer, SysDept> linkedMultiValueMap = new LinkedMultiValueMap<>();
        JSONObject jsonObject = dingApi.departmentList();
        JSONArray department = jsonObject.getJSONArray("department");
        for (int i = 0; i < department.length(); i++) {
            SysDept sysDept = new SysDept();
            JSONObject dept = department.getJSONObject(i);
            sysDept.setId(dept.getInt("id"));
            sysDept.setName(dept.getString("name"));
            sysDept.setOperateIp(SessionLocal.getUser().getOperateIp());
            sysDept.setOperateId(SessionLocal.getUser().getId());
            sysDept.setOperateName(SessionLocal.getUser().getName());
            sysDept.setOperateTime(new Date());
            if (dept.has("parentid")) {
                sysDept.setParentId(dept.getInt("parentid"));
                linkedMultiValueMap.add(sysDept.getParentId(), sysDept);
            } else {
                rootDept.setId(sysDept.getId());
            }
        }
        handleChildDept(rootDept, linkedMultiValueMap, deptList);
    }

    private void handleChildDept(SysDept parentDept, LinkedMultiValueMap<Integer, SysDept> linkedMap, List<SysDept> deptList) {
        List<SysDept> list = linkedMap.get(parentDept.getId());
        if (!ObjectUtils.isEmpty(list)) {
            for (SysDept item : list) {
                item.setLevel(LevelUtil.calculateLevel(parentDept.getLevel(), parentDept.getId()));
                deptList.add(item);
                handleChildDept(item, linkedMap, deptList);
            }
        }
    }
}
