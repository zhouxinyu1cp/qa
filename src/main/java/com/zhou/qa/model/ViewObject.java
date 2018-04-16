package com.zhou.qa.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxinyu1cp on 2018/4/16.
 */

// 用于组合数据，把不同类型的数据集合在一起 以便使用
public class ViewObject
{
    private Map<String, Object> data = new HashMap<>();

    public void set(String key, Object val)
    {
        data.put(key, val);
    }

    public Object get(String key)
    {
        return data.get(key);
    }
}











