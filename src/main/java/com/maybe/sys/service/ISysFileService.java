package com.maybe.sys.service;

import org.springframework.web.multipart.MultipartFile;

public interface ISysFileService {

    String upload(MultipartFile file);
}
