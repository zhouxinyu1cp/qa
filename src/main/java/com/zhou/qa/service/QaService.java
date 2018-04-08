package com.zhou.qa.service;

import org.springframework.stereotype.Service;

/**
 * Created by zhouxinyu1cp on 2018/4/8.
 */

// 演示依赖注入
@Service
public class QaService
{
    public String getMsg(int id)
    {
        return "Message from: " + String.valueOf(id);
    }
}
