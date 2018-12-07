package com.maybe.jxc.controller;

import com.maybe.jxc.common.dto.UnitLevelDto;
import com.maybe.jxc.common.param.UnitParam;
import com.maybe.jxc.service.IUnitService;
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


@Api(tags="计量单位", description="计量单位设置")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/datum/unit")
public class UnitController {

    @Autowired
    private IUnitService unitService;

    @ApiOperation("获取单位列表")
    @GetMapping("/list")
    public JsonData selectUnitList(@ApiParam(value = "单位分类", required = true) @RequestParam("category") Integer category) {
        List<UnitLevelDto> list = unitService.selectList(category);
        return JsonData.success(list);
    }

    @ApiOperation("新增单位")
    @PostMapping("/insert")
    public JsonData insert(@Valid @RequestBody UnitParam param, BindingResult bindingResult) {
        unitService.insert(param);
        return JsonData.successOperate();
    }

    @ApiOperation("修改单位")
    @PutMapping("/update")
    public JsonData update(@Valid @RequestBody UnitParam param, BindingResult bindingResult) {
        unitService.update(param);
        return JsonData.successOperate();
    }

    @ApiOperation("删除单位")
    @DeleteMapping("/delete")
    public JsonData delete(@ApiParam(value = "单位ID", required = true) @RequestParam("unitId") Integer unitId) {
        unitService.delete(unitId);
        return JsonData.successOperate();
    }

    @ApiOperation("上移单位")
    @PutMapping("/up")
    public JsonData up(@ApiParam(value = "单位ID", required = true) @RequestParam("unitId") Integer unitId) {
        unitService.up(unitId);
        return JsonData.successOperate();
    }

    @ApiOperation("下移单位")
    @PutMapping("/down")
    public JsonData down(@ApiParam(value = "单位ID", required = true) @RequestParam("unitId") Integer unitId) {
        unitService.down(unitId);
        return JsonData.successOperate();
    }
}
