package com.zhou.qa.controller;

import com.zhou.qa.model.*;
import com.zhou.qa.service.CommentService;
import com.zhou.qa.service.QuestionService;
import com.zhou.qa.service.UserService;
import com.zhou.qa.util.QaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/4/30.
 */

// 问题相关的控制器，包括发布问题等
@Controller
public class QuestionController
{
    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserHolder userHolder;

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    // 发布问题请求
    // 通过调用js函数来提交发布问题请求，是一个POST请求，返回一个json字符串给前端更新页面
    // 具体看 resources/static/scripts/main/component/popupAdd.js 和 popup.js
    @RequestMapping(value = {"/question/add"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam(value = "title") String title,
                              @RequestParam(value = "content") String content)
    {
        try
        {
            // 用户未登录
            if(userHolder.getUser() == null)
            {
                // 返回码 999（见 popupAdd.js），跳转到登录页面
                return QaUtil.getResponseJsonString(999);
            }

            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setUserId(userHolder.getUser().getId());

            // 添加问题成功，返回码 0
            if(questionService.addQuestion(question) > 0)
            {
                return QaUtil.getResponseJsonString(0);
            }
        }
        catch (Exception e)
        {
            logger.error("发布问题异常：" + e.getMessage());
        }

        return QaUtil.getResponseJsonString(1, "失败");
    }

    // 处理 单击主页上某个问题的标题时 发起的请求
    // 返回该问题的详细页面
    @RequestMapping(value = {"/question/{questionId}"}, method = {RequestMethod.GET})
    public String questionDetail(@PathVariable("questionId") int questionId,
                                 Model model)
    {
        try
        {
            Question question = questionService.getQuestionById(questionId);
            model.addAttribute("question", question);

            // 获取该问题对应的所有评论
            List<Comment> cmt_list = commentService.getCommentsByEntity(questionId, EntityType.ENTITY_QUESTION);

            // 把每条评论和对应的user组合在一起，传给页面
            List<ViewObject> vos = new ArrayList<>();
            for(Comment comment : cmt_list)
            {
                ViewObject vo = new ViewObject();

                User user = userService.getUserById(comment.getUserId());
                vo.set("user", user);
                vo.set("comment", comment);

                vos.add(vo);
            }

            model.addAttribute("comments", vos);
        }
        catch (Exception e)
        {
            logger.error("获取问题的详细信息错误：" + e.getMessage());
        }

        return "detail";
    }
}



















