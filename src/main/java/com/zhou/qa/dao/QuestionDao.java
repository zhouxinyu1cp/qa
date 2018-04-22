package com.zhou.qa.dao;

import com.zhou.qa.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/4/14.
 */

// MyBatis 插入数据库
@Mapper
public interface QuestionDao
{
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")",
            "values(#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question q);

    // 带有复杂逻辑的SQL语句使用 MyBatis 的 XML文件配置进行映射，
    // 在 /resources 文件夹下找到同名包目录下的同名xml文件，根据 selectLatestQuestions 作为id找到对应的SQL语句
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                               @Param("offset") int offset,
                               @Param("limit") int limit);
}











