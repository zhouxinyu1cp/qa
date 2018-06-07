package com.zhou.qa.service;

import com.zhou.qa.dao.FeedDao;
import com.zhou.qa.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/6/2.
 */

// 最新动态timeline相关的业务
@Service
public class FeedService
{
    @Autowired
    FeedDao feedDao;

    // 添加一条最新动态
    public int addFeed(Feed feed)
    {
        return feedDao.addFeed(feed) > 0 ? feed.getId() : 0;
    }

    // 通过id获取最新动态
    public Feed getFeedById(int id)
    {
        return feedDao.selectFeedById(id);
    }

    // 通过相关联users的最新动态
    public List<Feed> getFeedsByUserIds(int maxId, List<Integer> userIds,
                                        int offset, int limit)
    {
        return feedDao.selectFeedsByUserIds(maxId, userIds, offset, limit);
    }
}












