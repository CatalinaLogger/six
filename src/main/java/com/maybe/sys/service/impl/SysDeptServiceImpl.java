package com.maybe.sys.service.impl;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.DeptParam;
import com.maybe.sys.common.util.LevelUtil;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.common.util.SixUtil;
import com.maybe.sys.dao.*;
import com.maybe.sys.model.SysDept;
import com.maybe.sys.model.SysRole;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysDeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jin
 * @description:
 * @date 2018/4/27
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysDeptLeadMapper sysDeptLeadMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysUtilsMapper sysUtilsMapper;

    @Override
    public void insert(DeptParam param) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(param, sysDept);
        if (checkExist(sysDept.getParentId(), sysDept.getId(), "name", sysDept.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该部门");
        }
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(sysDept.getParentId()), sysDept.getParentId()));
        sysDept.setOperateIp(SessionLocal.getUser().getOperateIp());
        sysDept.setOperateId(SessionLocal.getUser().getId());
        sysDept.setOperateName(SessionLocal.getUser().getName());
        sysDept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(sysDept);
    }

    @Override
    @Transactional
    public void update(DeptParam param) {
        SysDept after = new SysDept();
        BeanUtils.copyProperties(param, after);
        if (checkExist(after.getParentId(), after.getId(), "name", after.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该部门");
        }
        SysDept before = sysDeptMapper.selectByPrimaryKey(after.getId());
        if (ObjectUtils.isEmpty(before)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待更新部门不存在");
        }
        after.setLevel(LevelUtil.calculateLevel(getLevel(after.getParentId()), after.getParentId()));
        after.setOperateIp(SessionLocal.getUser().getOperateIp());
        after.setOperateId(SessionLocal.getUser().getId());
        after.setOperateName(SessionLocal.getUser().getName());
        after.setOperateTime(new Date());
        updateWithChild(before, after);

        /** 获取主管这个角色，数据库ID为 7 */
        SysRole role = sysRoleMapper.selectByPrimaryKey(7);
        role.setDeptId(after.getId());
        role.setOperateIp(SessionLocal.getUser().getOperateIp());
        role.setOperateId(SessionLocal.getUser().getId());
        role.setOperateName(SessionLocal.getUser().getName());
        role.setOperateTime(new Date());

        List<Integer> oldLeadKeys = sysDeptLeadMapper.selectLeadKeysByDeptId(after.getId());
        List<Integer> newLeadKeys = param.getLeadKeys();
        Map<String, List<Integer>> deptMap = SixUtil.handelKeys(oldLeadKeys, newLeadKeys);
        if (deptMap.containsKey("addKeys")) {
            sysDeptLeadMapper.insertDeptLead(after, deptMap.get("addKeys"));
            sysRoleUserMapper.insertRoleUser(role, deptMap.get("addKeys"));
        }
        if (deptMap.containsKey("delKeys")) {
            sysDeptLeadMapper.removeDeptLead(after, deptMap.get("delKeys"));
            sysRoleUserMapper.removeRoleUser(role, deptMap.get("delKeys"));
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }

    @Override
    public void delete(Integer deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (ObjectUtils.isEmpty(sysDept)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待删除部门不存在");
        }
        List<SysDept> deptList = sysDeptMapper.findChildrenByLevel(LevelUtil.calculateLevel(sysDept.getLevel(), sysDept.getId()));
        if (deptList.size() > 0) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前部门存在子部门，禁止删除");
        }
        int count = sysUserMapper.countByDeptId(deptId, null);
        if (count > 0) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前部门存在用户，禁止删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }

    @Override
    public List<SysUser> findLeadListByDeptId(Integer deptId) {
        return sysUserMapper.findLeadListByDeptId(deptId);
    }

    /** 处理用户列表使之带上部门信息 */
    @Override
    public List<SysUser> handleUserListWithDept(List<SysUser> userList) {
        List<SysDept> deptList = sysDeptMapper.findDeptListWithUser();
        LinkedMultiValueMap<Integer, SysDept> deptMap = new LinkedMultiValueMap<>();
        for (SysDept dept : deptList) {
            deptMap.add(dept.getUserId(), dept);
        }
        for (SysUser user : userList) {
            user.setDept(deptMap.get(user.getId()));
        }
        return userList;
    }

    protected void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysDept> deptList = sysDeptMapper.findChildrenByLevel(LevelUtil.calculateLevel(before.getLevel(), before.getId()));
            if (!CollectionUtils.isEmpty(deptList)) {
                for (SysDept dept: deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateDeptLevel(deptList);
            }
        }
    }

    private boolean checkExist(Integer parentId, Integer deptId, String field, String value) {
        int count = sysUtilsMapper.countOfExist("sys_dept", parentId, deptId, field, value);
        return count > 0;
    }

    private String getLevel(Integer id) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(id);
        return ObjectUtils.isEmpty(dept) ? null : dept.getLevel();
    }
}
