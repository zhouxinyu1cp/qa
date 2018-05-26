package com.zhou.qa.async.handlers;

import com.zhou.qa.async.Event;
import com.zhou.qa.async.EventHandler;
import com.zhou.qa.async.EventType;
import com.zhou.qa.model.Message;
import com.zhou.qa.model.User;
import com.zhou.qa.service.MessageService;
import com.zhou.qa.service.UserService;
import com.zhou.qa.util.QaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/5/26.
 */

// 点赞事件处理器，作为一个Component被Spring框架管理
@Component
public class LikeEventHandler implements EventHandler
{
    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Override
    public void handleEvent(Event event)
    {
        /* 给某个实体点赞，应该给被点赞人发送私信通知 */
        Message msg = new Message();
        msg.setFromId(QaUtil.SYSTEM_USER_ID); // 点赞事件由某个用户触发，但该条私信是由系统自动发送
        msg.setToId(event.getToUserId());
        msg.setCreatedDate(new Date());
        msg.setHasRead(0);
        User fromUser = userService.getUserById(event.getFromUserId()); // 获取产生该事件的user
        msg.setContent("用户" + fromUser.getName() + "给你点了一个赞：" +
                "http://localhost:8080/question/" + event.getExt("questionId"));

        messageService.addMessage(msg);
    }

    @Override
    public List<EventType> getSupportEventTypes()
    {
        return Arrays.asList(EventType.LIKE_EVENT);
    }
}












