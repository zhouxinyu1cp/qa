package com.zhou.qa.util;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhouxinyu1cp on 2018/5/26.
 */

//邮件发送器
@Component
public class MailSender implements InitializingBean
{
    public static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    private JavaMailSenderImpl sender = new JavaMailSenderImpl();

    @Autowired
    private VelocityEngine velocityEngine;

    // 发送邮件的接口
    public boolean sendEmail(String toUser,
                              String subject,
                              String template,
                              Map<String, Object> data)
    {
        try {
            String nick = MimeUtility.encodeText("zhouxinyu1cp");
            InternetAddress from = new InternetAddress(nick + "<zhouxinyu1cp@163.com>");
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, "UTF-8", data); // 用模板加载出正文
            mimeMessageHelper.setTo(toUser); // 设置接收者
            mimeMessageHelper.setFrom(from); // 设置发送者
            mimeMessageHelper.setSubject(subject); // 设置主题
            mimeMessageHelper.setText(result, true); // 设置正文
            sender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    // 初始化，设置发送邮件所需的参数
    @Override
    public void afterPropertiesSet() throws Exception
    {
        sender.setUsername("zhouxinyu1cp@163.com"); // 使用163邮箱发邮件，设置登陆邮箱的用户名
        sender.setPassword("zhouxinyu1cp"); // 设置登陆邮箱的密码（授权码）
        sender.setHost("smtp.163.com"); // SMTP服务器地址
        sender.setPort(465); // SMTP服务器端口
        sender.setProtocol("smtps"); // SMTP SSL协议
        sender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true); // 启用 SMTP SSL协议
        //javaMailProperties.put("mail.smtp.auth", true);
        //javaMailProperties.put("mail.smtp.starttls.enable", true);
        sender.setJavaMailProperties(javaMailProperties);
    }
}















