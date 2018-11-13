package com.maybe.sys.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.config.SystemConfig;
import com.maybe.sys.common.dto.LoginUserDto;
import com.maybe.sys.common.dto.PageDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.param.LoginUser;
import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.common.param.UserParam;
import com.maybe.sys.common.util.*;
import com.maybe.sys.dao.*;
import com.maybe.sys.model.SysDept;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.model.SysUserLogin;
import com.maybe.sys.service.ISysTreeService;
import com.maybe.sys.service.ISysUserService;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.RequestUserAgent;
import org.hibernate.service.spi.ServiceException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author jin
 * @description:
 * @date 2018/5/5
 */
@Slf4j
@Service
public class SysUserServiceImpl implements ISysUserService{

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private ISysTreeService sysTreeService;
    @Autowired
    private SysDeptUserMapper sysDeptUserMapper;
    @Autowired
    private SysDeptLeadMapper sysDeptLeadMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysUtilsMapper sysUtilsMapper;
    @Autowired
    private SysUserLoginMapper sysUserLoginMapper;
    @Autowired
    private LoginUserDto loginUserDto;
    @Autowired
    private LoginUser loginUser;
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private SystemConfig systemConfig;
    @Resource
    private RedisTemplate<String, LoginUser> redisTemplate;

    @Override
    @Transactional
    public void insert(UserParam param) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(param, sysUser);
        if (checkExist(sysUser.getId(),"username", sysUser.getUsername())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "用户名已被占用");
        }
        if (checkExist(sysUser.getId(),"phone", sysUser.getUsername())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "号码已被占用");
        }
        if (checkExist(sysUser.getId(),"mail", sysUser.getUsername())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "邮箱已被占用");
        }
        String password = PasswordUtil.randomPassword();
        String encryptedPassword = MD5Util.encrypt(password);
        sysUser.setPassword(encryptedPassword);
        sysUser.setOperateIp(SessionLocal.getUser().getOperateIp());
        sysUser.setOperateId(SessionLocal.getUser().getId());
        sysUser.setOperateName(SessionLocal.getUser().getName());
        sysUser.setOperateTime(new Date());
        sysUserMapper.insertSelective(sysUser);
        if (param.getDeptKeys().size() > 0) {
            sysDeptUserMapper.insertUserDept(sysUser, param.getDeptKeys());
        } else {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "所属部门不能为空");
        }
        if (param.getRoleKeys().size() > 0) {
            sysRoleUserMapper.insertUserRole(sysUser, param.getRoleKeys());
        }
        sendPasswordEmail(sysUser.getMail(), sysUser.getName(), password);
    }

    @Override
    public SysUser select() {
        return sysUserMapper.selectByPrimaryKey(SessionLocal.getUser().getId());
    }

    @Override
    @Transactional
    public void update(UserParam param) {
        SysUser after = new SysUser();
        BeanUtils.copyProperties(param, after);
        if (checkExist(after.getId(),"username", after.getUsername())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "用户名已被占用");
        }
        if (checkExist(after.getId(),"phone", after.getUsername())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "号码已被占用");
        }
        if (checkExist(after.getId(),"mail", after.getUsername())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "邮箱已被占用");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(after.getId());
        if (ObjectUtils.isEmpty(before)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待更新用户不存在");
        }
        after.setOperateIp(SessionLocal.getUser().getOperateIp());
        after.setOperateId(SessionLocal.getUser().getId());
        after.setOperateName(SessionLocal.getUser().getName());
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);

        List<Integer> oldDeptKeys = sysDeptUserMapper.selectDeptKeysByUserId(after.getId());
        List<Integer> newDeptKeys = param.getDeptKeys();
        Map<String, List<Integer>> deptMap = SixUtil.handelKeys(oldDeptKeys, newDeptKeys);
        if (deptMap.containsKey("addKeys")) {
            sysDeptUserMapper.insertUserDept(after, deptMap.get("addKeys"));
        }
        if (deptMap.containsKey("delKeys")) {
            sysDeptUserMapper.removeUserDept(after, deptMap.get("delKeys"));
            sysDeptLeadMapper.deleteByUserIdAndDeptKeys(after.getId(), deptMap.get("delKeys"));
        }

        List<Integer> oldRoleKeys = sysRoleUserMapper.selectRoleKeysByUserId(after.getId());
        List<Integer> newRoleKeys = param.getRoleKeys();
        Map<String, List<Integer>> roleMap = SixUtil.handelKeys(oldRoleKeys, newRoleKeys);
        if (roleMap.containsKey("addKeys")) {
            sysRoleUserMapper.insertUserRole(after, roleMap.get("addKeys"));
        }
        if (roleMap.containsKey("delKeys")) {
            sysRoleUserMapper.removeUserRole(after, roleMap.get("delKeys"));
        }
    }

    @Override
    @Transactional
    public void delete(Integer userId) {
        sysUserMapper.deleteByPrimaryKey(userId);
        sysRoleUserMapper.deleteRoleUserByUserId(userId);
        sysDeptLeadMapper.deleteDeptLeadByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Integer> userKeys) {
        sysUserMapper.deleteByUserKeys(userKeys);
        sysRoleUserMapper.deleteRoleUserByUserKeys(userKeys);
        sysDeptLeadMapper.deleteDeptLeadByUserKeys(userKeys);
    }

    @Override
    public List<SysDept> findDeptListByUserId(Integer userId) {
        return sysDeptMapper.findDeptListByUserId(userId);
    }

    @Override
    public List<SysUser> findLeadListByUserId(Integer userId) {
        return sysUserMapper.findLeadListByUserId(userId);
    }

    @Override
    public List<SysUser> findLeadListByUsername(String username) {
        SysUser sysUser = sysUserMapper.findByUsername(username);
        return findLeadListByUserId(sysUser.getId());
    }

    @Override
    public PageDto<SysUser> findUserPageByDeptId(Integer deptId, String query, PageParam page) {
        if (deptId == null) {
            int total = sysUserMapper.countByDeptLevel("0", query);
            List<SysUser> list = sysUserMapper.findUserPageByDeptLevel("0", query, page);
            return new PageDto<>(page.getPage(), page.getSize(), total, list);
        } else {
            int total = sysUserMapper.countByDeptId(deptId, query);
            List<SysUser> list = sysUserMapper.findUserPageByDeptId(deptId, query, page);
            return new PageDto<>(page.getPage(), page.getSize(), total, list);
        }
    }

    @Override
    public boolean checkExist(Integer userId, String field, String value) {
        int count = sysUtilsMapper.countOfExist("sys_user", null, userId, field, value);
        return count > 0;
    }

    @Override
    public String loginByKeyword(String keyword, String value) {
        SysUser user = sysUserMapper.findByKeyword(keyword);
        if (user == null) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "请输入正确的账号和密码");
        } else if (!user.getPassword().equals(MD5Util.encrypt(value))) {
            if (user.getResetCode() != null && user.getResetCode().equals(MD5Util.encrypt(value))) {
                /** 当使用重置后的密码登录时使用新密码替换旧密码 */
                user.setPassword(user.getResetCode());
                user.setResetCode("");
                sysUserMapper.updateByPrimaryKeySelective(user);
                handelLoginLog(user);
                return handelAuthorize(user);
            } else {
                throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "请输入正确的账号和密码");
            }
        } else if (user.getStatus() == 2) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "账号被冻结");
        } else {
            if (user.getStatus() == 0) {
                /** 初次登录，自动激活账号 */
                user.setStatus(1);
                sysUserMapper.updateByPrimaryKeySelective(user);
            }
            handelLoginLog(user);
            return handelAuthorize(user);
        }
    }

    /**
     * 处理登录日志
     * @param user
     */
    private void handelLoginLog(SysUser user) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ipAddress = NetworkUtil.getIpAddress(request);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
        SysUserLogin userLogin = new SysUserLogin(user.getId(), user.getName(), ipAddress, new Date(),
                "", userAgent.getOperatingSystem().getName(), userAgent.getBrowser().getName(),
                userAgent.getBrowserVersion().getVersion());
        sysUserLoginMapper.insertSelective(userLogin);
    }

    @Override
    public String refreshToken(Integer userId) {
        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        return handelAuthorize(user);
    }

    /**
     * 授权访问,缓存用户信息
     * @param user
     * @return
     */
    private String handelAuthorize(SysUser user) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ipAddress = NetworkUtil.getIpAddress(request);
        try {
            String token = JWToken.createToken(user);
            BeanUtils.copyProperties(user, loginUser);
            String loginKey = RedisKeyPrefix.LOGINKEY + ipAddress + ":" + user.getId();
            clearUserCache(loginKey);
            // log.info("用户登录，缓存用户信息 >> " + loginKey);
            redisTemplate.opsForValue().set(loginKey, loginUser, 5, TimeUnit.HOURS);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SixException(ResultEnum.EXCEPTION.getCode(), "签发令牌失败");
        }
    }

    @Override
    public LoginUserDto getLoginUserByToken(String token) {
        try {
            Map<String, Claim> map = JWToken.verifyToken(token);
            Integer id = map.get("id").asInt();
            SysUser user = sysUserMapper.selectByPrimaryKey(id);
            loginUserDto.setName(user.getName());
            List<String> roles = new ArrayList<>();
            roles.add("admin");
            roles.add("user");
            loginUserDto.setRoles(roles);
            String avatar = user.getAvatar();
            if (StringUtils.isEmpty(avatar)) {
                loginUserDto.setAvatar(systemConfig.getAvatar());
            } else {
                loginUserDto.setAvatar(systemConfig.getFastUrl() + avatar);
            }
//            loginUserDto.setMenus(sysTreeService.menuTreeByUserId(user.getId()));
            loginUserDto.setMenus(sysTreeService.menuTree());
            return loginUserDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "非法令牌或令牌已过期");
        }
    }

    @Override
    public void logoutByKey() {
        LoginUser user = SessionLocal.getUser();
        String loginKey = RedisKeyPrefix.LOGINKEY + user.getOperateIp() + ":" + user.getId();
        clearUserCache(loginKey);
    }

    private void clearUserCache (String loginKey) {
        if (redisTemplate.hasKey(loginKey)) {
            // 缓存存在
            redisTemplate.delete(loginKey);
//            log.info("用户更新，从缓存中删除用户 >> " + loginKey);
        }
    }

    private void sendPasswordEmail(String email, String name, String password) {
        String deliver = "1641525429@qq.com";
        String[] receiver = {email};
        String[] carbonCopy = {};
        String subject = "北斗微芯OA办公平台账号密码，请注意查收！";
        String content =
            "<style>" +
            "  .message:hover{" +
            "    box-shadow: 2px 4px 8px 4px #ccc;" +
            "  }" +
            "</style>" +
            "<body>" +
            " <div class=\"message\" style=\"position: relative;margin: 30px auto;width: 400px;height: 300px;overflow: hidden;border-radius: 10px;border: 1px solid #ccc;color: #606266;font-family: 'Helvetica Neue',Helvetica,'PingFang SC','Hiragino Sans GB','Microsoft YaHei','微软雅黑',Arial,sans-serif;\">" +
            "   <div style=\"padding: 20px;\">" +
            "     <p style=\"margin: 0 0 10px 0;padding-bottom: 10px;color: #409eff;font-size: 24px;font-weight: bold;border-bottom: 1px solid #ccc;\">尊敬的："+name+"</p>" +
            "     <p style=\"margin: 0;color: #868686;font-size: 20px;\">您在北斗微芯OA办公平台账号密码为：<span style=\"color: red;font-size: 24px;font-weight: bold;\">"+password+"<span></p>" +
            "   </div>" +
            "   <div style=\"position: absolute; bottom: 0;width: 100%;height: 60px;background: #409eff;text-align: center;line-height: 60px;\">" +
            "     <a style=\"display: block;color: white;font-size: 26px;text-decoration: none\" href=\"http://10.10.10.164:80\">立即登录</a>" +
            "   </div>" +
            " </div>";
        boolean isHtml = true;
        try {
            mailUtil.sendHtmlEmail(deliver, receiver, carbonCopy, subject, content, isHtml);
        } catch (ServiceException e) {
            log.error("邮件发送失败", e);
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "邮件发送失败,请检查邮箱地址");
        }
    }


    @Override
    public void reset(String mail, String code) {
        String key = RedisKeyPrefix.CHECKKEY + code;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            // 缓存存在
            redisTemplate.delete(key);
            SysUser sysUser = sysUserMapper.findByKeyword(mail);
            if (ObjectUtils.isEmpty(sysUser)) {
                throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "此邮箱未绑定任何账号");
            }
            String password = PasswordUtil.randomPassword();
            String encryptedPassword = MD5Util.encrypt(password);
            sysUser.setResetCode(encryptedPassword);
            sysUserMapper.updateByPrimaryKeySelective(sysUser);
            sendPasswordEmail(sysUser.getMail(), sysUser.getName(), password);
        } else {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "验证码错误或已过期");
        }
    }

    @Override
    @Transactional
    public void setAvatar(MultipartFile file) {
        SysUser mine = sysUserMapper.selectByPrimaryKey(SessionLocal.getUser().getId());
        if (mine == null) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "获取用户信息失败");
        }
        String avatar = mine.getAvatar();
        try {
            String path = fastDFSClient.uploadFile(file);
            mine.setAvatar(path);
            sysUserMapper.updateByPrimaryKey(mine);
            try {
                fastDFSClient.deleteFile(avatar);
            } catch (Exception e) {
                /** 忽略图片删除异常 */
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "上传头像失败");
        }
       
    }

    @Override
    public List<SysUser> userListByRoleCode(String roleCode) {
        return sysUserMapper.userListByRoleCode(roleCode);
    }

    @Override
    public void passUpdate(String oldPassword, String newPassword) {
        SysUser user = sysUserMapper.selectByPrimaryKey(SessionLocal.getUser().getId());
        if (user.getPassword().equals(MD5Util.encrypt(oldPassword))) {
            user.setPassword(MD5Util.encrypt(newPassword));
            sysUserMapper.updateByPrimaryKey(user);
        } else {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "验证失败，原始密码错误！");
        }
    }

    @Override
    public void mineUpdate(String jsonInfo) {
        try {
            SysUser user = sysUserMapper.selectByPrimaryKey(SessionLocal.getUser().getId());
            user.setJsonInfo(jsonInfo);
            JSONObject jsonObject = new JSONObject(jsonInfo);
            if (jsonObject.has("nickName")) {
                user.setNickName(jsonObject.getString("nickName"));
            }
            if (jsonObject.has("sex")) {
                user.setSex(jsonObject.getInt("sex"));
            }
            if (jsonObject.has("say")) {
                user.setSay(jsonObject.getString("say"));
            }
            if (jsonObject.has("weChat")) {
                user.setWeChat(jsonObject.getString("weChat"));
            }
            if (jsonObject.has("qq")) {
                user.setQq(jsonObject.getString("qq"));
            }
            if (jsonObject.has("birthday")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date birthday = format.parse(jsonObject.getString("birthday"));
                user.setBirthday(birthday);
            }
            sysUserMapper.updateByPrimaryKeyWithBLOBs(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "保存失败，请格式是否正确");
        }

    }

    @Override
    public String mineSelect() {
        return sysUserMapper.findJsonInfoByUserId(SessionLocal.getUser().getId());
    }

}