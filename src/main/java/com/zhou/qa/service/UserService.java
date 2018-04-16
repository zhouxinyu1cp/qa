package com.zhou.qa.service;

import com.zhou.qa.dao.UserDao;
import com.zhou.qa.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouxinyu1cp on 2018/4/16.
 */

@Service
public class UserService
{
    @Autowired
    private UserDao userDao;

    public User getUserById(int id)
    {
        return userDao.selectById(id);
    }
}









