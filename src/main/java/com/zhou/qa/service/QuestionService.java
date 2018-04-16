package com.zhou.qa.service;

import com.zhou.qa.dao.QuestionDao;
import com.zhou.qa.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/4/16.
 */

@Service
public class QuestionService
{
    @Autowired
    private QuestionDao questionDao;

    public List<Question> getLatestQuestions(int userId, int offset, int limit)
    {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }
}














