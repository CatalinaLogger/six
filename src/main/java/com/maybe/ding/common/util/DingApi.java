package com.maybe.ding.common.util;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DingApi {

    private String token;


    public JSONObject departmentList(){
        return doGet("https://oapi.dingtalk.com/department/list", null);
    }

//    @Scheduled(fixedDelay = 1000*60*60) // 表示上一次任务执行完成后多久再次执行，参数类型为long，单位ms
    public void getToken(){
        // 987db4bdd4dd37c58bf80d4f36db3e43
        String json = HttpUtil.doGet("https://oapi.dingtalk.com/gettoken?corpid=dinge34b66a867d904ea35c2f4657eb6378f&corpsecret=G-GtXBCn9WGjqXh62_gNcbpDXx68bMN6A9miUJ-3BisyM-t72vykJZ_-86bLHUGF");
        JSONObject jsonObject = new JSONObject(json);
        token = (String) jsonObject.get("access_token");
        log.info("刷新AccessToken{}--> ", token);
    }


    private JSONObject doGet(String url, String params) {
        params = "?access_token=" + token + "&" + params;
        String res = HttpUtil.doGet(url + params);
        log.info("钉钉返回数据--> {}", res);
        JSONObject jsonObject = new JSONObject(res);
        int errCode = (int) jsonObject.get("errcode");
        if ( errCode != 0) {
            /** 遇到报错就刷新access_token */
            getToken();
            throw new SixException(ResultEnum.ERROR.getCode(), (String) jsonObject.get("errmsg"));
        }
        return jsonObject;
    }

    private JSONObject doPost(String url, String params) {
        params = "access_token=" + token + "&" + params;
        String res = HttpUtil.doPost(url, params);
        log.info("钉钉返回数据--> {}", res);
        JSONObject jsonObject = new JSONObject(res);
        int errCode = (int) jsonObject.get("errcode");
        if ( errCode != 0) {
            /** 遇到报错就刷新access_token */
            getToken();
            throw new SixException(ResultEnum.ERROR.getCode(), (String) jsonObject.get("errmsg"));
        }
        return jsonObject;
    }

}
