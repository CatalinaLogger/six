package com.maybe.sys.service.impl;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.LevelUtil;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.SysMenuMapper;
import com.maybe.sys.dao.SysRoleMenuMapper;
import com.maybe.sys.dao.SysUtilsMapper;
import com.maybe.sys.model.SysMenu;
import com.maybe.sys.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SysMenuServiceImpl implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysUtilsMapper sysUtilsMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public void insert(SysMenu sysMenu) {
        if (checkExist(sysMenu.getParentId(), sysMenu.getId(), "path", sysMenu.getPath())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该路由");
        }
        if (checkExist(sysMenu.getParentId(), sysMenu.getId(), "name", sysMenu.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该菜单");
        }
        sysMenu.setLevel(LevelUtil.calculateLevel(getLevel(sysMenu.getParentId()), sysMenu.getParentId()));
        sysMenu.setOperateIp(SessionLocal.getUser().getOperateIp());
        sysMenu.setOperateId(SessionLocal.getUser().getId());
        sysMenu.setOperateName(SessionLocal.getUser().getName());
        sysMenu.setOperateTime(new Date());
        sysMenuMapper.insertSelective(sysMenu);
    }

    @Override
    public void update(SysMenu after) {
        if (checkExist(after.getParentId(), after.getId(), "path", after.getPath())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该路由");
        }
        if (checkExist(after.getParentId(), after.getId(), "name", after.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该菜单");
        }
        SysMenu before = sysMenuMapper.selectByPrimaryKey(after.getId());
        if (ObjectUtils.isEmpty(before)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待更新菜单不存在");
        }
        after.setLevel(LevelUtil.calculateLevel(getLevel(after.getParentId()), after.getParentId()));
        after.setOperateIp(SessionLocal.getUser().getOperateIp());
        after.setOperateId(SessionLocal.getUser().getId());
        after.setOperateName(SessionLocal.getUser().getName());
        after.setOperateTime(new Date());
        updateWithChild(before, after);
    }

    @Override
    @Transactional
    public void delete(Integer menuId) {
        SysMenu sysMenu = sysMenuMapper.selectByPrimaryKey(menuId);
        if (ObjectUtils.isEmpty(sysMenu)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待删除菜单不存在");
        }
        List<SysMenu> menuList = sysMenuMapper.findChildrenByLevel(LevelUtil.calculateLevel(sysMenu.getLevel(), sysMenu.getId()));
        if (menuList.size() > 0) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前菜单存在子菜单，禁止删除");
        }
        sysRoleMenuMapper.deleteRoleMenuByMenuId(menuId);
        sysMenuMapper.deleteByPrimaryKey(menuId);
    }

    @Transactional
    protected void updateWithChild(SysMenu before, SysMenu after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            System.out.println(before.getLevel() + before.getId());
            List<SysMenu> menuList = sysMenuMapper.findChildrenByLevel(LevelUtil.calculateLevel(before.getLevel(), before.getId()));
            if (!CollectionUtils.isEmpty(menuList)) {
                for (SysMenu Menu: menuList) {
                    String level = Menu.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        Menu.setLevel(level);
                    }
                }
                sysMenuMapper.batchUpdateMenuLevel(menuList);
            }
        }
        sysMenuMapper.updateByPrimaryKey(after);
    }

    private boolean checkExist(Integer parentId, Integer menuId, String field, String value) {
        int count = sysUtilsMapper.countOfExist("sys_menu", parentId, menuId, field, value);
        return count > 0;
    }

    private String getLevel(Integer id) {
        SysMenu Menu = sysMenuMapper.selectByPrimaryKey(id);
        if (Menu == null) {
            return null;
        }
        return Menu.getLevel();
    }
}
