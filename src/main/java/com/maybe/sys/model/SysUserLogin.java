package com.maybe.sys.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysUserLogin {
    private Integer id;

    private Integer userId;

    private String userName;

    private String loginIp;

    private Date loginTime;

    private String loginAddress;

    private String loginSystem;

    private String loginBrowser;

    private String loginBrowserVersion;

    public SysUserLogin() {
    }

    public SysUserLogin(Integer userId, String userName, String loginIp, Date loginTime, String loginAddress, String loginSystem, String loginBrowser, String loginBrowserVersion) {
        this.userId = userId;
        this.userName = userName;
        this.loginIp = loginIp;
        this.loginTime = loginTime;
        this.loginAddress = loginAddress;
        this.loginSystem = loginSystem;
        this.loginBrowser = loginBrowser;
        this.loginBrowserVersion = loginBrowserVersion;
    }
}