package com.maybe.sys.controller;

import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.model.SysUserLogin;
import com.maybe.sys.service.ISysCenterService;
import com.maybe.sys.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags="个人中心", description="登录记录")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/system/center")
public class SysCenterController {

    @Autowired
    private ISysCenterService sysCenterService;
    @Autowired
    private ISysUserService sysUserService;

    @ApiOperation("获取当前用户个人资料")
    @GetMapping("/mine/select")
    public JsonData mineSelect() {
        String json = sysUserService.mineSelect();
        return JsonData.success(json);
    }

    @ApiOperation("更新当前用户个人资料")
    @PutMapping("/mine/update")
    public JsonData mineUpdate(@ApiParam(value = "原始密码", required = true) @RequestParam String jsonInfo) {
        sysUserService.mineUpdate(jsonInfo);
        return JsonData.successOperate();
    }

    @ApiOperation("修改当前用户密码")
    @PutMapping("/pass/update")
    public JsonData passUpdate(@ApiParam(value = "原始密码", required = true) @RequestParam String oldPassword,
                               @ApiParam(value = "新的密码", required = true) @RequestParam String newPassword) {
        sysUserService.passUpdate(oldPassword, newPassword);
        return JsonData.successOperate();
    }

    @ApiOperation("分页获取用户登录记录")
    @GetMapping("/login/page")
    public JsonData loginPage(@Valid PageParam pageParam, BindingResult bindingResult) {
        PageDto<SysUserLogin> page = sysCenterService.loginPage( pageParam);
        return JsonData.success(page);
    }
}
