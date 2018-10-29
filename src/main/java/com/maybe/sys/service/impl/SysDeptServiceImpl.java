package com.maybe.sys.service.impl;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.DeptLevelDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.LevelUtil;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.SysDeptMapper;
import com.maybe.sys.dao.SysUserMapper;
import com.maybe.sys.dao.SysUtilsMapper;
import com.maybe.sys.model.SysDept;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

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
    private SysUtilsMapper sysUtilsMapper;

    @Override
    public void insert(SysDept sysDept) {
        if (checkExist(sysDept.getParentId(), sysDept.getId(), "name", sysDept.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该部门");
        }
        handleLeaderHelper(sysDept);
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(sysDept.getParentId()), sysDept.getParentId()));
        sysDept.setOperateIp(SessionLocal.getUser().getOperateIp());
        sysDept.setOperateId(SessionLocal.getUser().getId());
        sysDept.setOperateName(SessionLocal.getUser().getName());
        sysDept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(sysDept);
    }

    @Override
    public void update(SysDept after) {
        if (checkExist(after.getParentId(), after.getId(), "name", after.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该部门");
        }
        SysDept before = sysDeptMapper.selectByPrimaryKey(after.getId());
        if (ObjectUtils.isEmpty(before)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待更新部门不存在");
        }
        handleLeaderHelper(after);
        after.setLevel(LevelUtil.calculateLevel(getLevel(after.getParentId()), after.getParentId()));
        after.setOperateIp(SessionLocal.getUser().getOperateIp());
        after.setOperateId(SessionLocal.getUser().getId());
        after.setOperateName(SessionLocal.getUser().getName());
        after.setOperateTime(new Date());
        updateWithChild(before, after);
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
    public DeptLevelDto mine() {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(SessionLocal.getUser().getDeptId());
        return DeptLevelDto.adapt(sysDept);
    }

    @Transactional
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
        sysDeptMapper.updateByPrimaryKey(after);
    }

    private void handleLeaderHelper(SysDept sysDept) {
        if (!ObjectUtils.isEmpty(sysDept.getLeaderId())) {
            SysUser leader = sysUserMapper.selectByPrimaryKey(sysDept.getLeaderId());
            if (ObjectUtils.isEmpty(leader)) {
                throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "未找到负责人信息");
            }
            sysDept.setLeaderName(leader.getName());
            sysDept.setLeaderUsername(leader.getUsername());
        }
        if (!ObjectUtils.isEmpty(sysDept.getHelperId())) {
            SysUser helper = sysUserMapper.selectByPrimaryKey(sysDept.getHelperId());
            if (ObjectUtils.isEmpty(helper)) {
                throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "未找到协助人信息");
            }
            sysDept.setHelperName(helper.getName());
            sysDept.setHelperUsername(helper.getUsername());
        }
    }

    private boolean checkExist(Integer parentId, Integer deptId, String field, String value) {
        int count = sysUtilsMapper.countOfExist("sys_dept", parentId, deptId, field, value);
        return count > 0;
    }

    private String getLevel(Integer id) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(id);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();
    }
}
