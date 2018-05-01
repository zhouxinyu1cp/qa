package com.zhou.qa.model;

import org.springframework.stereotype.Component;

/**
 * Created by zhouxinyu1cp on 2018/4/22.
 */

// 通过拦截器 PassportInterceptor 获取的用户信息放入该容器中，
// 若用户已登录，后台有其登录的 token，拦截器能根据 token 获取到用户信息
// 若用户没登录，没有有效 token，拦截器获取不了用户信息
@Component
public class UserHolder
{
    // 多线程场景下，通过线程局部存储，每个线程保留一个 User 对象的副本，各线程使用的 User 对象互不影响，
    // 每个线程对应一个 http 请求，每个请求对应的 User 对象互不相同
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser()
    {
        return users.get();
    }

    public void setUser(User user)
    {
        users.set(user);
    }

    public void clear()
    {
        users.remove();
    }
}



















