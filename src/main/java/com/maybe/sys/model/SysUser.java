package com.maybe.sys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysUser {
    private Integer id;

    private String name;

    private String nickName;

    private Integer sex;

    private Date birthday;

    private String phone;

    private String mail;

    private String say;
    @JsonIgnore
    private String avatar;

    private String weChat;

    private String qq;
    @JsonIgnore
    private String openWe;
    @JsonIgnore
    private String openQq;

    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String resetCode;

    private Integer parentId;

    private String parentName;

    private String parentUsername;

    private String level;

    private Integer status;

    private String remark;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;

    private String jsonInfo;

    private List<SysDept> dept;
    @JsonIgnore
    private Integer deptId;

}