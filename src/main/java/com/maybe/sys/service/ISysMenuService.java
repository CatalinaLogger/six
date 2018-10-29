package com.maybe.sys.service;

import com.maybe.sys.model.SysMenu;

/**
 * @author jin
 * @description:
 * @date 2018/4/27
 */
public interface ISysMenuService {

    void insert(SysMenu sysMenu);

    void update(SysMenu sysMenu);

    void delete(Integer menuId);
}
