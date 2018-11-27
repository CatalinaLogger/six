package com.maybe.sys.service;

import com.maybe.sys.common.dto.LoginUserDto;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.param.UserParam;
import com.maybe.sys.model.SysDept;
import com.maybe.sys.model.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/5/5
 */
public interface ISysUserService {

    void insert(UserParam param);

    SysUser select();

    void update(UserParam param);

    void delete(Integer userId);

    void deleteBatch(List<Integer> userKeys);

    PageDto<SysUser> findUserPageByDeptId(Integer deptId, String query, PageParam page);

    boolean checkExist(Integer userId, String field, String value);

    String loginByKeyword(String key, String value);

    String refreshToken(Integer userId);

    LoginUserDto getLoginUserByToken(String token);

    void logoutByKey();

    void reset(String mail, String code);

    void setAvatar(MultipartFile path);

    List<SysUser> userListByRoleCode(String roleCode);

    List<SysUser> userListByName(String name);

    void passUpdate(String oldPassword, String newPassword);

    void mineUpdate(String jsonInfo);

    String mineSelect();

    List<SysDept> findDeptListByUserId(Integer userId);

    List<SysUser> findLeadListByUserId(Integer integer);

    List<SysUser> findLeadListByUsername(String username);

    SysUser selectByUsername(String username);
}
