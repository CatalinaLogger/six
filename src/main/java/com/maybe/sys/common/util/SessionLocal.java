package com.maybe.sys.common.util;

import com.maybe.sys.common.param.LoginUser;

/**
 * @author jin
 * @description:
 * @date 2018/5/12
 */
public class SessionLocal {

    private static ThreadLocal<LoginUser> local = new ThreadLocal<>();
    /**
     * 设置登录用户信息
     */
    public static void setUser(LoginUser user){
        local.set(user);
    }
    /**
     * 获取登录用户信息
     */
    public static LoginUser getUser(){
        return local.get();
    }

    public static void remove(){
        local.remove();
    }
}
