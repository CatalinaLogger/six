package com.maybe.sys.model;

import lombok.Data;

import java.util.Date;
@Data
public class SysDept {
    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer leaderId;

    private String leaderName;

    private String leaderUsername;

    private Integer helperId;

    private String helperName;

    private String helperUsername;

    private Integer seq;

    private String remark;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;
}