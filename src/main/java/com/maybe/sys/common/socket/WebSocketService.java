package com.maybe.sys.common.socket;

import com.maybe.sys.dao.SysUserMapper;
import com.maybe.sys.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jin
 * @description:
 * @date 2018/5/5
 */
@Slf4j
@Service
public class WebSocketService {
    @Autowired
    private SysUserMapper sysUserMapper;

    public SysUser findUserById(Integer id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }
}