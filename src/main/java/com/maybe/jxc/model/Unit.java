package com.maybe.jxc.model;

import lombok.Data;

import java.util.Date;
@Data
public class Unit {
    private Integer id;

    private String name;

    private String deputy;

    private String conversion;

    private Integer num;

    private Integer category;

    private Integer parentId;

    private Integer seq;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}