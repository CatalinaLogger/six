package com.maybe.sys.model;

import lombok.Data;
import java.util.Date;

@Data
public class SysMenu {
    private Integer id;

    private String name;

    private String path;

    private String redirect;

    private String icon;

    private Integer hidden;

    private Integer cache;

    private Integer parentId;

    private String level;

    private Integer seq;

    private Integer type;

    private String remark;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}