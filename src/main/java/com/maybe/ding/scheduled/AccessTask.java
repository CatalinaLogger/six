package com.maybe.ding.scheduled;

import com.maybe.sys.common.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccessTask {

//    @Scheduled(fixedDelay = 1000*60*60) // 表示上一次任务执行完成后多久再次执行，参数类型为long，单位ms
//    public void getToken(){
//        // 987db4bdd4dd37c58bf80d4f36db3e43
//        String json = HttpUtil.doGet("https://oapi.dingtalk.com/gettoken?corpid=dinge34b66a867d904ea35c2f4657eb6378f&corpsecret=G-GtXBCn9WGjqXh62_gNcbpDXx68bMN6A9miUJ-3BisyM-t72vykJZ_-86bLHUGF");
//        System.out.println(System.currentTimeMillis() + json);
//    }

}
