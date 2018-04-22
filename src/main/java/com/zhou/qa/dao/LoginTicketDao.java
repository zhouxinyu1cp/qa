package com.zhou.qa.dao;

import com.zhou.qa.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by zhouxinyu1cp on 2018/4/22.
 */

@Mapper
public interface LoginTicketDao
{
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")",
            "values(#{userId}, #{ticket}, #{expired}, #{status})"})
    void addLoginTicket(LoginTicket ticket);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectByTicket(@Param("ticket") String ticket);

    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateByTicket(@Param("ticket") String ticket, @Param("status") int status);

    @Delete({"delete from ", TABLE_NAME, " where ticket=#{ticket}"})
    void deleteByTicket(@Param("ticket") String ticket);
}











