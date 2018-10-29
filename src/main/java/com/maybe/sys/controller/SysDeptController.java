package com.maybe.sys.controller;

import com.maybe.sys.common.dto.DeptLevelDto;
import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.param.DeptParam;
import com.maybe.sys.model.SysDept;
import com.maybe.sys.service.ISysDeptService;
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

@Api(tags="部门管理", description="维护部门组织架构")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/system/dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysTreeService sysTreeService;

    @ApiOperation("新增部门")
    @PostMapping("/insert")
    public JsonData insertDept(@Valid DeptParam param, BindingResult bindingResult) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(param, sysDept);
        sysDeptService.insert(sysDept);
        return JsonData.successOperate();
    }

    @ApiOperation("修改部门")
    @ApiImplicitParam(name = "id", value = "部门ID", dataType = "int", paramType = "query", required = true, example = "1")
    @PutMapping("/update")
    public JsonData updateDept(@Valid DeptParam param, BindingResult bindingResult) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(param, sysDept);
        sysDeptService.update(sysDept);
        return JsonData.successOperate();
    }

    @ApiOperation("删除部门")
    @DeleteMapping("/delete")
    public JsonData deleteDept(@ApiParam(value = "部门ID", required = true, example = "1") @RequestParam(value = "deptId", defaultValue = "") Integer deptId) {
        sysDeptService.delete(deptId);
        return JsonData.successOperate();
    }

    @ApiOperation("获取当前用户所在部门")
    @GetMapping("/mine")
    public JsonData selectDept() {
        return JsonData.success(sysDeptService.mine());
    }

    @ApiOperation("获取当前用户所在部门的子部门结构树")
    @GetMapping("/tree")
    public JsonData deptTree() {
        List<DeptLevelDto> dtoList = sysTreeService.deptTreeByUser();
        return JsonData.success(dtoList);
    }
}
