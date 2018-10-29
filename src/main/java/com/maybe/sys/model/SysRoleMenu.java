package com.maybe.sys.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysRoleMenu {
    private Integer id;

    private Integer roleId;

    private Integer menuId;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}