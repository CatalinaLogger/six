package com.maybe.ding;

import com.maybe.ding.common.util.DingApi;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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
@RequestMapping("/ding")
public class DingController {

    @Autowired
    private DingApi dingApi;

    @GetMapping("/test")
    public JSONObject test() {
        JSONObject jsonObject = dingApi.departmentList();
        System.out.println(jsonObject);
        return jsonObject;
    }
}
