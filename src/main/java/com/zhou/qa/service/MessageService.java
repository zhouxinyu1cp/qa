package com.zhou.qa.service;

import com.zhou.qa.dao.MessageDao;
import com.zhou.qa.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/5/17.
 */

@Service
public class MessageService
{
    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveService sensitiveService;

    // 添加一条私信
    public int addMessage(Message message)
    {
        // 过滤掉html标签和敏感词
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()));

        return messageDao.addMessage(message) > 0 ? message.getId() : 0;
    }

    // 根据会话id选出其下所有的私信
    public List<Message> getMessageDetailsByConversationId(String conversationId, int offset, int limit)
    {
        return messageDao.selectMessageDetailsByConversationId(conversationId, offset, limit);
    }

    // 根据当前用户id选出与该用户关联的所有私信列表
    public List<Message> getMessageListByUserId(int userId, int offset, int limit)
    {
        return messageDao.selectMessageListByUserId(userId, offset, limit);
    }

    // 获取该会话中未读的私信数
    public int getConversationUnreadCount(int userId, String conversationId)
    {
        return messageDao.selectConversationUnreadCount(userId, conversationId);
    }
}















