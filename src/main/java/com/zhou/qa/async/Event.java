package com.zhou.qa.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxinyu1cp on 2018/5/26.
 */

// 事件类，异步通知系统中主要的信息载体，可扩展
public class Event
{
    private EventType eventType;    // 事件类型
    private int fromUserId;         // 产生事件的user id
    private int toUserId;           // 接收事件的user id
    private int EntityType;         // 该事件所关联的实体类型
    private int EntityId;           // 该事件所关联的实体id
    private Map<String, String> exts = new HashMap<String, String>();   // 扩展字段，设置一些额外数据

    // 提供设置扩展数据的接口
    // 返回当前对象以便链式调用
    public Event setExt(String key, String val)
    {
        exts.put(key, val);
        return this;
    }

    public String getExt(String key)
    {
        return exts.get(key);
    }

    // 支持序列化，提供一个默认构造
    public Event() {}
    public Event(EventType type) { this.eventType = type; }

    public EventType getEventType() {
        return eventType;
    }

    public Event setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public Event setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
        return this;
    }

    public int getToUserId() {
        return toUserId;
    }

    public Event setToUserId(int toUserId) {
        this.toUserId = toUserId;
        return this;
    }

    public int getEntityType() {
        return EntityType;
    }

    public Event setEntityType(int entityType) {
        EntityType = entityType;
        return this;
    }

    public int getEntityId() {
        return EntityId;
    }

    public Event setEntityId(int entityId) {
        EntityId = entityId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}





