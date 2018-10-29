package com.maybe.sys.common.dto;

import com.maybe.sys.model.SysMenu;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/4/30
 */
@Data
public class MenuLevelDto extends SysMenu {

    private List<MenuLevelDto> children = new ArrayList<MenuLevelDto>();

    public static MenuLevelDto adapt(SysMenu menu) {
        MenuLevelDto dto = new MenuLevelDto();
        BeanUtils.copyProperties(menu, dto);
        return dto;
    }
}
