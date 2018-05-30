package com.zhou.qa.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.zhou.qa.async.Event;
import com.zhou.qa.async.EventSender;
import com.zhou.qa.async.EventType;
import com.zhou.qa.model.EntityType;
import com.zhou.qa.model.User;
import com.zhou.qa.model.UserHolder;
import com.zhou.qa.model.ViewObject;
import com.zhou.qa.service.CommentService;
import com.zhou.qa.service.FollowService;
import com.zhou.qa.service.UserService;
import com.zhou.qa.util.QaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouxinyu1cp on 2018/5/28.
 */

// 关注、取关的控制器，包括关注用户、关注问题、取消关注、获取关注列表等
@Controller
public class FollowController
{
    public static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    UserHolder userHolder;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    EventSender eventSender;

    // 关注某问题
    // 通过js提交请求，POST请求，返回一个json字符串给前端
    // 具体请求信息见 resources/static/scripts/main/util/action.js 和 main/util/business.js
    @RequestMapping(value = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId)
    {
        try
        {
            // 未登录，跳转登录
            if(userHolder.getUser() == null)
            {
                return QaUtil.getResponseJsonString(999);
            }

            if(followService.follow(userHolder.getUser().getId(),
                    EntityType.ENTITY_QUESTION, questionId))
            {
                /* 产生一个关注事件 */
                Event event = new Event(EventType.FOLLOW_EVENT);
                event.setFromUserId(userHolder.getUser().getId())
                        .setEntityType(EntityType.ENTITY_QUESTION)
                            .setEntityId(questionId);
                eventSender.sendEvent(event);

                // 关注成功，返给前端相关信息
                Map<String, Object> info = new HashMap<String, Object>();
                info.put("id", userHolder.getUser().getId());
                info.put("name", userHolder.getUser().getName());
                info.put("headUrl", userHolder.getUser().getHeadUrl());
                // 返回当前问题的粉丝数
                info.put("count", followService.getFollowersCount(EntityType.ENTITY_QUESTION, questionId));

                return QaUtil.getResponseJsonString(0, info);
            }

        }
        catch (Exception e)
        {
            logger.error("关注问题出错：" + e.getMessage());
        }

        return QaUtil.getResponseJsonString(1, "关注问题出错");
    }

    // 取关某问题
    // 见上
    @RequestMapping(value = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId)
    {
        try
        {
            // 未登录，跳转登录
            if(userHolder.getUser() == null)
            {
                return QaUtil.getResponseJsonString(999);
            }

            if(followService.unfollow(userHolder.getUser().getId(),
                    EntityType.ENTITY_QUESTION, questionId))
            {
                 /* 产生一个取关事件 */
                Event event = new Event(EventType.UNFOLLOW_EVENT);
                event.setFromUserId(userHolder.getUser().getId())
                        .setEntityType(EntityType.ENTITY_QUESTION)
                            .setEntityId(questionId);
                eventSender.sendEvent(event);

                Map<String, Object> info = new HashMap<String, Object>();
                info.put("id", userHolder.getUser().getId());
                // 返回当前问题的粉丝数
                info.put("count", followService.getFollowersCount(EntityType.ENTITY_QUESTION, questionId));

                return QaUtil.getResponseJsonString(0, info);
            }

        }
        catch (Exception e)
        {
            logger.error("取关问题出错：" + e.getMessage());
        }

        return QaUtil.getResponseJsonString(1, "取关问题出错");
    }

    // 关注某用户
    // 同上
    @RequestMapping(value = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId)
    {
        try
        {
            // 未登录，跳转登录
            if(userHolder.getUser() == null)
            {
                return QaUtil.getResponseJsonString(999);
            }

            if(followService.follow(userHolder.getUser().getId(),
                    EntityType.ENTITY_USER, userId))
            {
                /* 产生一个关注事件 */
                Event event = new Event(EventType.FOLLOW_EVENT);
                event.setFromUserId(userHolder.getUser().getId())
                        .setEntityType(EntityType.ENTITY_USER)
                            .setEntityId(userId);
                eventSender.sendEvent(event);

                // 返回当前用户已关注的用户数量
                long followeeCount = followService.getFolloweesCount(userHolder.getUser().getId(),
                                                                        EntityType.ENTITY_USER);
                return QaUtil.getResponseJsonString(0, String.valueOf(followeeCount));
            }

        }
        catch (Exception e)
        {
            logger.error("关注用户出错：" + e.getMessage());
        }

        return QaUtil.getResponseJsonString(1, "关注用户出错");
    }

    // 取关某用户
    // 同上
    @RequestMapping(value = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId)
    {
        try
        {
            // 未登录，跳转登录
            if(userHolder.getUser() == null)
            {
                return QaUtil.getResponseJsonString(999);
            }

            if(followService.unfollow(userHolder.getUser().getId(),
                    EntityType.ENTITY_USER, userId))
            {
                /* 产生一个取关事件 */
                Event event = new Event(EventType.UNFOLLOW_EVENT);
                event.setFromUserId(userHolder.getUser().getId())
                        .setEntityType(EntityType.ENTITY_USER)
                            .setEntityId(userId);
                eventSender.sendEvent(event);

                // 返回当前用户已关注的用户数量
                long followeeCount = followService.getFolloweesCount(userHolder.getUser().getId(),
                                                                        EntityType.ENTITY_USER);
                return QaUtil.getResponseJsonString(0, String.valueOf(followeeCount));
            }

        }
        catch (Exception e)
        {
            logger.error("取关用户出错：" + e.getMessage());
        }

        return QaUtil.getResponseJsonString(1, "取关用户出错");
    }

    // 某用户的粉丝页请求
    @RequestMapping(value = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(@PathVariable("uid") int userId,
                            Model model)
    {
        try
        {
            User curUser = userService.getUserById(userId);
            model.addAttribute("curUser", curUser);

            long followerCount = followService.getFollowersCount(EntityType.ENTITY_USER, userId);
            model.addAttribute("followerCount", followerCount); // 粉丝数

            List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
            model.addAttribute("followers", getFollowUserInfo(followerIds)); // 每个粉丝的具体信息
        }
        catch(Exception e)
        {
            logger.error("请求粉丝页失败：" + e.getMessage());
        }

        return "followers";
    }

    // 某用户的关注页请求
    @RequestMapping(value = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(@PathVariable("uid") int userId,
                            Model model)
    {
        try
        {
            User curUser = userService.getUserById(userId);
            model.addAttribute("curUser", curUser);

            long followeeCount = followService.getFolloweesCount(userId, EntityType.ENTITY_USER);
            model.addAttribute("followeeCount", followeeCount); // 关注数

            List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);
            model.addAttribute("followees", getFollowUserInfo(followeeIds)); // 每个关注的具体信息
        }
        catch(Exception e)
        {
            logger.error("请求关注页失败：" + e.getMessage());
        }

        return "followees";
    }


    // helper函数，转成 ViewObject
    private List<ViewObject> getFollowUserInfo(List<Integer> userIds)
    {
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for(Integer uid : userIds)
        {
            ViewObject vo = new ViewObject();

            boolean followed = false;
            if(userHolder.getUser() != null)
            {
                // 当前已登录的用户是否已关注过该用户
                followed = followService.isFollower(userHolder.getUser().getId(), EntityType.ENTITY_USER, uid);
            }

            vo.set("followed", followed);
            vo.set("user", userService.getUserById(uid));
            vo.set("followerCount", followService.getFollowersCount(EntityType.ENTITY_USER, uid)); // 该用户的粉丝数
            vo.set("followeeCount", followService.getFolloweesCount(uid, EntityType.ENTITY_USER)); // 该用户关注了多少其他用户
            vo.set("commentCount", commentService.getCommentCountByUserId(uid)); // 该用户发表的评论数

            vos.add(vo);
        }

        return vos;
    }
}














