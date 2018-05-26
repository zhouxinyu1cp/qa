package com.zhou.qa.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/5/22.
 */

// 使用Jedis操作Redis，对Redis命令做一层封装
@Service
public class JedisAdapter implements InitializingBean
{
    public static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool; // Redis连接池

    public static void print(int idx, Object obj)
    {
        System.out.println(String.format("%d：%s", idx, obj.toString()));
    }

    public static void main(String[] args)
    {
        /*
         *  Redis可用作对象缓存、PV更新、点赞、
         *  关注列表、排行榜、异步设计、验证码等
         */

        Jedis jedis = new Jedis("redis://localhost:6379/1"); // 选择Redis中第1个数据库
        jedis.flushDB();  // 清除该数据库中的所有内容

        //------------- get set -------------//
        jedis.set("key_hello", "world");
        print(1, jedis.get("key_hello"));
        jedis.rename("key_hello", "new_hello"); // 重命名key
        print(1, jedis.get("new_hello"));

        jedis.setex("timeout", 10, "超时10"); // 设置可超时的key-value，超时后自动删除
        print(1, jedis.get("timeout"));

        //------------- 数值操作 -------------//
        jedis.set("pv", "100");
        jedis.incr("pv"); // 加 1
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 5); // 加 5
        print(2, jedis.get("pv"));
        jedis.decrBy("pv", 3); // 减 3
        print(2, jedis.get("pv"));

        print(3, jedis.keys("*"));

        //------------- list 操作 -------------//
        //------- 可用于列表需求的场景 -------//
        String listKey = "list";
        for(int i = 0; i < 10; ++i)
        {
            jedis.lpush(listKey, "a" + String.valueOf(i)); // 往 list 添加元素
        }

        print(4, jedis.lrange(listKey, 0, 20)); // 取 list 某个范围内的元素
        print(4, jedis.lrange(listKey, 0, 3));

        print(5, jedis.llen(listKey)); // list 长度
        print(6, jedis.lpop(listKey)); // 弹出 list 的首部元素
        print(7, jedis.llen(listKey));

        print(8, jedis.lindex(listKey, 2)); // 取 list 某个位置的元素

        jedis.linsert(listKey, BinaryClient.LIST_POSITION.BEFORE, "a4", "xxx"); // 在某个元素前面插入新元素
        jedis.linsert(listKey, BinaryClient.LIST_POSITION.AFTER, "a4", "ccc"); // 在某个元素后面插入新元素
        print(9, jedis.lrange(listKey, 0, 20));

        //------------- hash map 操作 -------------//
        //----------- 适合动态增删属性 -----------//
        String hKey = "userxx";
        jedis.hset(hKey, "name", "zxy"); // 设置key-value属性，类似 HashMap
        jedis.hset(hKey, "phone", "18544447777");
        jedis.hset(hKey, "location", "swjtu");
        print(10, jedis.hget(hKey, "phone"));
        print(11, jedis.hgetAll(hKey)); // 获取所有的k-v
        print(12, jedis.hkeys(hKey)); // 获取所有keys
        print(13, jedis.hvals(hKey)); // 获取所有values
        print(14, jedis.hexists(hKey, "age")); // 是否包含某个key
        print(15, jedis.hexists(hKey, "location"));
        jedis.hsetnx(hKey, "age", "25"); // 若不存在某个key，才设置对应的k-v
        jedis.hsetnx(hKey, "name", "sfh"); // key存在则不设置
        print(16, jedis.hgetAll(hKey));
        jedis.hdel(hKey, "age"); // 删除某个key
        print(17, jedis.hgetAll(hKey));

        //------------- set 操作 -------------//
        //------ 适合去重，已读，共同好友等 ------//
        String sKey1 = "set1";
        String sKey2 = "set2";
        for(int i = 0; i < 10; ++i)
        {
            jedis.sadd(sKey1, String.valueOf(i)); // 往 set 中添加元素
            jedis.sadd(sKey2, String.valueOf(i*i));
        }

        print(18, jedis.smembers(sKey1)); // 获取 set 中所有元素
        print(18, jedis.smembers(sKey2));

        print(19, jedis.sdiff(sKey1, sKey2)); // 取差集，sKey1有而sKey2没有的元素
        print(20, jedis.sinter(sKey1, sKey2)); // 取交集，sKey1、sKey2均有的元素
        print(21, jedis.sunion(sKey1, sKey2)); // 联合，合并sKey1、sKey2所有元素并去重

        print(22, jedis.sismember(sKey1, "5")); // set 中是否包含某个元素
        print(22, jedis.sismember(sKey2, "20"));

        jedis.srem(sKey1, "8"); // 删除 set 中的某个元素
        print(23, jedis.smembers(sKey1));

        print(24, jedis.scard(sKey1)); // 获取 set 中的元素个数

        jedis.smove(sKey2, sKey1, "36"); // 从sKey2中把某个元素移动到sKey1中
        print(25, jedis.smembers(sKey1));
        print(25, jedis.smembers(sKey2));

        print(26, jedis.srandmember(sKey1, 3)); // 从sKey1中随机返回3个元素

        //------------- sorted set 操作 -------------//
        //--------- 带有权重，适合有排序需求 ---------//
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 50, "zhou"); // 添加带有权重的元素到 sorted set 中
        jedis.zadd(rankKey, 70, "song");
        jedis.zadd(rankKey, 80, "li");
        jedis.zadd(rankKey, 90, "zhang");
        jedis.zadd(rankKey, 35, "zhu");

        print(27, jedis.zcard(rankKey)); // 求 sorted set 中的元素个数
        print(28, jedis.zcount(rankKey, 60, 100)); // 统计 60-100 之间的元素个数

        print(29, jedis.zscore(rankKey, "zhou")); // 求 sorted set 中某个元素的权重
        print(30, jedis.zincrby(rankKey, 2, "zhou")); // "zhou"的权重加2

//        print(31, jedis.zincrby(rankKey, 2, "haha"));
//        print(31, jedis.zscore(rankKey, "haha"));

        print(32, jedis.zrange(rankKey, 0, 2)); // rankKey按权重排序后(默认升序)，返回第0到第2的元素
        print(33, jedis.zrevrange(rankKey, 0, 2)); // rankKey按权重排序后，返回逆序第0到第2的元素

        // 返回权重 60-100 之间的所有元素（以元组的形式）
        for(Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, 60, 100))
        {
            print(34, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }

        print(35, jedis.zrank(rankKey, "zhang")); // 返回某元素在sorted set中的正向排名(从0开始)
        print(35, jedis.zrevrank(rankKey, "zhang")); // 返回某元素在sorted set中的逆向排名(从0开始)

        //------------- Redis连接池，复用 -------------//
        // 连接池默认可创建8个连接
        JedisPool pool = new JedisPool("redis://localhost:6379/1");
        for(int i = 0; i < 100; ++i)
        {
            Jedis j = pool.getResource();
            print(36, j.get("pv"));
            j.close(); // 若使用完不close()，达到最大连接数后会阻塞
        }
    }

    // 初始化接口
    @Override
    public void afterPropertiesSet() throws Exception
    {
        // 选择第2个数据库
        pool = new JedisPool("redis://localhost:6379/2");
    }

    //-------- 对 Redis set 做封装 --------//

    // 在set中添加元素
    public long sadd(String key, String member)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.sadd(key, member);
        }
        catch (Exception e)
        {
            logger.error("Redis添加数据出错：" + e.getMessage());
        }
        finally
        {
            if(jedis != null)
            {
                jedis.close(); // 释放连接
            }
        }

        return -1;
    }

    // 在set中删除元素
    public long srem(String key, String member)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.srem(key, member);
        }
        catch (Exception e)
        {
            logger.error("Redis删除数据出错：" + e.getMessage());
        }
        finally
        {
            if(jedis != null)
            {
                jedis.close(); // 释放连接
            }
        }

        return -1;
    }

    // 判断元素是否在set中
    public boolean sismember(String key, String member)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.sismember(key, member);
        }
        catch (Exception e)
        {
            logger.error("Redis出错：" + e.getMessage());
        }
        finally
        {
            if(jedis != null)
            {
                jedis.close(); // 释放连接
            }
        }

        return false;
    }

    // 返回set中的元素个数
    public long scard(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.scard(key);
        }
        catch (Exception e)
        {
            logger.error("Redis出错：" + e.getMessage());
        }
        finally
        {
            if(jedis != null)
            {
                jedis.close(); // 释放连接
            }
        }

        return 0;
    }

    //-------- 对 Redis list 做封装 --------//

    // 从list左边添加元素
    public long lpush(String key, String val)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.lpush(key, val);
        }
        catch (Exception e)
        {
            logger.error("Redis添加数据出错：" + e.getMessage());
        }
        finally
        {
            if(jedis != null)
            {
                jedis.close(); // 释放连接
            }
        }

        return -1;
    }

    // 从list右边弹出元素，
    // 若list为空，则阻塞直到list有元素为止
    // 返回一个双元素的List，第1个是key，第2个是value，取value即可
    public List<String> brpop(int timeout, String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key); // timeout=0，无限阻塞
        }
        catch (Exception e)
        {
            logger.error("Redis添加数据出错：" + e.getMessage());
        }
        finally
        {
            if(jedis != null)
            {
                jedis.close(); // 释放连接
            }
        }

        return null;
    }
}

















