package com.maybe.jxc.controller;

import com.maybe.jxc.common.param.GroupParam;
import com.maybe.jxc.common.param.PropertyParam;
import com.maybe.jxc.model.Property;
import com.maybe.jxc.model.PropertyGroup;
import com.maybe.jxc.service.IPropertyService;
import com.maybe.sys.common.dto.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(tags="属性", description="产品属性设置")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/datum/property")
public class PropertyController {

    @Autowired
    private IPropertyService propertyService;

    @ApiOperation("获取属性列表")
    @GetMapping("/group/list")
    public JsonData selectGroupList() {
        List<PropertyGroup> list = propertyService.selectGroupList();
        return JsonData.success(list);
    }

    @ApiOperation("新增属性")
    @PostMapping("/group/insert")
    public JsonData insertGroup(@Valid GroupParam param, BindingResult bindingResult) {
        propertyService.insertGroup(param);
        return JsonData.successOperate();
    }

    @ApiOperation("修改属性")
    @ApiImplicitParam(name = "id", value = "属性ID", dataType = "int", paramType = "query", required = true, example = "1")
    @PutMapping("/group/update")
    public JsonData updateGroup(@Valid GroupParam param, BindingResult bindingResult) {
        propertyService.updateGroup(param);
        return JsonData.successOperate();
    }

    @ApiOperation("删除属性")
    @DeleteMapping("/group/delete")
    public JsonData deleteGroup(@ApiParam(value = "属性ID", required = true) @RequestParam("groupId") Integer groupId) {
        propertyService.deleteGroup(groupId);
        return JsonData.successOperate();
    }

    @ApiOperation("上移属性")
    @PutMapping("/group/up")
    public JsonData upGroup(@ApiParam(value = "属性ID", required = true) @RequestParam("groupId") Integer groupId) {
        propertyService.upGroup(groupId);
        return JsonData.successOperate();
    }

    @ApiOperation("下移属性")
    @PutMapping("/group/down")
    public JsonData downGroup(@ApiParam(value = "属性ID", required = true) @RequestParam("groupId") Integer groupId) {
        propertyService.downGroup(groupId);
        return JsonData.successOperate();
    }

    @ApiOperation("获取属性值列表")
    @GetMapping("/list")
    public JsonData selectGroupList(@ApiParam(value = "属性ID", required = true) @RequestParam("groupId") Integer groupId) {
        List<Property> list = propertyService.selectList(groupId);
        return JsonData.success(list);
    }

    @ApiOperation("新增属性值")
    @ApiImplicitParam(name = "groupId", value = "属性ID", dataType = "int", paramType = "query", required = true, example = "1")
    @PostMapping("/insert")
    public JsonData insert(@Valid PropertyParam param, BindingResult bindingResult) {
        propertyService.insert(param);
        return JsonData.successOperate();
    }

    @ApiOperation("修改属性值")
    @ApiImplicitParam(name = "id", value = "属性值ID", dataType = "int", paramType = "query", required = true, example = "1")
    @PutMapping("/update")
    public JsonData update(@Valid PropertyParam param, BindingResult bindingResult) {
        propertyService.update(param);
        return JsonData.successOperate();
    }

    @ApiOperation("删除属性值")
    @DeleteMapping("/delete")
    public JsonData delete(@ApiParam(value = "属性值ID", required = true) @RequestParam("propertyId") Integer propertyId) {
        propertyService.delete(propertyId);
        return JsonData.successOperate();
    }

    @ApiOperation("上移属性值")
    @PutMapping("/up")
    public JsonData up(@ApiParam(value = "属性值ID", required = true) @RequestParam("propertyId") Integer propertyId) {
        propertyService.up(propertyId);
        return JsonData.successOperate();
    }

    @ApiOperation("下移属性值")
    @PutMapping("/down")
    public JsonData down(@ApiParam(value = "属性值ID", required = true) @RequestParam("propertyId") Integer propertyId) {
        propertyService.down(propertyId);
        return JsonData.successOperate();
    }
}
