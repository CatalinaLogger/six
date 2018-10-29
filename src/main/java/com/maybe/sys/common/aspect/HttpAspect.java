package com.maybe.sys.common.aspect;

import com.auth0.jwt.interfaces.Claim;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.LoginUser;
import com.maybe.sys.common.util.JWToken;
import com.maybe.sys.common.util.NetworkUtil;
import com.maybe.sys.common.util.RedisKeyPrefix;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class HttpAspect {

    @Autowired
    private ISysUserService sysUserService;
    @Resource
    private RedisTemplate<String, LoginUser> redisTemplate;

    @Pointcut("execution(public * com.maybe.*.controller.*.*(..))")
    public void pointCut(){
    }

    @Before("pointCut()")
    public void doBefore(){
        LoginUser loginUser;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ipAddress = NetworkUtil.getIpAddress(request);
        String token = request.getHeader("X-Token");
        Map<String, Claim> map;
        try {
            map = JWToken.verifyToken(token);
        } catch (Exception e) {
            throw new SixException(ResultEnum.ERROR_ACCESS.getCode(), "令牌已过期，请重新登录");
        }
        Integer id = map.get("id").asInt();
        String loginKey = RedisKeyPrefix.LOGINKEY + ipAddress + ":" + id;
        // 缓存存在
        if (redisTemplate.hasKey(loginKey)) {
            // 从缓存中获取用户
            loginUser = redisTemplate.opsForValue().get(loginKey);
        } else {
            /** 如果Token未过期,则刷新Token */
            String newToken = sysUserService.refreshToken(id);
            HttpServletResponse response = attributes.getResponse();
            response.setHeader("Access-Control-Expose-Headers", "token");
            response.setHeader("token", newToken);
            loginUser = redisTemplate.opsForValue().get(loginKey);
            if (loginUser == null) {
                throw new SixException(ResultEnum.ERROR_ACCESS.getCode(), "用户已退出，请重新登录");
            }
        }
        loginUser.setOperateIp(ipAddress);
        System.out.println("获取用户>>>> " + loginUser.toString());
        SessionLocal.setUser(loginUser);
    }

    @AfterReturning(returning = "object", pointcut = "pointCut()")
    public void doAfter(){
        SessionLocal.remove();
    }
}
