package com.zhou.qa.dao;

import com.zhou.qa.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by zhouxinyu1cp on 2018/4/14.
 */

// MyBatis 插入数据库，简单的SQL语句直接用注解即可
@Mapper
public interface UserDao
{
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")",
            "values(#{name}, #{password}, #{salt}, #{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{username}"})
    User selectByName(@Param("username") String username);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePasswd(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);
}











