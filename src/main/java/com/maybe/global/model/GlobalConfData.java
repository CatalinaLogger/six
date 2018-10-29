package com.maybe.global.model;

import lombok.Data;

import java.util.Date;

@Data
public class GlobalConfData {
    private Integer id;

    private Integer confId;

    private String confCode;

    private String code;

    private String name;

    private Integer value;

    private Integer parentId;

    private String level;

    private Integer seq;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}