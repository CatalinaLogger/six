package com.maybe.sys.service;

import com.maybe.sys.common.dto.DeptLevelDto;
import com.maybe.sys.model.SysDept;

/**
 * @author jin
 * @description:
 * @date 2018/4/27
 */
public interface ISysDeptService {

    void insert(SysDept sysDept);

    void update(SysDept sysDept);

    void delete(Integer deptId);

    DeptLevelDto mine();

}
