package com.maybe.sys.service.impl;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.param.RoleParam;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.SysRoleMapper;
import com.maybe.sys.dao.SysRoleMenuMapper;
import com.maybe.sys.dao.SysRoleUserMapper;
import com.maybe.sys.model.SysMenu;
import com.maybe.sys.model.SysRole;
import com.maybe.sys.model.SysRoleMenu;
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

/**
 * @author jin
 * @description:
 * @date 2018/5/10
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    @Transactional
    public void insert(RoleParam param) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(param, sysRole);
        if (checkExist(sysRole.getName(), sysRole.getId())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "该角色已存在");
        }
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
        SysRole after = new SysRole();
        BeanUtils.copyProperties(param, after);
        if (checkExist(after.getName(), after.getId())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "该角色已存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(after.getId());
        if (ObjectUtils.isEmpty(before)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待更新角色不存在");
        }
        after.setOperateIp(SessionLocal.getUser().getOperateIp());
        after.setOperateId(SessionLocal.getUser().getId());
        after.setOperateName(SessionLocal.getUser().getName());
        after.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKey(after);
        List<Integer> oldKeys = sysRoleMenuMapper.selectMenuKeysByRoleId(after.getId());
        List<Integer> newKeys = param.getMenuKeys();
        List<Integer> addKeys = new ArrayList<>();
        addKeys.addAll(newKeys);
        addKeys.removeAll(oldKeys);
        List<Integer> delKeys = new ArrayList<>();
        delKeys.addAll(oldKeys);
        delKeys.removeAll(newKeys);
        if (addKeys.size() > 0) {
            sysRoleMenuMapper.insertRoleMenu(after, addKeys);
        }
        if (delKeys.size() > 0) {
            sysRoleMenuMapper.removeRoleMenu(after, delKeys);
        }
    }

    @Override
    @Transactional
    public void delete(Integer roleId) {
        SysRole before = sysRoleMapper.selectByPrimaryKey(roleId);
        if (ObjectUtils.isEmpty(before)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待删除角色不存在");
        }
        sysRoleMenuMapper.deleteRoleMenuByRoleId(roleId);
        sysRoleUserMapper.deleteRoleUserByRoleId(roleId);
        sysRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public List<SysRole> findAll() {
        return sysRoleMapper.findAll();
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
        if (total > 0) {
            List<SysUser> list = sysRoleUserMapper.pageBoundByRoleId(roleId, query, page);
            return new PageDto<>(page.getPage(), page.getSize(), total, list);
        }
        return new PageDto<>(page.getPage(), page.getSize(), total, null);
    }

    @Override
    public PageDto<SysUser> pageUnboundByRoleId(Integer roleId, String query, PageParam page) {
        int total = sysRoleUserMapper.countUnboundByRoleId(roleId, query);
        if (total > 0) {
            List<SysUser> list = sysRoleUserMapper.pageUnboundByRoleId(roleId, query, page);
            return new PageDto<>(page.getPage(), page.getSize(), total, list);
        }
        return new PageDto<>(page.getPage(), page.getSize(), total, null);
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

    public boolean checkExist(String roleName, Integer roleId) {
        return sysRoleMapper.countByName(roleName, roleId) > 0;
    }
}
