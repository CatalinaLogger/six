package com.maybe.ding.controller;

import com.maybe.ding.service.IDingUserService;
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
@RequestMapping("/system/user")
public class DingUserController {

    @Autowired
    private IDingUserService dingUserService;

    @GetMapping("/sync")
    public JsonData syncUser() {
        dingUserService.synchronize();
        return JsonData.successOperate();
    }
}
