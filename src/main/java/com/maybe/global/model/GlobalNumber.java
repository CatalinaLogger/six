package com.maybe.global.model;

import lombok.Data;

import java.util.Date;

@Data
public class GlobalNumber {
    private String key;

    private String name;

    private String prefix;

    private String format;

    private Integer suffix;

    private Integer suffixSize;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}