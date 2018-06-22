package com.zhou.qa.async;

/**
 * Created by zhouxinyu1cp on 2018/5/26.
 */

// 事件类型，在异步通知系统中，标明不同事件的类型
public enum EventType
{
    LOGIN_EVENT(1),     // 登录事件
    LIKE_EVENT(2),      // 点赞事件
    MESSAGE_EVENT(3),   // 私信事件
    MAIL_EVENT(4),      // 邮件事件
    FOLLOW_EVENT(5),    // 关注事件
    UNFOLLOW_EVENT(6),  // 取关事件
    COMMENT_EVENT(7),   // 评论事件
    SOLR_CREATE_INDEX_EVENT(8); // 建立solr全文索引事件

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}





