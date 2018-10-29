package com.maybe.work.controller;

import com.maybe.sys.common.dto.JsonData;
import com.maybe.work.service.IWorkDraftService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags="流程表单", description="管理流程表单草稿")
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/work/draft")
public class WorkDraftController {

    @Autowired
    private IWorkDraftService workDraftService;

    @ApiOperation("根据流程定义ID获取当前用户保存的草稿")
    @GetMapping("/select")
    public JsonData selectDraft(@ApiParam(value = "流程定义ID", required = true) @RequestParam("defineId") String defineId){
        String jsonInfo = workDraftService.select(defineId);
        return JsonData.success(jsonInfo);
    }

    @ApiOperation("根据流程定义ID保存当前用户保存的草稿")
    @PostMapping("/insert")
    public JsonData insertDraft(@ApiParam(value = "流程定义ID", required = true) @RequestParam("defineId") String defineId,
                                @ApiParam("表单数据（JSON字符串）") @RequestParam("jsonData") String jsonData){
        workDraftService.insert(defineId, jsonData);
        return JsonData.successOperate();
    }

    @ApiOperation("根据流程定义ID删除当前用户保存的草稿")
    @DeleteMapping("/delete")
    public JsonData deleteDraft(@ApiParam(value = "流程定义ID", required = true)@RequestParam("defineId") String defineId){
        workDraftService.delete(defineId);
        return JsonData.successOperate();
    }

}
