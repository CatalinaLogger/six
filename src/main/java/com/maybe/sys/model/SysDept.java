package com.maybe.sys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysDept {
    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer seq;

    private String remark;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;

    private List<SysUser> lead;
    @JsonIgnore
    private Integer userId;
}