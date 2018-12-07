package com.maybe.jxc.model;

import lombok.Data;

import java.util.Date;

@Data
public class Property {
    private Integer id;

    private Integer groupId;

    private String name;

    private Integer seq;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}