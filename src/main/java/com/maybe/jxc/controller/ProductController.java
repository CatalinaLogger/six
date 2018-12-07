package com.maybe.jxc.controller;

import com.maybe.jxc.common.dto.ProductGroupLevelDto;
import com.maybe.jxc.common.param.GroupParam;
import com.maybe.jxc.service.IProductService;
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


@Api(tags="产品", description="产品分类及产品管理")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/datum/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @ApiOperation("获取产品分类树")
    @GetMapping("/group/tree")
    public JsonData selectGroupTree() {
        List<ProductGroupLevelDto> list = productService.selectGroupTree();
        return JsonData.success(list);
    }

    @ApiOperation("新增产品分类")
    @PostMapping("/group/insert")
    public JsonData insertGroup(@Valid GroupParam param, BindingResult bindingResult) {
        productService.insertGroup(param);
        return JsonData.successOperate();
    }

    @ApiOperation("修改产品分类")
    @ApiImplicitParam(name = "id", value = "产品分类ID", dataType = "int", paramType = "query", required = true, example = "1")
    @PutMapping("/group/update")
    public JsonData updateGroup(@Valid GroupParam param, BindingResult bindingResult) {
        productService.updateGroup(param);
        return JsonData.successOperate();
    }

    @ApiOperation("删除产品分类")
    @DeleteMapping("/group/delete")
    public JsonData deleteGroup(@ApiParam(value = "产品分类ID", required = true) @RequestParam("groupId") Integer groupId) {
        productService.deleteGroup(groupId);
        return JsonData.successOperate();
    }

    @ApiOperation("上移产品分类")
    @PutMapping("/group/up")
    public JsonData upGroup(@ApiParam(value = "产品分类ID", required = true) @RequestParam("groupId") Integer groupId) {
        productService.upGroup(groupId);
        return JsonData.successOperate();
    }

    @ApiOperation("下移产品分类")
    @PutMapping("/group/down")
    public JsonData downGroup(@ApiParam(value = "产品分类ID", required = true) @RequestParam("groupId") Integer groupId) {
        productService.downGroup(groupId);
        return JsonData.successOperate();
    }
}
