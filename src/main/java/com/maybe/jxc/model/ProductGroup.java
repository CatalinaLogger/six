package com.maybe.jxc.model;

import lombok.Data;

import java.util.Date;

@Data
public class ProductGroup {
    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer seq;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}