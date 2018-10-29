package com.maybe.sys.controller;

import com.maybe.sys.common.dto.MenuLevelDto;
import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.param.MenuParam;
import com.maybe.sys.model.SysMenu;
import com.maybe.sys.service.ISysMenuService;
import com.maybe.sys.service.ISysTreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags="菜单管理", description="维护目录、菜单、按钮等")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysTreeService sysTreeService;

    @ApiOperation("新增菜单")
    @PostMapping("/insert")
    public JsonData insertMenu(@Valid MenuParam param, BindingResult bindingResult) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(param, sysMenu);
        sysMenuService.insert(sysMenu);
        return JsonData.successOperate();
    }

    @ApiOperation("修改菜单")
    @ApiImplicitParam(name = "id", value = "菜单ID", dataType = "int", paramType = "query", required = true, example = "1")
    @PutMapping("/update")
    public JsonData updateMenu(@Valid MenuParam param, BindingResult bindingResult) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(param, sysMenu);
        sysMenuService.update(sysMenu);
        return JsonData.successOperate();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/delete")
    public JsonData deleteMenu(@ApiParam(value = "菜单ID", required = true, example = "1") @RequestParam(value = "menuId", defaultValue = "") Integer menuId) {
        sysMenuService.delete(menuId);
        return JsonData.successOperate();
    }

    @ApiOperation("获取菜单树")
    @GetMapping("/tree")
    public JsonData menuTree() {
        List<MenuLevelDto> dtoList = sysTreeService.menuTree();
        return JsonData.success(dtoList);
    }
}
