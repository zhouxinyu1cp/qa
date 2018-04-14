package com.zhou.qa.dao;

import com.zhou.qa.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by zhouxinyu1cp on 2018/4/14.
 */

@Mapper
public interface UserDao
{
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")",
            "values(#{name}, #{password}, #{salt}, #{headUrl})"})
    int addUser(User user);
}





