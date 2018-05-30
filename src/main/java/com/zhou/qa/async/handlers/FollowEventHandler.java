package com.zhou.qa.async.handlers;

import com.zhou.qa.async.Event;
import com.zhou.qa.async.EventHandler;
import com.zhou.qa.async.EventType;
import com.zhou.qa.model.EntityType;
import com.zhou.qa.model.Message;
import com.zhou.qa.model.Question;
import com.zhou.qa.model.User;
import com.zhou.qa.service.MessageService;
import com.zhou.qa.service.QuestionService;
import com.zhou.qa.service.UserService;
import com.zhou.qa.util.QaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Entity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.jar.Pack200;

/**
 * Created by zhouxinyu1cp on 2018/5/29.
 */

// 关注、取关事件处理器
@Component
public class FollowEventHandler implements EventHandler
{
    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    MessageService messageService;

    private void doFollow(Event event)
    {
        User fromUser = userService.getUserById(event.getFromUserId());

        Message message = new Message();
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setFromId(QaUtil.SYSTEM_USER_ID); // 系统发私信通知对应的用户

        if(event.getEntityType() == EntityType.ENTITY_QUESTION)
        {
            Question question = questionService.getQuestionById(event.getEntityId());
            message.setToId(question.getUserId());

            String content = "用户" + fromUser.getName() + "关注了你发布的问题：" + question.getTitle()
                                + "，链接：http://localhost:8080/question/" + String.valueOf(question.getId());
            message.setContent(content);
        }
        else if(event.getEntityType() == EntityType.ENTITY_USER)
        {
            message.setToId(event.getEntityId());

            String content = "用户" + fromUser.getName() + "关注了你";
            message.setContent(content);
        }

        messageService.addMessage(message);
    }

    private void doUnFollow(Event event)
    {
        User fromUser = userService.getUserById(event.getFromUserId());

        Message message = new Message();
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setFromId(QaUtil.SYSTEM_USER_ID); // 系统发私信通知对应的用户

        if(event.getEntityType() == EntityType.ENTITY_QUESTION)
        {
            Question question = questionService.getQuestionById(event.getEntityId());
            message.setToId(question.getUserId());

            String content = "用户" + fromUser.getName() + "取关了你发布的问题：" + question.getTitle()
                    + "，链接：http://localhost:8080/question/" + String.valueOf(question.getId());
            message.setContent(content);
        }
        else if(event.getEntityType() == EntityType.ENTITY_USER)
        {
            message.setToId(event.getEntityId());

            String content = "用户" + fromUser.getName() + "取关了你";
            message.setContent(content);
        }

        messageService.addMessage(message);
    }

    @Override
    public void handleEvent(Event event)
    {
        switch(event.getEventType())
        {
            case FOLLOW_EVENT:
                doFollow(event);
                break;

            case UNFOLLOW_EVENT:
                doUnFollow(event);
                break;

            default:
                break;
        }
    }

    @Override
    public List<EventType> getSupportEventTypes()
    {
        return Arrays.asList(EventType.FOLLOW_EVENT, EventType.UNFOLLOW_EVENT);
    }


}















