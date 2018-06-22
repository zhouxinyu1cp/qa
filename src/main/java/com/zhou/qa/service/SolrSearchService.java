package com.zhou.qa.service;

import com.zhou.qa.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouxinyu1cp on 2018/6/22.
 */

// 提供solr全文搜索服务
@Service
public class SolrSearchService
{
    @Autowired
    private QuestionService questionService;

    private static final String SOLR_URL = "http://localhost:8983/solr/qa";
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";

    // solr引擎连接客户端
    private HttpSolrClient solrClient = new HttpSolrClient.Builder(SOLR_URL).build();

    // 根据关键词搜索相关的问题
    public List<Question> searchQuestions(String keyword, int start, int rowsNum,
                                          String hlPrev, String hlPost) throws Exception
    {
        List<Question> questions = new ArrayList<Question>();

        // 创建solr查询
        SolrQuery solrQuery = new SolrQuery(keyword);
        solrQuery.setStart(start); // 设置起始行
        solrQuery.setRows(rowsNum); // 设置返回行数
        solrQuery.setHighlight(true); // 设置高亮
        solrQuery.setHighlightSimplePre(hlPrev); // 设置高亮前缀
        solrQuery.setHighlightSimplePost(hlPost); // 设置高亮后缀
        solrQuery.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD); // 设置高亮影响的字段

        QueryResponse response = solrClient.query(solrQuery);
        Map<String, Map<String, List<String>>> hlResults = response.getHighlighting();
        for(Map.Entry<String, Map<String, List<String>>> entry : hlResults.entrySet())
        {
            int qId = Integer.parseInt(entry.getKey());
            Question question = questionService.getQuestionById(qId);

            if(entry.getValue().containsKey(QUESTION_TITLE_FIELD))
            {
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                if(titleList.size() > 0)
                {
                    question.setTitle(titleList.get(0));
                }
            }

            if(entry.getValue().containsKey(QUESTION_CONTENT_FIELD))
            {
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if(contentList.size() > 0)
                {
                    question.setContent(contentList.get(0));
                }
            }

            questions.add(question);
        }

        return questions;
    }

    // 发布新问题后，建立新问题的全文索引
    public boolean createIndexOfQuestion(int questionId, String questionTitle,
                                          String questionContent) throws Exception
    {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", questionId);
        doc.setField(QUESTION_TITLE_FIELD, questionTitle);
        doc.setField(QUESTION_CONTENT_FIELD, questionContent);
        solrClient.add(doc); // 添加新document的全文索引
        UpdateResponse response = solrClient.commit();

        return response != null && response.getStatus() == 0;
    }
}














