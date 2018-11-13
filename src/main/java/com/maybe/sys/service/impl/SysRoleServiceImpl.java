package com.maybe.sys.service.impl;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.param.RoleParam;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.*;
import com.maybe.sys.model.SysRole;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUtilsMapper sysUtilsMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public void insertGroup(String name) {
        if (checkExist(0, null, "name", name)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "该角色组名称已存在");
        }
        SysRole group = new SysRole();
        group.setName(name);
        group.setParentId(0);
        group.setEdit(1);
        group.setOperateIp(SessionLocal.getUser().getOperateIp());
        group.setOperateId(SessionLocal.getUser().getId());
        group.setOperateName(SessionLocal.getUser().getName());
        group.setOperateTime(new Date());
        sysRoleMapper.insert(group);
    }

    @Override
    public void updateGroup(Integer id, String name) {
        if (checkExist(0, id, "name", name)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "该角色组名称已存在");
        }
        SysRole group = sysRoleMapper.selectByPrimaryKey(id);
        if (group.getEdit().equals(0)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "系统默认角色组，无法修改");
        }
        group.setName(name);
        group.setOperateIp(SessionLocal.getUser().getOperateIp());
        group.setOperateId(SessionLocal.getUser().getId());
        group.setOperateName(SessionLocal.getUser().getName());
        group.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKey(group);
    }

    @Override
    public List<SysRole> groupList() {
        return sysRoleMapper.groupList();
    }

    @Override
    @Transactional
    public void insert(RoleParam param) {
        if (checkExist(param.getGroupId(), param.getId(), "name", param.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "该角色已存在");
        }
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(param, sysRole);
        sysRole.setParentId(param.getGroupId());
        sysRole.setEdit(1);
        sysRole.setOperateIp(SessionLocal.getUser().getOperateIp());
        sysRole.setOperateId(SessionLocal.getUser().getId());
        sysRole.setOperateName(SessionLocal.getUser().getName());
        sysRole.setOperateTime(new Date());
        sysRoleMapper.insert(sysRole);
        if (param.getMenuKeys().size() > 0) {
            sysRoleMenuMapper.insertRoleMenu(sysRole, param.getMenuKeys());
        }
    }

    @Override
    @Transactional
    public void update(RoleParam param) {
        if (checkExist(param.getGroupId(), param.getId(), "name", param.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "该角色已存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        if (ObjectUtils.isEmpty(before)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待更新角色不存在");
        }
        if (before.getEdit().equals(0)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "系统默认角色，无法修改");
        }
        BeanUtils.copyProperties(param, before);
        before.setParentId(param.getGroupId());
        before.setOperateIp(SessionLocal.getUser().getOperateIp());
        before.setOperateId(SessionLocal.getUser().getId());
        before.setOperateName(SessionLocal.getUser().getName());
        before.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKey(before);
        List<Integer> oldKeys = sysRoleMenuMapper.selectMenuKeysByRoleId(before.getId());
        List<Integer> newKeys = param.getMenuKeys();
        List<Integer> addKeys = new ArrayList<>();
        addKeys.addAll(newKeys);
        addKeys.removeAll(oldKeys);
        List<Integer> delKeys = new ArrayList<>();
        delKeys.addAll(oldKeys);
        delKeys.removeAll(newKeys);
        if (addKeys.size() > 0) {
            sysRoleMenuMapper.insertRoleMenu(before, addKeys);
        }
        if (delKeys.size() > 0) {
            sysRoleMenuMapper.removeRoleMenu(before, delKeys);
        }
    }

    @Override
    @Transactional
    public void delete(Integer roleId) {
        SysRole before = sysRoleMapper.selectByPrimaryKey(roleId);
        if (ObjectUtils.isEmpty(before)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待删除角色组或角色不存在");
        }
        if (before.getEdit().equals(0)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "系统默认，无法删除");
        }
        if (before.getParentId().equals(0)) {
            int count = sysUtilsMapper.countOfExist("sys_role", before.getId(), null, null, null);
            if (count > 0) {
                throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "该角色组下存在角色，无法删除");
            }
        } else {
            sysRoleMenuMapper.deleteRoleMenuByRoleId(roleId);
            sysRoleUserMapper.deleteRoleUserByRoleId(roleId);
        }
        sysRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public List<SysRole> roleList() {
        return sysRoleMapper.roleList();
    }

    @Override
    public List<SysRole> roleListByUser() {
        return sysRoleUserMapper.roleListByUserId(SessionLocal.getUser().getId());
    }

    @Override
    public List<Integer> findMenuKeysByRoleId(Integer roleId) {
        return sysRoleMenuMapper.selectMenuKeysByRoleId(roleId);
    }

    @Override
    public List<Integer> findRoleKeysByUserId(Integer userId) {
        return sysRoleUserMapper.selectRoleKeysByUserId(userId);
    }

    @Override
    public PageDto<SysUser> pageBoundByRoleId(Integer roleId, String query, PageParam page) {
        int total = sysRoleUserMapper.countBoundByRoleId(roleId, query);
        List<SysUser> list = sysRoleUserMapper.pageBoundByRoleId(roleId, query, page);
        return new PageDto<>(page.getPage(), page.getSize(), total, list);
    }

    @Override
    public PageDto<SysUser> pageUnboundByRoleId(Integer roleId, String query, PageParam page) {
        int total = sysRoleUserMapper.countUnboundByRoleId(roleId, query);
        List<SysUser> list = sysRoleUserMapper.pageUnboundByRoleId(roleId, query, page);
        return new PageDto<>(page.getPage(), page.getSize(), total, list);
    }

    @Override
    public void binding(RoleParam param) {
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(param.getId());
        if (ObjectUtils.isEmpty(sysRole)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "绑定角色不存在");
        }
        sysRole.setOperateIp(SessionLocal.getUser().getOperateIp());
        sysRole.setOperateId(SessionLocal.getUser().getId());
        sysRole.setOperateName(SessionLocal.getUser().getName());
        sysRole.setOperateTime(new Date());
        if (param.getUserKeys().size() > 0) {
            sysRoleUserMapper.insertRoleUser(sysRole, param.getUserKeys());
        }
    }

    @Override
    public void untying(RoleParam param) {
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(param.getId());
        if (ObjectUtils.isEmpty(sysRole)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "绑定角色不存在");
        }
        if (param.getUserKeys().size() > 0) {
            sysRoleUserMapper.removeRoleUser(sysRole, param.getUserKeys());
        }
    }

    public boolean checkExist(Integer parentId, Integer roleId, String field, String value) {
        int count = sysUtilsMapper.countOfExist("sys_role", parentId, roleId, field, value);
        return count > 0;
    }
}
