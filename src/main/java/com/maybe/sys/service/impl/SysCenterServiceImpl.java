package com.maybe.sys.service.impl;

import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.SysUserLoginMapper;
import com.maybe.sys.model.SysUserLogin;
import com.maybe.sys.service.ISysCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SysCenterServiceImpl implements ISysCenterService {

    @Autowired
    private SysUserLoginMapper sysUserLoginMapper;

    @Override
    public PageDto<SysUserLogin> loginPage(PageParam page) {
        int total = sysUserLoginMapper.loginCount(SessionLocal.getUser().getId());
        if (total > 0) {
            List<SysUserLogin> list = sysUserLoginMapper.loginPage(SessionLocal.getUser().getId(), page);
            return new PageDto<>(page.getPage(), page.getSize(), total, list);
        }
        return new PageDto<>(page.getPage(), page.getSize(), total, null);
    }
}
