package com.zhou.qa.util;

/**
 * Created by zhouxinyu1cp on 2018/5/24.
 */

// 用于生成Redis中的相关key
public class RedisKeyUtil
{
    public static String SPLIT = ":"; // 分隔符
    public static String LIKE = "like"; // 点赞key前缀
    public static String DISLIKE = "dislike"; // 踩赞key前缀
    public static String BLOCK_QUEEU = "blockQueeu"; // 事件队列key前缀
    public static String FOLLOWER = "follower"; // 粉丝集合key前缀（关注者集合）
    public static String FOLLOWEE = "followee"; // 关注对象集合key前缀

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

    // 返回阻塞队列的key
    public static String getQueeuKey()
    {
        return BLOCK_QUEEU;
    }

    // 返回某个实体的粉丝集合key，如某用户的粉丝集合key，某问题的粉丝集合key
    public static String getFollowersKey(int entityType, int entityId)
    {
        return FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    // 返回某用户对某种实体的关注对象集合key，如某用户的关注问题列表、关注用户列表等
    public static String getFolloweesKey(int userId, int entityType)
    {
        return FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }
}












