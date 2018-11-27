package com.maybe.sys.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class SystemConfig {

    @Value("${system.avatar}")
    private String avatar;
    @Value("${system.fast-url}")
    private String fastUrl;
    @Value("${system.active}")
    private String active;

}

