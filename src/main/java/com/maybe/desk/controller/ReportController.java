package com.maybe.desk.controller;

import com.maybe.desk.common.dto.WeeklyLevelDto;
import com.maybe.desk.common.param.WeeklyParam;
import com.maybe.desk.model.Weekly;
import com.maybe.desk.service.IReportService;
import com.maybe.sys.common.dto.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Api(tags="高效汇报", description="日报、周报、月报")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/desk/report")
public class ReportController {

    @Autowired
    private IReportService reportService;

    @ApiOperation("获取上周周报")
    @GetMapping("/weekly/select/before")
    public JsonData selectBefore() {
        Weekly weekly = reportService.selectBefore();
        return JsonData.success(weekly);
    }

    @ApiOperation("获取本周周报")
    @GetMapping("/weekly/select")
    public JsonData selectWeekly() {
        Weekly weekly = reportService.selectWeekly();
        return JsonData.success(weekly);
    }

    @ApiOperation("保存个人周报")
    @PostMapping("/weekly/insert")
    public JsonData insertWeekly(@Valid WeeklyParam param, BindingResult bindingResult) {
        reportService.insertWeekly(param);
        return JsonData.successOperate();
    }

    @ApiOperation("获取汇总周报")
    @GetMapping("/merge/weekly")
    public JsonData mergeWeekly(@DateTimeFormat(pattern="yyyy-MM-dd") @RequestParam(value = "date", required = false) Date date) {
        List<WeeklyLevelDto> list = reportService.mergeWeekly(date);
        return JsonData.success(list);
    }

}
