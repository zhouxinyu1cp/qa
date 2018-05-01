package com.zhou.qa.service;

import org.apache.commons.lang.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxinyu1cp on 2018/5/1.
 */

// 实现敏感词过滤的业务
// 使用前缀树过滤文本
@Service
public class SensitiveService implements InitializingBean
{
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    // 敏感词的替换词
    private static final String REPLACE_STR = "xxx";

    // 前缀树
    private class TrieNode
    {
        // 子节点集，Character是子节点字符，TrieNode是对应的子节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        // 结束标记，从根节点到当前节点，是否构成一个敏感词，若是，bEnd = true
        private boolean bEnd = false;

        public TrieNode getSubNode(Character key)
        {
            if(subNodes.containsKey(key))
            {
                return subNodes.get(key);
            }

            return null;
        }

        public void addSubNode(Character key, TrieNode node) { subNodes.put(key, node); }

        public boolean isEnd() { return bEnd; }

        public void setEnd(boolean end) { this.bEnd = end; }
    }

    // 前缀树 根节点，根节点没有值
    private TrieNode rootNode = new TrieNode();

    // 判断是不是无效字符
    // @return：true - 无效文本，false - 有效
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        // 过滤掉不是字母表中的字符（东亚文字是有效字符）
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    // 在前缀树中添加敏感词
    public void addSensetiveWord(String lineWord)
    {
        TrieNode loopNode = rootNode;

        for(int i = 0; i < lineWord.length(); i++)
        {
            Character c = lineWord.charAt(i);

            if(isSymbol(c)) { continue; }

            TrieNode node = loopNode.getSubNode(c);
            // 若没有，则在前缀树中添加节点
            if(node == null)
            {
                node = new TrieNode();
                loopNode.addSubNode(c, node);
            }

            // 继续遍历前缀树
            loopNode = node;

            // 到达敏感词的最后一个字符处，设置当前节点的结束标记为 true
            if(i == lineWord.length() - 1)
            {
                loopNode.setEnd(true);
            }
        }
    }

    // 过滤文本中的敏感词，
    public String filter(String text)
    {
        StringBuilder result = new StringBuilder();

        int position = 0; // 遍历文本用的下标
        int rollback_pos = 0; // 回滚到开始匹配敏感词的下标
        TrieNode node = rootNode;

        while(position < text.length())
        {
            Character c = text.charAt(position);

            // 跳过正在匹配一个敏感词过程中的无效字符
            if(isSymbol(c))
            {
                // 若一个敏感词匹配完毕，应保留所有的其余文本
                if(node == rootNode)
                {
                    result.append(c);
                    rollback_pos++;
                }

                position++;
                continue;
            }

            node = node.getSubNode(c);

            // 不是敏感词中的字符
            if(node == null)
            {
                result.append(text.charAt(rollback_pos)); // 从 rollback_pos 开始的不是敏感词，添加到替换后的文本中
                position = rollback_pos + 1; // position 跳回到 rollback_pos + 1 处
                rollback_pos = position; // rollback_pos 与 position 重新对其
                node = rootNode; // 下一次重新从根节点开始搜索前缀树
            }
            // 在文本中找到一个敏感词的全部字符
            else if(node.isEnd())
            {
                // 从 rollback_pos 到 position 是一个敏感词，用"xxx"替换
                result.append(REPLACE_STR);
                position = position + 1;
                rollback_pos = position;
                node = rootNode;
            }
            // 继续匹配敏感词的下一个字符
            else
            {
                position++;
            }
        }

        // 若文本最后仅含有某个敏感词的部分字符，不是敏感词，将其拷贝过来
        result.append(text.substring(rollback_pos));

        return result.toString();
    }

    // 该类对象初始化后，调用下面方法完成一些属性设置工作
    @Override
    public void afterPropertiesSet() throws Exception
    {
        rootNode = new TrieNode();

        try
        {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                addSensetiveWord(line.trim());
            }

            read.close();
        }
        catch (Exception e)
        {
            logger.error("读取敏感词文件失败：" + e.getMessage());
        }
    }
}















