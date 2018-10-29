package com.maybe.sys.controller;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.param.RoleParam;
import com.maybe.sys.model.SysRole;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysRoleService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags="角色管理", description="管理角色权限及用户")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @ApiOperation("新增角色")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "code", value = "角色编码", dataType = "string", paramType = "query", required = true),
        @ApiImplicitParam(name = "name", value = "角色名称", dataType = "string", paramType = "query", required = true),
        @ApiImplicitParam(name = "menuKeys", value = "菜单ID数组，示例：1,2,3", dataType = "string", paramType = "query", required = true)
    })
    @PostMapping("/insert")
    public JsonData insertRole(@Valid RoleParam param, BindingResult bindingResult) {
        sysRoleService.insert(param);
        return JsonData.successOperate();
    }

    @ApiOperation("修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "角色编码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "name", value = "角色名称", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "menuKeys", value = "菜单ID数组，示例：1,2,3", dataType = "string", paramType = "query", required = true)
    })
    @PutMapping("/update")
    public JsonData updateRole(@Valid RoleParam param, BindingResult bindingResult) {
        sysRoleService.update(param);
        return JsonData.successOperate();
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/delete")
    public JsonData deleteRole(@ApiParam(value = "角色ID", required = true, example = "1") @RequestParam(value = "roleId", defaultValue = "") Integer roleId) {
        sysRoleService.delete(roleId);
        return JsonData.successOperate();
    }

    @ApiOperation("获取全部角色列表")
    @GetMapping("/list")
    public JsonData roleList() {
        List<SysRole> list = sysRoleService.findAll();
        return JsonData.success(list);
    }

    @ApiOperation("获取当前用户所拥有的角色列表")
    @GetMapping("/mine")
    public JsonData roleMine() {
        List<SysRole> list = sysRoleService.roleListByUser();
        return JsonData.success(list);
    }

    @ApiOperation("获取角色所绑定的菜单ID列表")
    @GetMapping("/menu/keys")
    public JsonData menuKeys(@ApiParam(value = "角色ID", required = true, example = "1") @RequestParam("roleId") Integer roleId) {
        List<Integer> list = sysRoleService.findMenuKeysByRoleId(roleId);
        return JsonData.success(list);
    }

    @ApiOperation("分页获取角色所绑定的用户")
    @GetMapping("/user/page/bound")
    public JsonData pageBound(@ApiParam(value = "角色ID", required = true, example = "1") @RequestParam(value = "roleId", defaultValue = "") Integer roleId,
                              @ApiParam("用户查询条件（姓名/手机/邮箱）") @RequestParam(value = "query", required = false) String query,
                              @Valid PageParam param, BindingResult bindingResult) {
        if (roleId == null) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "请提供角色ID");
        }
        query = ObjectUtils.isEmpty(query) ? null : query;
        PageDto<SysUser> page = sysRoleService.pageBoundByRoleId(roleId, query, param);
        return JsonData.success(page);
    }

    @ApiOperation("分页获取角色未绑定的用户")
    @GetMapping("/user/page/unbound")
    public JsonData pageUnbound(@ApiParam(value = "角色ID", required = true, example = "1") @RequestParam(value = "roleId", defaultValue = "") Integer roleId,
                                @ApiParam("用户查询条件（姓名/手机/邮箱）")@RequestParam(value = "query", required = false) String query,
                                @Valid PageParam param, BindingResult bindingResult) {
        if (roleId == null) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "请提供角色ID");
        }
        query = ObjectUtils.isEmpty(query) ? null : query;
        PageDto<SysUser> page = sysRoleService.pageUnboundByRoleId(roleId, query, param);
        return JsonData.success(page);
    }

    @ApiOperation("绑定用户到角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID", dataType = "int", paramType = "query", required = true, example = "1"),
            @ApiImplicitParam(name = "userKeys", value = "用户ID数组，示例：1,2,3", dataType = "string", paramType = "query", required = true)
    })
    @PostMapping("/user/binding")
    public JsonData binding(@Valid RoleParam param, BindingResult bindingResult) {
        sysRoleService.binding(param);
        return JsonData.successOperate();
    }

    @ApiOperation("解绑用户到角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID", dataType = "int", paramType = "query", required = true, example = "1"),
            @ApiImplicitParam(name = "userKeys", value = "用户ID数组，示例：1,2,3", dataType = "string", paramType = "query", required = true)
    })
    @PostMapping("/user/untying")
    public JsonData untying(@Valid RoleParam param, BindingResult bindingResult) {
        sysRoleService.untying(param);
        return JsonData.successOperate();
    }
}
