package com.zhou.qa.async;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.zhou.qa.util.JedisAdapter;
import com.zhou.qa.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouxinyu1cp on 2018/5/26.
 */

// 事件接收器，接收事件并把事件分发给指定的EventHandler
@Component
public class EventReceiver implements InitializingBean, ApplicationContextAware
{
    public static final Logger logger = LoggerFactory.getLogger(EventReceiver.class);

    @Autowired
    JedisAdapter jedisAdapter;

    private ApplicationContext applicationContext;

    // 事件分发器，把事件发给对其感兴趣的EventHandler处理
    // 某种类型的事件可能需要多个EventHandler处理
    private Map<EventType, List<EventHandler>> dispatcher = new HashMap<EventType, List<EventHandler>>();

    private void registerEventHandlers()
    {
        // 找到所有实现了EventHandler接口的类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans != null)
        {
            for(Map.Entry<String, EventHandler> entry : beans.entrySet())
            {
                EventHandler handler = entry.getValue();
                List<EventType> types = handler.getSupportEventTypes(); // 该EventHandler关心的事件类型
                for(EventType type : types)
                {
                    if(!dispatcher.containsKey(type))
                    {
                        dispatcher.put(type, new ArrayList<EventHandler>()); // 没有映射关系，创建
                    }

                    dispatcher.get(type).add(handler); // 添加EventType和EventHandler的映射关系
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        // 该接口提供设置applicationContext
        this.applicationContext = applicationContext;
    }

    // 初始化工作
    @Override
    public void afterPropertiesSet() throws Exception
    {
        /* 注册EventHandler，把 EventType-EventHandler 映射关系添加到dispatcher中 */
        registerEventHandlers();

        /*
         * 启动线程，从redis中对应的事件队列中不断的取事件，
         * 然后分发给对应的EventHandler处理
         */
        new Thread()
        {
            @Override
            public void run()
            {
                String queeuKey = RedisKeyUtil.getQueeuKey();

                while(true)
                {
                    List<String> vals = jedisAdapter.brpop(0, queeuKey); // 没有元素则阻塞
                    for(String val : vals)
                    {
                        // 返回的是key，跳过
                        if(val.equals(queeuKey)) { continue; }

                        // 从Event的json串中反序列化出Event对象
                        Event event = JSONObject.parseObject(val, Event.class);

                        // 若没有该事件的EventHandler，跳过
                        if(!dispatcher.containsKey(event.getEventType()))
                        {
                            logger.error("未知的事件");
                            continue;
                        }

                        // 根据EventType找到对应的EventHandler
                        for(EventHandler handler : dispatcher.get(event.getEventType()))
                        {
                            handler.handleEvent(event); // 每个Handler处理该事件
                        }
                    }
                }
            }
        }.start();
    }
}













