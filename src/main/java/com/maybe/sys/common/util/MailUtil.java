package com.maybe.sys.common.util;

import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
public class MailUtil {

    @Autowired
    JavaMailSender mailSender;

    /** 发送简单邮件 */
    public void sendSimpleEmail(String deliver, String[] receiver, String[] carbonCopy, String subject, String content) {

        long startTimestamp = System.currentTimeMillis();
        log.info("Start send mail ... ");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(deliver);
            message.setTo(receiver);
            message.setCc(carbonCopy);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("Send mail success, cost {} million seconds", System.currentTimeMillis() - startTimestamp);
        } catch (MailException e) {
            log.error("Send mail failed, error message is : {} \n", e.getMessage());
            e.printStackTrace();
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "发送邮件失败");
        }
    }

    /** 发送Html邮件 */
    public void sendHtmlEmail(String deliver, String[] receiver, String[] carbonCopy, String subject, String content, boolean isHtml){
        long startTimestamp = System.currentTimeMillis();
        log.info("Start send email ...");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(deliver);
            messageHelper.setTo(receiver);
            messageHelper.setCc(carbonCopy);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, isHtml);

            mailSender.send(message);
            log.info("Send email success, cost {} million seconds", System.currentTimeMillis() - startTimestamp);
        } catch (MessagingException e) {
            log.error("Send email failed, error message is {} \n", e.getMessage());
            e.printStackTrace();
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "发送邮件失败");
        }
    }

}