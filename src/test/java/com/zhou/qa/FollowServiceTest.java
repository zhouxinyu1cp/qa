package com.zhou.qa;

import com.zhou.qa.model.EntityType;
import com.zhou.qa.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.SystemProfileValueSource;
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
public class FollowServiceTest
{
    @Autowired
    FollowService followService;

    @Test
    public void initFollowData()
    {
        for (int i = 0; i < 11; ++i)
        {
            for (int j = 1; j < i; ++j)
            {
                followService.follow(j, EntityType.ENTITY_USER, i);
            }
        }
    }
}
