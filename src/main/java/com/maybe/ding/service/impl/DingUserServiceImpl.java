package com.maybe.ding.service.impl;

import com.maybe.ding.common.util.DingApi;
import com.maybe.ding.service.IDingUserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DingUserServiceImpl implements IDingUserService {

    @Autowired
    private DingApi dingApi;

    @Override
    public void synchronize() {
        JSONObject deptObject = dingApi.departmentList();
        if (deptObject.has("department")) {
            JSONArray deptList = deptObject.getJSONArray("department");
            for (int i = 0; i < deptList.length(); i++) {
                JSONObject dept = deptList.getJSONObject(i);
                JSONObject userObject = dingApi.userList("department_id=" +dept.getInt("id"));
                System.out.println(userObject);
            }
        }
    }
}
