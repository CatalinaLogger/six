package com.maybe.work.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "work_furlough")
public class Furlough {
    @Id
    private String procInstId;
    @Column(length = 1000)
    private String jobContent;

    private Date startTime;

    private Date endTime;

    private Integer duration;

    private Integer userId;
    
    private String userName;

    private Integer deptId;

    private String deptName;

    private Integer status;
}
