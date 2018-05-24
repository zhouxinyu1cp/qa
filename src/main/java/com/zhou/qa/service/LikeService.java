package com.zhou.qa.service;

import com.zhou.qa.util.JedisAdapter;
import com.zhou.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouxinyu1cp on 2018/5/24.
 */

// 点赞、踩赞业务
@Service
public class LikeService
{
    @Autowired
    JedisAdapter jedisAdapter;

    // 点赞，关联某一个实体，如某个评论的点赞、某个问题的点赞等
    // @return：已有的点赞数
    public long like(int userId, int entityType, int entityId)
    {
        // 该实体对应在Redis中的点赞key、踩赞key
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);

        // 该实体关联的点赞集合中添加该用户
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

        // 该实体关联的踩赞集合中删除该用户
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));

        // 返回该实体的点赞数
        return jedisAdapter.scard(likeKey);
    }

    // 踩赞，关联某一个实体，如某个评论的踩赞、某个问题的踩赞等
    // @return：已有的点赞数
    public long dislike(int userId, int entityType, int entityId)
    {
        // 该实体对应在Redis中的点赞key、踩赞key
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);

        // 该实体关联的踩赞集合中添加该用户
        jedisAdapter.sadd(dislikeKey, String.valueOf(userId));

        // 该实体关联的点赞集合中删除该用户
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        // 返回该实体的点赞数
        return jedisAdapter.scard(likeKey);
    }

    // 获取关联到某个实体的点赞数
    public long getLikeCount(int entityType, int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }

    // 获取当前用户对某个实体的点赞状态
    // @return：>0点赞，<0踩赞，==0未登录
    public int getLikeStatus(int userId, int entityType, int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if(jedisAdapter.sismember(likeKey, String.valueOf(userId)))
        {
            return 1; // 已点赞
        }

        String disLikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        if(jedisAdapter.sismember(disLikeKey, String.valueOf(userId)))
        {
            return -1; // 已踩赞
        }

        return 0;
    }
}
















