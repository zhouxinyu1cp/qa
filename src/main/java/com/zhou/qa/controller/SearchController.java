package com.zhou.qa.controller;

import com.zhou.qa.model.EntityType;
import com.zhou.qa.model.Question;
import com.zhou.qa.model.User;
import com.zhou.qa.model.ViewObject;
import com.zhou.qa.service.FollowService;
import com.zhou.qa.service.SolrSearchService;
import com.zhou.qa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/6/22.
 */

// 站内搜索入口
@Controller
public class SearchController
{
    public static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private SolrSearchService solrSearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @RequestMapping(value = {"/search"}, method = {RequestMethod.GET})
    public String search(@RequestParam("q") String keyword,
                         Model model)
    {
        try
        {
            List<ViewObject> vos = new ArrayList<ViewObject>();

            List<Question> questionList = solrSearchService.searchQuestions(keyword, 0, 10,
                                                                                "<em>", "</em>");
            for(Question question : questionList)
            {
                ViewObject vo = new ViewObject();

                User user = userService.getUserById(question.getUserId());
                vo.set("user", user);

                vo.set("question", question);

                long followerCount = followService.getFollowersCount(EntityType.ENTITY_QUESTION, question.getId());
                vo.set("followCount", followerCount);

                vos.add(vo);
            }

            model.addAttribute("vos", vos);
        }
        catch (Exception e)
        {
            logger.error("站内搜索出错：" + e.getMessage());
        }

        return "result";
    }
}











