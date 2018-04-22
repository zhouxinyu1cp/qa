package com.zhou.qa.interceptor;

import com.zhou.qa.dao.LoginTicketDao;
import com.zhou.qa.dao.UserDao;
import com.zhou.qa.model.LoginTicket;
import com.zhou.qa.model.User;
import com.zhou.qa.model.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by zhouxinyu1cp on 2018/4/22.
 */

// 拦截器演示，拦截用户的请求，通过 token 取出用户信息，放入Spring框架上下文供全局使用
// 拦截器使用场景：1、判断发起请求的用户是谁    2、判断发起请求的用户有没有权限访问当前页面
// 一个拦截器只做一件事，不要把多个职责整合到一个拦截器里

// 下面是用来判断用户身份的拦截器
@Component
public class PassportInterceptor implements HandlerInterceptor
{
    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserHolder userHolder;

    // Controller处理请求前被调用，一般用于判断该请求是否可以继续执行，或是取出一些公共信息
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object o) throws Exception
    {
        String ticket = null;
        Cookie[] cks = httpServletRequest.getCookies();
        if(cks != null)
        {
            for(Cookie cookie : cks)
            {
                if(cookie.getName().equals("ticket"))
                {
                    // 若浏览器发来有 token，则获取该 token
                    ticket = cookie.getValue();
                    break;
                }
            }

            if(ticket != null)
            {
                LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);

                // 判断 token 的有效性
                if(loginTicket == null ||
                   loginTicket.getStatus() != 0 ||
                   loginTicket.getExpired().before(new Date()))
                {
                    return true;
                }

                // 取出有效 token 对应的 User，放入线程局部存储中 待后续使用
                User user = userDao.selectById(loginTicket.getUserId());
                userHolder.setUser(user);
            }
        }

        return true;  // 若返回 false，整个请求结束，不再走后续流程
    }

    // Controller处理后、渲染前被调用，用于一些数据交换、数据推送等场景
    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o,
                           ModelAndView modelAndView) throws Exception
    {
        if(modelAndView != null)
        {
            // 添加完成后，可以在后续的视图页面里（就是模板里）通过 "user" 来访问取出的 User 对象
            modelAndView.addObject("user", userHolder.getUser());
        }
    }

    // 请求结束前被调用，释放资源等操作
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o,
                                Exception e) throws Exception
    {
        // 结束前清理掉
        userHolder.clear();
    }
}










