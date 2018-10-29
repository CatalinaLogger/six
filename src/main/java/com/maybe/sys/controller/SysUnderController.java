package com.maybe.sys.controller;

import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysUnderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags="我的下属", description="管理用户上下级关系")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/system/under")
public class SysUnderController {

    @Autowired
    private ISysUnderService sysUnderService;

    @ApiOperation("获取用户的下级用户列表")
    @GetMapping("/with/list")
    public JsonData withUserList(@ApiParam(value = "用户ID", required = true, example = "1") @RequestParam(value = "userId", defaultValue = "") Integer userId) {
        List<SysUser> userList = sysUnderService.withListOfUser(userId);
        return JsonData.success(userList);
    }

    @ApiOperation("获取没有上级的用户列表")
    @GetMapping("/none/page")
    public JsonData noneUserPage(@ApiParam("用户查询条件") @RequestParam(value = "query", required = false) String query,
                                @Valid PageParam param, BindingResult bindingResult) {
        query = ObjectUtils.isEmpty(query) ? null : query;
        PageDto<SysUser> userPage = sysUnderService.userPageOfNone(query, param);
        return JsonData.success(userPage);
    }

    @ApiOperation("给用户绑定下级用户")
    @ApiImplicitParam(name = "userKeys", value = "下级用户ID数组，示例：1,2,3", dataType = "string", paramType = "query", required = true)
    @PutMapping("/push")
    public JsonData underPush(@ApiParam(value = "用户ID", required = true, example = "1") @RequestParam(value = "userId", required = false) Integer userId,
                              @ApiParam(hidden = true) @RequestParam("userKeys") List<Integer> userKeys) {
        sysUnderService.underPush(userId, userKeys);
        return JsonData.successOperate();
    }

    @ApiOperation("给用户解绑下级用户")
    @ApiImplicitParam(name = "userKeys", value = "下级用户ID数组，示例：1,2,3", dataType = "string", paramType = "query", required = true)
    @PutMapping("/pull")
    public JsonData underPull(@ApiParam(value = "用户ID", required = true, example = "1") @RequestParam(value = "userId", required = false) Integer userId,
                              @ApiParam(hidden = true) @RequestParam("userKeys") List<Integer> userKeys) {
        sysUnderService.underPull(userId, userKeys);
        return JsonData.successOperate();
    }
}
