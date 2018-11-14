package com.maybe.sys.service;

import com.maybe.sys.common.param.DeptParam;
import com.maybe.sys.model.SysUser;

import java.util.List;

public interface ISysDeptService {
    void insert(DeptParam param);

    void update(DeptParam param);

    void delete(Integer deptId);

    List<SysUser> findLeadListByDeptId(Integer deptId);

    List<SysUser> handleUserListWithDept(List<SysUser> userList);
}
