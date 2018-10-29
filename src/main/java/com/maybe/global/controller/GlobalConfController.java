package com.maybe.global.controller;

import com.maybe.global.common.dto.ConfLevelDto;
import com.maybe.global.common.param.ConfDataParam;
import com.maybe.global.common.param.ConfParam;
import com.maybe.global.model.GlobalConf;
import com.maybe.global.service.IGlobalConfService;
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

@Api(tags="通用配置", description="配置类别、配置数据")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/global/conf")
public class GlobalConfController {

    @Autowired
    private IGlobalConfService globalConfService;

    @ApiOperation("获取配置类别列表")
    @GetMapping("/list")
    public JsonData confList() {
        List<GlobalConf> list = globalConfService.findAll();
        return JsonData.success(list);
    }

    @ApiOperation("新增配置类别")
    @PostMapping("/insert")
    public JsonData insertConf(@Valid ConfParam param, BindingResult bindingResult) {
        globalConfService.insert(param);
        return JsonData.successOperate();
    }

    @ApiOperation("修改配置类别")
    @ApiImplicitParam(name = "id", value = "配置类别ID", dataType = "int", paramType = "query", required = true, example="1")
    @PutMapping("/update")
    public JsonData updateConf(@Valid ConfParam param, BindingResult bindingResult) {
        globalConfService.update(param);
        return JsonData.successOperate();
    }

    @ApiOperation("删除配置类别")
    @DeleteMapping("/delete")
    public JsonData deleteConf(@ApiParam(value = "配置类别ID", required = true, example = "1") @RequestParam(value = "confId", defaultValue = "") Integer confId) {
        globalConfService.delete(confId);
        return JsonData.successOperate();
    }

    @ApiOperation("新增配置数据")
    @PostMapping("/data/insert")
    public JsonData insertConfData(@Valid ConfDataParam param, BindingResult bindingResult) {
        globalConfService.insertData(param);
        return JsonData.successOperate();
    }

    @ApiOperation("修改配置数据")
    @ApiImplicitParam(name = "id", value = "配置数据ID", dataType = "int", paramType = "query", required = true, example="1")
    @PutMapping("/data/update")
    public JsonData updateConfData(@Valid ConfDataParam param, BindingResult bindingResult) {
        globalConfService.updateData(param);
        return JsonData.successOperate();
    }

    @ApiOperation("删除配置数据")
    @DeleteMapping("/data/delete")
    public JsonData deleteConfData(@ApiParam(value = "配置数据ID", required = true, example = "1") @RequestParam(value = "confDataId", defaultValue = "") Integer confDataId) {
        globalConfService.deleteData(confDataId);
        return JsonData.successOperate();
    }

    @ApiOperation("根据配置类别编码获取配置数据树")
    @GetMapping("/data/tree")
    public JsonData dataTreeByConfCode(@ApiParam(value = "配置类别编码", required = true) @RequestParam("code") String code) {
        List<ConfLevelDto> dtoList = globalConfService.dataTreeByConfCode(code);
        return JsonData.success(dtoList);
    }

}
