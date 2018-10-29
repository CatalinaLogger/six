package com.maybe.global.model;

import lombok.Data;

import java.util.Date;

@Data
public class GlobalConf {
    private Integer id;

    private String code;

    private String name;

    private Integer seq;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}