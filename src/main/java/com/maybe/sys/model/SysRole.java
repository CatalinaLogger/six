package com.maybe.sys.model;

import lombok.Data;
import java.util.Date;

@Data
public class SysRole {
    private Integer id;

    private String code;

    private String name;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}