package com.maybe.sys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.Date;

@Data
public class SysRole {
    private Integer id;

    private String code;

    private String name;

    private Integer parentId;

    private Integer edit;
    @JsonIgnore
    private String operateIp;
    @JsonIgnore
    private Integer operateId;

    private String operateName;

    private Date operateTime;
    @JsonIgnore
    private Integer deptId;
}