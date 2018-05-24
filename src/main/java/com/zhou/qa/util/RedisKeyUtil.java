package com.zhou.qa.util;

/**
 * Created by zhouxinyu1cp on 2018/5/24.
 */

// 用于生成Redis中的相关key
public class RedisKeyUtil
{
    public static String SPLIT = ":"; // 分隔符
    public static String LIKE = "like";
    public static String DISLIKE = "dislike";

    // 关联某个实体，获取点赞key
    public static String getLikeKey(int entityType, int entityId)
    {
        return LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    // 关联某个实体，获取踩赞key
    public static String getDislikeKey(int entityType, int entityId)
    {
        return DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
}
