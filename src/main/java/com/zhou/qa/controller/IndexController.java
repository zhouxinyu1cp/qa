package com.zhou.qa.controller;

import com.zhou.qa.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * Created by zhouxinyu1cp on 2018/4/4.
 */

@Controller
public class IndexController
{
    public static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    public IndexController() {}

    // 主页请求
    @RequestMapping(path={"/", "/index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index()
    {
        logger.info("VISIT INDEX");
        return "Hi, My first SpringBoot index";
    }

    // 获取路径参数、Http请求参数
    @RequestMapping(path={"/param/{gid}/{uid}"})
    @ResponseBody
    public String param(@PathVariable("gid") String gid,
                        @PathVariable("uid") int uid,
                        @RequestParam(name="type", defaultValue = "10") int type,
                        @RequestParam(name="key", defaultValue = "heihei", required = false) String key)
    {
        return String.format("%s: %d, type:%d, key:%s", gid, uid, type, key);
    }

    // 网页模板测试
    @RequestMapping(path = {"/template"}, method = {RequestMethod.GET})
    public String template(Model model)  // 通过 Model 把数据传递到网页上
    {
        model.addAttribute("value1", "zhouxinyu");

        List<String> arr = Arrays.asList("崎岖", "你妹", "真传进来了");
        model.addAttribute("arr", arr);

        Map<String, String> m = new HashMap<>();
        for(int i = 1; i <= 3; i++)
        {
            m.put(String.valueOf(i), String.valueOf(i + 10));
        }
        model.addAttribute("map", m);

        model.addAttribute("user", new User("zhou"));

        return "index";  // 返回 resources/templates 下的网页模板
    }

    // 测试 HttpServletRequest  HttpServletResponse
    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response)
//                          @CookieValue("JSESSIONID") String sessionId)
    {
        request.getSession(true);  // 服务端创建sessionId

        StringBuffer sb = new StringBuffer();

//        sb.append("COOKIEVALUE:" + sessionId + "<br>");

        Enumeration<String> hns = request.getHeaderNames();
        while(hns.hasMoreElements())
        {
            String name = hns.nextElement();
            sb.append(name + ": " + request.getHeader(name) + "<br>");
        }

        if(request.getCookies() != null)
        {
            for(Cookie cookie : request.getCookies())
            {
                sb.append("Cookie: " + cookie.getName() + "=" + cookie.getValue() + "<br>");
            }
        }

        sb.append(request.getMethod() + "<br>");
        sb.append(request.getRequestURI() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");

        response.addHeader("myNewId", "hello web project");
        response.addCookie(new Cookie("username", "daodaodao"));

        return sb.toString();
    }

    // 重定向测试
    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession session)
    {
        session.setAttribute("msg", "this is a redirect action");

        RedirectView rv = new RedirectView("/", true);  // 创建重定向跳转页面

        if(code == 301)
        {
            rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }

        return rv;
    }

    // 异常测试
    @RequestMapping(path = {"/exception"}, method = {RequestMethod.GET})
    @ResponseBody
    public String exception(@RequestParam(value = "key", required = false) String key)
    {
        if(key.equals("123"))
        {
            return "Hi, Exception";
        }

        throw new IllegalArgumentException("请求参数错误");
    }

    // 处理异常
    @ExceptionHandler()
    @ResponseBody
    public String processException(Exception e)
    {
        return "Exception: " + e.getMessage();
    }
}

















