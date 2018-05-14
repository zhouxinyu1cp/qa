package com.zhou.qa.controller;

import com.zhou.qa.model.Comment;
import com.zhou.qa.model.EntityType;
import com.zhou.qa.model.UserHolder;
import com.zhou.qa.service.CommentService;
import com.zhou.qa.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by zhouxinyu1cp on 2018/5/13.
 */

// 评论相关的控制器，包括添加评论等
@Controller
public class CommentController
{
    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    UserHolder userHolder;

    @Autowired
    QuestionService questionService;

    // 添加评论，在 detail.html 中有个 form 表单提交评论，URL请求是 /addComment
    @RequestMapping(value = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam(value = "questionId") int questionId,
                             @RequestParam(value = "content") String content,
                             HttpServletRequest request)
    {
        try
        {
            // 用户未登录，跳转登录页面
            if(userHolder.getUser() == null)
            {
                // next参数是为了登录后跳回当前页面
                return "redirect:/reglogin?next=" + request.getRequestURI();
            }
            else
            {
                Comment comment = new Comment();
                comment.setUserId(userHolder.getUser().getId());
                comment.setCreatedDate(new Date());
                comment.setContent(content);
                comment.setEntityType(EntityType.ENTITY_QUESTION);
                comment.setEntityId(questionId);
                comment.setStatus(0);

                // 添加评论
                commentService.addComment(comment);

                // 更新数据库中该条问题所对应的 comment_count 字段
                // （考虑做成一个事物，添加评论及修改comment_count字段应该为一个操作，后续可设计成异步方式）
                int counts = commentService.getCommentsCountByEntityId(comment.getEntityId(), comment.getEntityType());
                questionService.updateCommentCountById(comment.getEntityId(), counts);
            }
        }
        catch (Exception e)
        {
            logger.error("添加评论错误：" + e.getMessage());
        }

        // 跳转到当前问题的详细页
        return "redirect:/question/" + String.valueOf(questionId);
    }
}












