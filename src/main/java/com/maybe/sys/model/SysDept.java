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
    @JsonIgnore
    private String level;

    private Integer seq;

    private String remark;
    @JsonIgnore
    private String operateIp;
    @JsonIgnore
    private Integer operateId;

    private String operateName;

    private Date operateTime;

    private List<SysUser> lead;
    @JsonIgnore
    private Integer userId;
}