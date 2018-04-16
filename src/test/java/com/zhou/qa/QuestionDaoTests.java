package com.zhou.qa;

import com.zhou.qa.dao.QuestionDao;

import com.zhou.qa.model.Question;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.SystemProfileValueSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QaApplication.class)
@WebAppConfiguration
public class QuestionDaoTests
{
	@Autowired
	QuestionDao questionDao;

	@Test
	public void initQuestionDatabase()
	{
		Random rand = new Random();

		for(int i = 0; i < 11; i++)
		{
			Question q = new Question();
			q.setCommentCount(1);
			Date d = new Date();
			d.setTime(d.getTime() + 3600 * 1000 * i);
			q.setCreatedDate(d);
			q.setUserId(i + 1);
			q.setTitle(String.format("Title%d", i));
			q.setContent(String.format("afafafafafaf content %d", i));

			questionDao.addQuestion(q);
		}

		System.out.println(questionDao.selectLatestQuestions(0, 0, 10));
	}

}












