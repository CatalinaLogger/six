package com.maybe.sys.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysDeptUser {
    private Integer id;

    private Integer deptId;

    private Integer userId;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}