package com.maybe.desk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class Weekly {
    @JsonIgnore
    private Integer id;
    @JsonIgnore
    private Integer year;
    @JsonIgnore
    private Integer week;

    private String done;

    private String sum;

    private String plan;

    private String help;

    private String remark;
    @JsonIgnore
    private String operateIp;
    @JsonIgnore
    private Integer operateId;

    private String operateName;

    private Date operateTime;
    @JsonIgnore
    private String level;
}