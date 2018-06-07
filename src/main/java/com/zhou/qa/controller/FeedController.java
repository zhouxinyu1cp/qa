package com.zhou.qa.controller;

import com.zhou.qa.model.EntityType;
import com.zhou.qa.model.Feed;
import com.zhou.qa.model.UserHolder;
import com.zhou.qa.service.FeedService;
import com.zhou.qa.service.FollowService;
import com.zhou.qa.util.JedisAdapter;
import com.zhou.qa.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/6/2.
 */

// timeline最新动态的控制器，实现了 推/拉 两种模式
@Controller
public class FeedController
{
    @Autowired
    UserHolder userHolder;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    // 拉模式下的timeline实现，
    // 当前用户登录后主动去读取所关注用户产生的最新动态
    @RequestMapping(value = {"/pullFeeds"}, method = {RequestMethod.GET})
    public String pullFeeds(Model model)
    {
        // 若当前用户未登录，应随意读取最新动态，此处暂不处理
        if(userHolder.getUser() == null)
        {
            return "feeds";
        }

        // 获取当前用户关注的所有用户
        List<Integer> userIds =  followService.getFollowees(userHolder.getUser().getId(),
                                                                EntityType.ENTITY_USER,
                                                                0, Integer.MAX_VALUE);

        // 从数据库中主动拉取所关注用户的最新动态
        List<Feed> feeds = feedService.getFeedsByUserIds(Integer.MAX_VALUE, userIds, 0, 20);

        // 利用模板渲染数据，在 Model 上设置数据，未设置的字段从 feed.data 中读取
        // feed.data 是一个JSON串，从数据库读出时已设置好，且提供了feed.get(key)方法
        model.addAttribute("feeds", feeds);

        return "feeds";
    }

    // 推模式下的timeline实现，产生的最新动态被推给对应的用户，
    // 每个用户有一个timeline队列，取最新动态时，从自己的timeline队列中取即可
    @RequestMapping(value = {"/pushFeeds"}, method = {RequestMethod.GET})
    public String pushFeeds(Model model)
    {
        // 若当前用户未登录，应随意读取最新动态，此处暂不处理
        if(userHolder.getUser() == null)
        {
            return "feeds";
        }

        List<Feed> feeds = new ArrayList<Feed>();

        String timelineKey = RedisKeyUtil.getTimelineKey(userHolder.getUser().getId());
        List<String> feedIds = jedisAdapter.lrange(timelineKey, 0, 10);
        for(String feedId : feedIds)
        {
            Feed feed = feedService.getFeedById(Integer.parseInt(feedId));
            if(feed != null)
            {
                feeds.add(feed);
            }
        }

        model.addAttribute("feeds", feeds);

        return "feeds";
    }
}













