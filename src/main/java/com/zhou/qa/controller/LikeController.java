package com.zhou.qa.controller;

import com.zhou.qa.async.Event;
import com.zhou.qa.async.EventSender;
import com.zhou.qa.async.EventType;
import com.zhou.qa.model.Comment;
import com.zhou.qa.model.EntityType;
import com.zhou.qa.model.UserHolder;
import com.zhou.qa.service.CommentService;
import com.zhou.qa.service.LikeService;
import com.zhou.qa.util.QaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhouxinyu1cp on 2018/5/24.
 */

// 点赞相关的控制器，包括点赞、踩赞
@Controller
public class LikeController
{
    public static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    UserHolder userHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    EventSender eventSender;

    // 点赞
    // 通过js提交请求，POST请求，返回一个json字符串给前端
    // 具体请求信息见 resources/static/scripts/main/util/action.js 和 main/site/detail.js
    @RequestMapping(value = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId)
    {
        try
        {
            // 未登录
            if(userHolder.getUser() == null)
            {
                return QaUtil.getResponseJsonString(999); // 跳转到登录页面
            }

            // 产生一个点赞事件，发给异步通知系统处理
            Comment comment = commentService.getCommentById(commentId);
            Event event = new Event(EventType.LIKE_EVENT);
            event.setFromUserId(userHolder.getUser().getId())
                    .setToUserId(comment.getUserId())
                    .setEntityType(EntityType.ENTITY_COMMENT)
                    .setEntityId(commentId)
                    .setExt("questionId", String.valueOf(comment.getEntityId()));
            // 发送事件
            eventSender.sendEvent(event);

            long likeCount = likeService.like(userHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId); // 点赞
            return QaUtil.getResponseJsonString(0, String.valueOf(likeCount)); // 返回当前的点赞数
        }
        catch (Exception e)
        {
            logger.error("点赞请求错误：" + e.getMessage());
        }

        return QaUtil.getResponseJsonString(1, "点赞错误");
    }

    // 点赞
    // 通过js提交请求，POST请求，返回一个json字符串给前端
    // 具体请求信息 resources/static/scripts/main/util/action.js 和 main/site/detail.js
    @RequestMapping(value = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId)
    {
        try
        {
            // 未登录
            if(userHolder.getUser() == null)
            {
                return QaUtil.getResponseJsonString(999); // 跳转到登录页面
            }

            long likeCount = likeService.dislike(userHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId); // 踩赞
            return QaUtil.getResponseJsonString(0, String.valueOf(likeCount)); // 返回当前的点赞数
        }
        catch (Exception e)
        {
            logger.error("踩赞请求错误：" + e.getMessage());
        }

        return QaUtil.getResponseJsonString(1, "踩赞错误");
    }
}













