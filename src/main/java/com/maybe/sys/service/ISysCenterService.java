package com.maybe.sys.service;

import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.model.SysUserLogin;

public interface ISysCenterService {

    PageDto<SysUserLogin> loginPage(PageParam pageParam);
}
