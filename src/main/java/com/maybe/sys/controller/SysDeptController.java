package com.maybe.sys.controller;

import com.maybe.sys.common.dto.DeptLevelDto;
import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.param.DeptParam;
import com.maybe.sys.model.SysDept;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysDeptService;
import com.maybe.sys.service.ISysTreeService;
import io.swagger.annotations.*;
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
        sysDeptService.insert(param);
        return JsonData.successOperate();
    }

    @ApiOperation("修改部门")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "部门ID", dataType = "int", paramType = "query", required = true, example = "1"),
        @ApiImplicitParam(name = "leadKeys", value = "主管ID数组", dataType = "string", paramType = "query")
    })
    @PutMapping("/update")
    public JsonData updateDept(@Valid DeptParam param, BindingResult bindingResult) {
        sysDeptService.update(param);
        return JsonData.successOperate();
    }

    @ApiOperation("删除部门")
    @DeleteMapping("/delete")
    public JsonData deleteDept(@ApiParam(value = "部门ID", required = true, example = "1") @RequestParam(value = "deptId", defaultValue = "") Integer deptId) {
        sysDeptService.delete(deptId);
        return JsonData.successOperate();
    }

    @ApiOperation("获取部门结构树")
    @GetMapping("/tree")
    public JsonData deptTree() {
        List<DeptLevelDto> dtoList = sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }

    @ApiOperation("获取部门主管列表")
    @GetMapping("/lead/list")
    public JsonData LeadList(@ApiParam(value = "部门ID", required = true, example = "1") @RequestParam("deptId") Integer deptId) {
        List<SysUser> list = sysDeptService.findLeadListByDeptId(deptId);
        return JsonData.success(list);
    }

}
