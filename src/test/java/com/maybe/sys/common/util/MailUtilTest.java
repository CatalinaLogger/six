package com.maybe.sys.common.util;

import org.hibernate.service.spi.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailUtilTest {

    @Autowired
    private MailUtil mailUtil;

    @Test
    public void sendSimpleEmail() {
        String deliver = "1641525429@qq.com";
        String[] receiver = {"610977025@qq.com"};
        String[] carbonCopy = {"1641525429@qq.com"};
        String subject = "This is a simple email";
        String content = "This is a simple content";

        try {
            mailUtil.sendSimpleEmail(deliver, receiver, carbonCopy, subject, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendLinkEmail() {
        String deliver = "1641525429@qq.com";
        String[] receiver = {"610977025@qq.com"};
        String[] carbonCopy = {"1641525429@qq.com"};
        String subject = "开发平台账号激活";
        String content =
            "<style>\n" +
            "  .message:hover{\n" +
            "    box-shadow: 2px 4px 8px 4px #ccc;\n" +
            "  }\n" +
            "</style>\n" +
            "<body>\n" +
            " <div class=\"message\" style=\"position: relative;margin: 30px auto;width: 400px;height: 300px;overflow: hidden;border-radius: 10px;border: 1px solid #ccc;color: #606266;font-family: 'Helvetica Neue',Helvetica,'PingFang SC','Hiragino Sans GB','Microsoft YaHei','微软雅黑',Arial,sans-serif;\">\n" +
            "   <div style=\"padding: 20px;\">\n" +
            "     <p style=\"margin: 0 0 10px 0;padding-bottom: 10px;color: #409eff;font-size: 24px;font-weight: bold;border-bottom: 1px solid #ccc;\">尊敬的：德莱文</p>\n" +
            "     <p style=\"margin: 0;color: #868686;font-size: 20px;\">您在开发平台账号注册成功，请点击下方链接完成激活,一个月内有效！</p>\n" +
            "   </div>\n" +
            "   <div style=\"position: absolute; bottom: 0;width: 100%;height: 60px;background: #409eff;text-align: center;line-height: 60px;\">\n" +
            "     <a style=\"display: block;color: white;font-size: 26px;text-decoration: none\" href=\"http://193.112.10.196\">点我完成激活</a>\n" +
            "   </div>\n" +
            " </div>";
        boolean isHtml = true;
        try {
            mailUtil.sendHtmlEmail(deliver, receiver, carbonCopy, subject, content, isHtml);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}