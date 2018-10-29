package com.maybe.global.controller;

import com.maybe.ding.service.IDingDeptService;
import com.maybe.sys.common.dto.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/global/number")
public class GlobalNumberController {

    @Autowired
    private IDingDeptService dingDeptService;

    @GetMapping("/sync")
    public JsonData syncDept() {
        dingDeptService.synchronize();
        return JsonData.successOperate();
    }
}
