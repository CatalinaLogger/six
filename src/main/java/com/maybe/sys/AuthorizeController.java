package com.maybe.sys;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.JsonData;
import com.maybe.sys.common.config.CaptchaConfig;
import com.maybe.sys.common.util.RedisKeyPrefix;
import com.maybe.sys.service.ISysUserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

@Api(tags="登录授权", description="用户登录、重置密码、验证码图片、登录页背景")
@Slf4j
@CrossOrigin // 支持跨域请求
@RestController
@RequestMapping("/auth")
public class AuthorizeController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private CaptchaConfig captchaConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public JsonData login(@ApiParam(value = "账号/手机/邮箱", required = true) @RequestParam("username") String username,
                          @ApiParam(value = "密码", required = true) @RequestParam("password") String password) {
        String token = sysUserService.loginByKeyword(username, password);
        return JsonData.success(token);
    }

    @ApiOperation("重置密码")
    @PutMapping("/reset")
    public JsonData reset(@ApiParam(value = "邮箱", required = true) @RequestParam("mail") String mail,
                          @ApiParam(value = "验证码", required = true) @RequestParam("code") String code) {
        sysUserService.reset(mail, code);
        return JsonData.success(ResultEnum.SUCCESS_OPERATE.getCode(), "新的密码已发送到邮箱，请注意查收", null);
    }

    @ApiOperation("获取登录页背景图")
    @GetMapping("/daily")
    public JsonData daily(@ApiParam(value = "日期", required = true) @RequestParam("date") String date) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
//        MultiValueMap<String, String> param= new LinkedMultiValueMap<>();
//        // 添加请求的参数
//        param.add("date", date);             //必传
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(param, headers);
        // 执行HTTP请求
        ResponseEntity<String> response = restTemplate.exchange("http://open.iciba.com/dsapi/?date=" + date, HttpMethod.GET, null, String.class);  //最后的参数需要用String.class  使用其他的会报错
        return JsonData.success(response.getBody());
    }


    @ApiOperation("获取验证码图片")
    @GetMapping("/kaptcha/*")
    public ModelAndView getKaptchaImage(HttpServletResponse response) throws Exception {

        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaConfig.captchaProducer().createText();

        String key = RedisKeyPrefix.CHECKKEY + capText;
        // 放入缓存，并设置缓存时间
        redisTemplate.opsForValue().set(key, "", 1, TimeUnit.MINUTES);

        // create the image with the text
        BufferedImage bi = captchaConfig.captchaProducer().createImage(capText);
        ServletOutputStream out = response.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

}
