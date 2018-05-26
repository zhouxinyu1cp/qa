package com.zhou.qa.async;

import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/5/26.
 */

// 事件处理器，要处理某个事件，实现该接口即可
public interface EventHandler
{
    void handleEvent(Event event); // 该方法内实现处理某个事件的具体逻辑

    List<EventType> getSupportEventTypes(); // 返回该事件处理器关心的事件类型
}




