package com.zhou.qa.service;

import com.zhou.qa.dao.CommentDao;
import com.zhou.qa.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/5/13.
 */

@Service
public class CommentService
{
    @Autowired
    CommentDao commentDao;

    @Autowired
    SensitiveService sensitiveService;

    // 添加评论
    public int addComment(Comment comment)
    {
        // 过滤掉html标签
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        // 过滤掉敏感词
        comment.setContent(sensitiveService.filter(comment.getContent()));

        return commentDao.addComment(comment) > 0 ? comment.getId() : 0;
    }

    // 根据 实体id 和 实体类型 获取关联的所有评论
    public List<Comment> getCommentsByEntity(int entityId, int entityType)
    {
        return commentDao.selectCommentsByEntityId(entityId, entityType);
    }

    // 根据 实体id 和 实体类型 获取关联的评论数
    public int getCommentsCountByEntityId(int entityId, int entityType)
    {
        return commentDao.getCommentsCountByEntityId(entityId, entityType);
    }

    // 根据指定id获取对应的评论
    public Comment getCommentById(int commentId)
    {
        return commentDao.selectByCommentId(commentId);
    }

    // 获取某用户发表了多少评论数
    public int getCommentCountByUserId(int userId)
    {
        return commentDao.getCommentCountByUserId(userId);
    }
}










