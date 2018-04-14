package com.zhou.qa;

import com.zhou.qa.dao.UserDao;
import com.zhou.qa.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;

//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QaApplication.class)
@WebAppConfiguration
public class UserDaoTests
{
	@Autowired
	UserDao userDao;

	@Test
	public void initUserDatabase()
	{
		Random rand = new Random();

		for(int i = 0; i < 11; i++)
		{
			User u = new User();
			u.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", rand.nextInt(1000)));
			u.setName(String.format("User%d", i));
			u.setPassword("");
			u.setSalt("");

			userDao.addUser(u);
		}
	}

}












