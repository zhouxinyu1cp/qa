package com.zhou.qa.service;

import com.zhou.qa.util.JedisAdapter;
import com.zhou.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by zhouxinyu1cp on 2018/5/28.
 */

// 关注、取消关注业务
@Service
public class FollowService
{
    @Autowired
    private JedisAdapter jedisAdapter;

    // 关注服务，用户关注某个实体，可关注问题，关注其他用户
    public boolean follow(int userId, int entityType, int entityId)
    {
        // 获取该实体的粉丝集合key
        String followersKey = RedisKeyUtil.getFollowersKey(entityType, entityId);
        // 获取该用户对该类实体的关注对象集合key
        String followeesKey = RedisKeyUtil.getFolloweesKey(userId, entityType);

        // 使用Redis事务，
        // 把该用户添加到该实体的粉丝集合中，
        // 把该实体添加到该用户的关注对象集合中
        Jedis jedis = jedisAdapter.getJedis();
        Transaction ta = jedisAdapter.multi(jedis);
        ta.zadd(followersKey, new Date().getTime(), String.valueOf(userId));
        ta.zadd(followeesKey, new Date().getTime(), String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(ta, jedis);

        return (ret != null && ret.size() == 2);
    }

    // 取消关注服务，与关注服务对应
    public boolean unfollow(int userId, int entityType, int entityId)
    {
        // 获取该实体的粉丝集合key
        String followersKey = RedisKeyUtil.getFollowersKey(entityType, entityId);
        // 获取该用户对该类实体的关注对象集合key
        String followeesKey = RedisKeyUtil.getFolloweesKey(userId, entityType);

        // 使用Redis事务，
        // 从该实体的粉丝集合中删除该用户，
        // 从该用户的关注对象集合中删除该实体
        Jedis jedis = jedisAdapter.getJedis();
        Transaction ta = jedisAdapter.multi(jedis);
        ta.zrem(followersKey, String.valueOf(userId));
        ta.zrem(followeesKey, String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(ta, jedis);

        return (ret != null && ret.size() == 2);
    }

    // 获取某个实体的粉丝数量，即某个实体的关注数，如某问题的关注数
    public long getFollowersCount(int entityType, int entityId)
    {
        // 获取该实体的粉丝集合key
        String followersKey = RedisKeyUtil.getFollowersKey(entityType, entityId);
        return jedisAdapter.zcard(followersKey);
    }

    // 获取某用户对某类实体关注了多少，如该用户关注了多少问题，关注了多少其他用户
    public long getFolloweesCount(int userId, int entityType)
    {
        // 获取该用户对该类实体的关注对象集合key
        String followeesKey = RedisKeyUtil.getFolloweesKey(userId, entityType);
        return jedisAdapter.zcard(followeesKey);
    }

    // 判断某用户是否是某个实体的粉丝，即某用户是否关注了某实体
    public boolean isFollower(int userId, int entityType, int entityId)
    {
        // 获取该实体的粉丝集合key
        String followersKey = RedisKeyUtil.getFollowersKey(entityType, entityId);
        // 结果不为null，表明Redis zset中存在该用户
        return jedisAdapter.zscore(followersKey, String.valueOf(userId)) != null;
    }

    // 获取某实体的粉丝列表
    public List<Integer> getFollowers(int entityType, int entityId, int start, int end)
    {
        String followersKey = RedisKeyUtil.getFollowersKey(entityType, entityId);
        Set<String> followers = jedisAdapter.zrevrange(followersKey, start, end);
        return setToList(followers);
    }

    // 获取某用户对某类实体的关注列表，如某用户关注的问题列表，关注的用户列表等
    public List<Integer> getFollowees(int userId, int entityType, int start, int end)
    {
        String followeesKey = RedisKeyUtil.getFolloweesKey(userId, entityType);
        Set<String> followers = jedisAdapter.zrevrange(followeesKey, start, end);
        return setToList(followers);
    }

    // help函数
    private List<Integer> setToList(Set<String> set)
    {
        List<Integer> ret = new ArrayList<Integer>();
        for(String val : set)
        {
            ret.add(Integer.parseInt(val));
        }

        return ret;
    }
}











