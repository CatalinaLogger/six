package com.maybe.sys.common.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jin
 * @description: 登录成功数据
 * @date 2018/5/6
 */
@Data
@Component
public class LoginUserDto {

    private String name;
    private String avatar;
    private List<String> roles;
    private List<MenuLevelDto> menus;
}
