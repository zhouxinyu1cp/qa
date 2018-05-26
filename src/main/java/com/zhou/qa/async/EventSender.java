package com.zhou.qa.async;

import com.alibaba.fastjson.JSONObject;
import com.zhou.qa.util.JedisAdapter;
import com.zhou.qa.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhouxinyu1cp on 2018/5/26.
 */

// 事件发生器，将事件发往指定的队列中
@Component
public class EventSender
{
    public static final Logger logger = LoggerFactory.getLogger(EventSender.class);

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean sendEvent(Event event)
    {
        try
        {
            String queeuKey = RedisKeyUtil.getQueeuKey();

            String val = JSONObject.toJSONString(event); // 把Event序列化成json字符串后，放入对应的redis队列中
            jedisAdapter.lpush(queeuKey, val);
            return true;
        }
        catch (Exception e)
        {
            logger.error("发送事件出错：" + e.getMessage());
        }

        return false;
    }
}






