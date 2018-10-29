package com.maybe.sys.controller;

import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.dto.LoginUserDto;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.param.UserParam;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysRoleService;
import com.maybe.sys.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Api(tags="用户管理", description="维护用户信息、状态")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/system/user")
public class SysUserController {
    
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;

    @ApiOperation("新增用户")
    @ApiImplicitParam(name = "roleKeys", value = "角色ID数组，示例：1,2,3", dataType = "string", paramType = "query", required = true)
    @PostMapping("/insert")
    public JsonData insertUser(@Valid UserParam param, BindingResult bindingResult) {
        sysUserService.insert(param);
        return JsonData.successOperate();
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/mine")
    public JsonData selectUser() {
        SysUser user = sysUserService.select();
        return JsonData.success(user);
    }

    @ApiOperation("修改用户")
    @ApiImplicitParam(name = "roleKeys", value = "角色ID数组，示例：1,2,3", dataType = "string", paramType = "query", required = true)
    @PutMapping("/update")
    public JsonData updateUser(@Valid UserParam param, BindingResult bindingResult) {
        sysUserService.update(param);
        return JsonData.successOperate();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/delete")
    public JsonData deleteUser(@ApiParam(value = "用户ID", required = true, example = "1") @RequestParam(value = "userId", defaultValue = "") Integer userId) {
        sysUserService.delete(userId);
        return JsonData.successOperate();
    }

    @ApiOperation("批量删除用户")
    @ApiImplicitParam(name = "userKeys", value = "用户ID数组，示例：1,2,3", dataType = "string", paramType = "query", required = true)
    @DeleteMapping("/delete/batch")
    public JsonData deleteBatchUser(@ApiParam(hidden = true) @RequestParam(value = "userKeys", defaultValue = "") List<Integer> userKeys) {
        sysUserService.deleteBatch(userKeys);
        return JsonData.successOperate();
    }

    @ApiOperation("分页获取用户列表")
    @GetMapping("/page")
    public JsonData page(@ApiParam(value = "部门ID", example = "1") @RequestParam(value = "deptId", defaultValue = "") Integer deptId,
                         @ApiParam("查询条件") @RequestParam(value = "query", required = false) String query,
                         @Valid  PageParam param, BindingResult bindingResult) {
        query = ObjectUtils.isEmpty(query) ? null : query;
        PageDto<SysUser> page = sysUserService.findUserPageByDeptId(deptId, query, param);
        return JsonData.success(page);
    }

    @ApiOperation("检查用户是否已存在")
    @GetMapping("/check")
    public JsonData check(@ApiParam(value = "用户ID", example = "1")  @RequestParam(value = "userId", defaultValue = "") Integer userId,
                          @ApiParam(value = "属性名", required = true) @RequestParam("field") String field,
                          @ApiParam(value = "属性值", required = true) @RequestParam("value") String value) {
        boolean bool = sysUserService.checkExist(userId, field, value);
        return JsonData.success(bool);
    }

    @ApiOperation("获取登录用户初始化数据，角色、权限、头像等")
    @GetMapping("/info")
    public JsonData info(@ApiParam(value = "授权令牌", required = true) @RequestParam("token") String token) {
        LoginUserDto dto = sysUserService.getLoginUserByToken(token);
        return JsonData.success(dto);
    }

    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public JsonData logout() {
        sysUserService.logoutByKey();
        return JsonData.success();
    }

    @ApiOperation("获取用户所拥有的角色列表")
    @GetMapping("/role/keys")
    public JsonData roleKeys(@ApiParam(value = "用户ID", required = true, example = "1") @RequestParam("userId") Integer userId) {
        List<Integer> list = sysRoleService.findRoleKeysByUserId(userId);
        return JsonData.success(list);
    }

    @ApiOperation("设置当前用户头像")
    @PostMapping("/avatar")
    public JsonData avatar(@ApiParam(value = "图片文件", required = true) @RequestParam("file") MultipartFile file) {
        sysUserService.setAvatar(file);
        return JsonData.successOperate();
    }

    @ApiOperation("根据角色编码获取用户列表")
    @GetMapping("/list/role")
    public JsonData userListByRoleCode(@ApiParam(value = "角色编码", required = true) @RequestParam("roleCode") String roleCode) {
        List<SysUser> list = sysUserService.userListByRoleCode(roleCode);
        return JsonData.success(list);
    }
}
