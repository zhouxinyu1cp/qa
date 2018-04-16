package com.zhou.qa.dao;

import com.zhou.qa.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/4/14.
 */

@Mapper
public interface QuestionDao
{
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")",
            "values(#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question q);


    List<Question> selectLatestQuestions(@Param("userId") int userId,
                               @Param("offset") int offset,
                               @Param("limit") int limit);
}











