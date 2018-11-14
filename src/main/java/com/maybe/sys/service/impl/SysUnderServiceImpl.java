package com.maybe.sys.service.impl;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.util.LevelUtil;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.SysUserMapper;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysDeptService;
import com.maybe.sys.service.ISysUnderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/5/5
 */
@Slf4j
@Service
public class SysUnderServiceImpl implements ISysUnderService{

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysDeptService sysDeptService;

    @Override
    public List<SysUser> withListOfUser(Integer userId) {
        if (ObjectUtils.isEmpty(userId)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "请提供用户信息");
        }
        if (userId.equals(SessionLocal.getUser().getId())) {
            return sysDeptService.handleUserListWithDept(sysUserMapper.withListOfUser(userId));
        }
        SysUser under = sysUserMapper.selectByPrimaryKey(userId);
        if (ObjectUtils.isEmpty(under.getLevel()) || !under.getLevel().contains(SessionLocal.getUser().getId().toString())) {
            return null;
        } else {
            return sysDeptService.handleUserListWithDept(sysUserMapper.withListOfUser(userId));
        }
    }

    @Override
    public PageDto<SysUser> userPageOfNone(String query, PageParam page) {
        int total = sysUserMapper.userCountPageOfNone(query);
        List<SysUser> list = sysDeptService.handleUserListWithDept(sysUserMapper.userPageOfNone(query, page));
        return new PageDto<>(page.getPage(), page.getSize(), total, list);
    }

    @Override
    public void underPush(Integer userId, List<Integer> userKeys) {
        if (ObjectUtils.isEmpty(userKeys)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "至少绑定一个用户");
        }
        userId = ObjectUtils.isEmpty(userId) ? SessionLocal.getUser().getId() : userId;
        SysUser under = sysUserMapper.selectByPrimaryKey(userId);
        if (ObjectUtils.isEmpty(under.getLevel())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "无绑定下属资格");
        }
        if (under.getId().equals(userId) || under.getLevel().contains(SessionLocal.getUser().getId().toString())) {
            /** 绑定用户的下属 */
            under.setLevel(LevelUtil.calculateLevel(under.getLevel(), under.getId()));
            sysUserMapper.underPush(under, userKeys);
        } else {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "无绑定下属资格");
        }
    }

    @Transactional
    @Override
    public void underPull(Integer userId, List<Integer> userKeys) {
        userId = ObjectUtils.isEmpty(userId) ? SessionLocal.getUser().getId() : userId;
        SysUser under = sysUserMapper.selectByPrimaryKey(userId);
        if (under.getId().equals(userId) || under.getLevel().contains(SessionLocal.getUser().getId().toString())) {
            /** 解绑用户的下属 */
            for (Integer userKey : userKeys) {
                sysUserMapper.underPullChild(userKey);
            }
            /** 解绑用户的下属... */
            sysUserMapper.underPull(under.getId(), userKeys);
        } else {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "无解绑下属资格");
        }
    }
}