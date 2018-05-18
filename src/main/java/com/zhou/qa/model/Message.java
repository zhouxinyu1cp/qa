package com.zhou.qa.model;

import java.util.Date;

/**
 * Created by zhouxinyu1cp on 2018/5/17.
 */

// Message对象，对应数据库表 message 中的每条数据，代表一条用户发送的私信
public class Message
{
    private int id;
    private int fromId;         // 发送方用户id
    private int toId;           // 接收方用户id
    private String content;     // 私信内容
    private Date createdDate;
    private int hasRead;       // 私信是否已经读过
    private String conversationId;  // 会话id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public String getConversationId() {
        // 会话id 用 发送发和接收方用户id 拼成
        // id小的在前、大的在后
        if(fromId < toId)
        {
            return String.format("%d_%d", fromId, toId);
        }
        else
        {
            return String.format("%d_%d", toId, fromId);
        }
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}












