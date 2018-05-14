package com.zhou.qa.model;

import java.util.Date;

/**
 * Created by zhouxinyu1cp on 2018/5/13.
 */

// Comment对象，对应数据库表 comment 中的每条数据，代表一条评论
public class Comment
{
    private int id;
    private String content;
    private int userId;         // 该条评论是哪个用户发的
    private int entityType;     // 该条评论关联的评论类型，是问题的评论、图片的评论、评论的评论......
    private int entityId;       // 该条评论关联的实体id
    private Date createdDate;
    private int status;         // 该条评论的状态，删除/隐藏等，status == 0 表示状态正常

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}



