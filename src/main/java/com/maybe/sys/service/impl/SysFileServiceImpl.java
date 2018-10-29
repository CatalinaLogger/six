package com.maybe.sys.service.impl;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.config.SystemConfig;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.FastDFSClient;
import com.maybe.sys.service.ISysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SysFileServiceImpl implements ISysFileService {

    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private SystemConfig systemConfig;

    @Override
    public String upload(MultipartFile file) {
        try {
            return systemConfig.getFastUrl() + fastDFSClient.uploadFile(file);
        } catch (Exception e) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "上传图片失败");
        }
    }
}
