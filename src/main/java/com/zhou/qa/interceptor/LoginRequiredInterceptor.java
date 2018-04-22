package com.zhou.qa.interceptor;

import com.zhou.qa.model.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhouxinyu1cp on 2018/4/22.
 */

// "需要登录"权限的拦截器，有的页面需要登录，利用该拦截器实现未登录状态跳转登录页面

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor
{
    @Autowired
    UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object o) throws Exception
    {
        // 用户未登录，没有用户信息，跳转登录
        if(userHolder.getUser() == null)
        {
            // 跳转到登录页面，并附加上当前 http 请求的页面 URL 的请求参数
            httpServletResponse.sendRedirect("/reglogin?next=" + httpServletRequest.getRequestURI());
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o,
                           ModelAndView modelAndView) throws Exception
    {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o,
                                Exception e) throws Exception
    {

    }
}













