package com.ymt.utils;

import java.io.UnsupportedEncodingException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service("mailUtils")
public class MailUtils {
    private static String system_flag;
    static {
        try {
            system_flag = javax.mail.internet.MimeUtility.encodeText("WMS系统邮件");
        } catch (UnsupportedEncodingException e) {
        }
    }

    public static void sendMail(String subject, String to, String text) {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        senderImpl.setHost("smtp.126.com");
        senderImpl.setUsername("zhouxianglh");
        senderImpl.setPassword("**********");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(system_flag + "<zhouxianglh@126.com>");
        mailMessage.setSubject("测试邮件");
        mailMessage.setTo(to.split(","));
        mailMessage.setText(text);
        senderImpl.send(mailMessage);
    }

    public static void main(String[] args) {
        sendMail("my test", "zhouxianglh@qq.com", "测试邮件");
        System.out.println("over.................");
    }
}
