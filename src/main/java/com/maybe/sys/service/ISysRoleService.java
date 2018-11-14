package com.maybe.sys.service;

import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.param.RoleParam;
import com.maybe.sys.model.SysRole;
import com.maybe.sys.model.SysUser;

import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/5/5
 */
public interface ISysRoleService {

    void insertGroup(String name);

    void updateGroup(Integer id, String name);

    List<SysRole> groupList();

    void insert(RoleParam param);

    void update(RoleParam param);

    void delete(Integer roleId);

    List<SysRole> roleList();

    List<SysRole> roleListByUser();

    List<Integer> findMenuKeysByRoleId(Integer roleId);

    PageDto<SysUser> pageBoundByRoleId(Integer roleId, String query, PageParam param);

    PageDto<SysUser> pageUnboundByRoleId(Integer roleId, String query, PageParam param);

    void binding(RoleParam param);

    void untying(RoleParam param);
}
