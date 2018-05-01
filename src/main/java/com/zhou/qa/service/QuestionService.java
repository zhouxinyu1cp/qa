package com.zhou.qa.service;

import com.zhou.qa.dao.QuestionDao;
import com.zhou.qa.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/4/16.
 */

@Service
public class QuestionService
{
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private SensitiveService sensitiveService;

    public List<Question> getLatestQuestions(int userId, int offset, int limit)
    {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }

    // 处理发布问题请求，添加一条问题记录
    public int addQuestion(Question question)
    {
        // 过滤特殊的html标签，如<script>等，防止xss等攻击
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        // 过滤敏感词，把敏感词替换成"xxx"
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

        // 添加成功，返回问题id
        return questionDao.addQuestion(question) > 0 ? question.getId() : 0;
    }

    // 根据 id 获取 Question
    public Question getQuestionById(int questionId)
    {
        return questionDao.selectById(questionId);
    }
}














