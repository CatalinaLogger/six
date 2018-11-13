package com.maybe.sys.common.param;

import com.maybe.sys.model.SysDept;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

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

    private List<SysDept> dept;

    private Integer status;

    private String operateIp;
}
