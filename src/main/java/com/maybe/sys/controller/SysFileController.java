package com.maybe.sys.controller;

import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.service.ISysFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags="文件管理", description="文件上传、保存、管理")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/system/file")
public class SysFileController {

    @Autowired
    private ISysFileService sysFileService;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public JsonData upload(@ApiParam(value = "文件", required = true) @RequestParam("file") MultipartFile file) {
        String path = sysFileService.upload(file);
        return JsonData.success(path);
    }
}
