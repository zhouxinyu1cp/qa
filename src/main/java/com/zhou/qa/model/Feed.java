package com.zhou.qa.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Created by zhouxinyu1cp on 2018/6/2.
 */

// Feed对象，对应数据库表 feed 中的每条数据，代表一条最新动态
public class Feed
{
    private int id;
    private int userId; // 该条最新动态由哪个用户产生的
    private Date createdDate;
    private int type; // 该条最新动态的类型，问题的动态，评论的动态，关注的动态等，其值就是EventType的值
    private String data; // 最新动态的内容，方便扩展字段，是一个JSON串

    // data的JSON对象，在setData()时初始化，
    // 以便提供get(key)方法，可以在Model中以 feed.attrKey 直接使用其值，类似 ViewObject
    private JSONObject jsonData = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        jsonData = JSONObject.parseObject(data);
    }

    // 辅助的get(key)方法，类似 ViewObject
    public String get(String key)
    {
        return jsonData == null ? null : jsonData.getString(key);
    }
}











