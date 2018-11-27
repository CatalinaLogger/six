package com.maybe.flow.dao;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.model.FlowWait;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.model.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowWaitMapper {

    int waitCountByUserAndRole(@Param("username") String username, @Param("roles") List<SysRole> roles,  @Param("param") FlowParam param, @Param("page") PageParam page);

    List<FlowWait> waitPageByUserAndRole(@Param("username") String username, @Param("roles") List<SysRole> roles,  @Param("param") FlowParam param, @Param("page") PageParam page);

    List<String> userListByProcessId(@Param("processId") String processId);
}