package com.maybe.sys.service;

import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.model.SysUser;

import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/5/5
 */
public interface ISysUnderService {

    List<SysUser> withListOfUser(Integer userId);

    PageDto<SysUser> userPageOfNone(String query, PageParam page);

    void underPush(Integer userId, List<Integer> userKeys);

    void underPull(Integer userId, List<Integer> userKeys);
}
