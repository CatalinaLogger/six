package com.maybe.jxc.controller;

import com.maybe.jxc.common.param.StoreParam;
import com.maybe.jxc.model.Store;
import com.maybe.jxc.service.IStoreService;
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

@Api(tags="仓库", description="仓库管理")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/datum/store")
public class StoreController {

    @Autowired
    private IStoreService storeService;

    @ApiOperation("获取仓库列表")
    @GetMapping("/select")
    public JsonData selectStore() {
        List<Store> list = storeService.select();
        return JsonData.success(list);
    }

    @ApiOperation("新增仓库")
    @PostMapping("/insert")
    public JsonData insertStore(@Valid StoreParam param, BindingResult bindingResult) {
        storeService.insert(param);
        return JsonData.successOperate();
    }

    @ApiOperation("修改仓库")
    @ApiImplicitParam(name = "id", value = "仓库ID", dataType = "int", paramType = "query", required = true, example = "1")
    @PutMapping("/update")
    public JsonData updateStore(@Valid StoreParam param, BindingResult bindingResult) {
        storeService.update(param);
        return JsonData.successOperate();
    }

    @ApiOperation("修改仓库状态")
    @PutMapping("/update/status")
    public JsonData updateStoreStatus(@ApiParam(value = "仓库ID", required = true) @RequestParam("storeId") Integer storeId,
                                      @ApiParam(value = "状态码", required = true) @RequestParam("status") Integer status) {
        storeService.updateStatus(storeId, status);
        return JsonData.successOperate();
    }

    @ApiOperation("删除仓库")
    @DeleteMapping("/delete")
    public JsonData delete(@ApiParam(value = "仓库ID", required = true) @RequestParam("storeId") Integer storeId) {
        storeService.delete(storeId);
        return JsonData.successOperate();
    }
}
