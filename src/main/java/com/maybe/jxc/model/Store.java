package com.maybe.jxc.model;

import lombok.Data;

import java.util.Date;

@Data
public class Store {
    private Integer id;

    private String code;

    private String name;

    private String address;

    private Integer dutyId;

    private String dutyName;

    private String dutyPhone;

    private String dutyLink;

    private Integer status;

    private Integer seq;

    private String remark;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}