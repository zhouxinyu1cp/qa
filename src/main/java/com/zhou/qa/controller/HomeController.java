package com.zhou.qa.controller;

import com.zhou.qa.model.Question;
import com.zhou.qa.model.ViewObject;
import com.zhou.qa.service.QuestionService;
import com.zhou.qa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/4/16.
 */

// 项目中真正的主页
@Controller
public class HomeController
{
    public static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    private List<ViewObject> getQuestions(int userId, int offset, int limit)
    {
        List<Question> qs = questionService.getLatestQuestions(userId, offset, limit);

        List<ViewObject> vos = new ArrayList<>();

        for(Question q : qs)
        {
            ViewObject vo = new ViewObject();
            vo.set("question", q);
            vo.set("user", userService.getUserById(q.getUserId()));
            vos.add(vo);
        }

        return vos;
    }

    // 主页请求
    @RequestMapping(path={"/", "/home"}, method = {RequestMethod.GET})
    public String index(Model model)
    {
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "home";
    }

    // 处理user点击的超链接请求，返回当前userId的数据
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET})
    public String userPageHref(Model model,
                               @PathVariable("userId") int userId)
    {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        return "home";
    }
}











