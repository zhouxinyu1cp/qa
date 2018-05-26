package com.zhou.qa.async.handlers;

import com.zhou.qa.async.Event;
import com.zhou.qa.async.EventHandler;
import com.zhou.qa.async.EventType;
import com.zhou.qa.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouxinyu1cp on 2018/5/26.
 */

// 登陆事件处理器
@Component
public class LoginEventHandler implements EventHandler
{
    @Autowired
    MailSender mailSender;

    @Override
    public void handleEvent(Event event)
    {
        /* 登陆时，给用户发欢迎邮件 */
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("username", event.getExt("username"));

        mailSender.sendEmail(event.getExt("toEmail"),
                              "欢迎登陆",
                              "mails/login_notify.html",
                              data);
    }

    @Override
    public List<EventType> getSupportEventTypes()
    {
        return Arrays.asList(EventType.LOGIN_EVENT);
    }
}















