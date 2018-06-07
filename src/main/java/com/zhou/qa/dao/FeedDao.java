package com.zhou.qa.dao;

import com.zhou.qa.model.Feed;
import com.zhou.qa.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/6/2.
 */

@Mapper
public interface FeedDao
{
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, created_date, type, data ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")",
            "values(#{userId}, #{createdDate}, #{type}, #{data})"})
    int addFeed(Feed feed);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Feed selectFeedById(@Param("id") int id);

    // 通过 FeedDao.xml 配置文件实现动态 Sql
    List<Feed> selectFeedsByUserIds(@Param("maxId") int maxId,
                                    @Param("userIds") List<Integer> userIds,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);
}









