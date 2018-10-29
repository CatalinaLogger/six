package com.maybe.sys.common.param;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author jin
 * @description:
 * @date 2018/5/12
 */
@Data
@Component
public class LoginUser {

    private Integer id;

    private String name;

    private String phone;

    private String mail;

    private String username;

    private Integer deptId;

    private String deptName;

    private Integer status;

    private String operateIp;
}
