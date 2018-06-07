package com.zhou.qa.async.handlers;

import com.alibaba.fastjson.JSONObject;
import com.zhou.qa.async.Event;
import com.zhou.qa.async.EventHandler;
import com.zhou.qa.async.EventType;
import com.zhou.qa.model.*;
import com.zhou.qa.service.FeedService;
import com.zhou.qa.service.FollowService;
import com.zhou.qa.service.QuestionService;
import com.zhou.qa.service.UserService;
import com.zhou.qa.util.JedisAdapter;
import com.zhou.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by zhouxinyu1cp on 2018/6/2.
 */

// 处理 最新动态Feed流
@Component
public class FeedHandler implements EventHandler
{
    @Autowired
    FeedService feedService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void handleEvent(Event event)
    {
        /* 收到相关事件，添加该事件的最新动态feed */

        User fromUser = userService.getUserById(event.getFromUserId());

        Feed feed = new Feed();

        // 为了测试，userId设为1-10中的随机值
//        feed.setUserId(new Random().nextInt(10) + 1);
        feed.setUserId(fromUser.getId());
        feed.setCreatedDate(new Date());
        feed.setType(event.getEventType().getValue());

        // 把附加数据均设置到 feed.data 中
        Map<String, String> data = new HashMap<String, String>();
        data.put("userName", fromUser.getName());
        data.put("userHead", fromUser.getHeadUrl());

        Question question = null;
        if(event.getEventType() == EventType.FOLLOW_EVENT &&
                event.getEntityType() == EntityType.ENTITY_QUESTION)
        {
            question = questionService.getQuestionById(event.getEntityId());
        }
        else if(event.getEventType() == EventType.COMMENT_EVENT &&
                    event.getEntityType() == EntityType.ENTITY_QUESTION)
        {
            question = questionService.getQuestionById(event.getEntityId());
        }
        data.put("questionTitle", question.getTitle());
        data.put("questionId", String.valueOf(question.getId()));

        feed.setData(JSONObject.toJSONString(data));

        feedService.addFeed(feed);

        /*
         *  推模式下的timeline实现中，把产生的最新动态推给产生该事件的用户的所有粉丝，
         *  实质是把最新动态的feed.id推到每个粉丝的timeline队列中
         */
        // 获取产生该最新动态的用户的所有粉丝
        List<Integer> followerUserIds = followService.getFollowers(EntityType.ENTITY_USER,
                                                                fromUser.getId(),
                                                                0, Integer.MAX_VALUE);
        for(Integer followerUserId : followerUserIds)
        {
            // 利用 redis list 作为用户的 timeline 队列
            String timelineKey = RedisKeyUtil.getTimelineKey(followerUserId);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
        }
    }

    @Override
    public List<EventType> getSupportEventTypes()
    {
        // 暂时支持 关注事件的最新动态、评论事件的最新动态
        return Arrays.asList(EventType.FOLLOW_EVENT, EventType.COMMENT_EVENT);
    }
}















