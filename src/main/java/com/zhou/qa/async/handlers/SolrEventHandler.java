package com.zhou.qa.async.handlers;

import com.zhou.qa.async.Event;
import com.zhou.qa.async.EventHandler;
import com.zhou.qa.async.EventType;
import com.zhou.qa.model.EntityType;
import com.zhou.qa.service.SolrSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/6/22.
 */

// 处理solr相关事件
@Component
public class SolrEventHandler implements EventHandler
{
    @Autowired
    private SolrSearchService solrSearchService;

    @Override
    public void handleEvent(Event event)
    {
        if(event.getEntityType() == EntityType.ENTITY_QUESTION)
        {
            int qId = event.getEntityId();
            String qTitle = event.getExt("question_title");
            String qContent = event.getExt("question_content");

            // 建立新问题的全文索引
            try{
                solrSearchService.createIndexOfQuestion(qId, qTitle, qContent);
            } catch (Exception e) {
                System.out.println("建立solr索引出错" + e.getMessage());
            }

        }
    }

    @Override
    public List<EventType> getSupportEventTypes()
    {
        return Arrays.asList(EventType.SOLR_CREATE_INDEX_EVENT);
    }
}
