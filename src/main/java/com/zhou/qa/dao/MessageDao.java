package com.zhou.qa.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.zhou.qa.model.Message;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/5/17.
 */

@Mapper
public interface MessageDao
{
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    String LIST_FIELDS = " from_id, to_id, content, has_read, conversation_id ";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")",
            " values(#{fromId}, #{toId}, #{content}, #{createdDate}, #{hasRead}, #{conversationId})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> selectMessageDetailsByConversationId(@Param("conversationId") String conversationId,
                                                       @Param("offset") int offset,
                                                       @Param("limit") int limit);

    @Select({"select ", LIST_FIELDS, ", tt.date as created_date, tt.cnt as id from message, ",
            "(select max(created_date) as date, count(id) as cnt from message where from_id=#{userId} or to_id=#{userId} group by conversation_id) as tt ",
            " where message.created_date=tt.date ",
            " order by created_date desc ",
            " limit #{offset}, #{limit}"})
    List<Message> selectMessageListByUserId(@Param("userId") int userId,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME,
            " where has_read=0 and to_id=#{toUserId} and conversation_id=#{conversationId}"})
    int selectConversationUnreadCount(@Param("toUserId") int userId,
                                      @Param("conversationId") String conversationId);
}











