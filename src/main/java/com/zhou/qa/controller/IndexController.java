package com.zhou.qa.controller;

import com.zhou.qa.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public IndexController() {}

    @RequestMapping(path={"/", "/index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index()
    {
        return "Hi, My first SpringBoot index";
    }

    @RequestMapping(path={"/param/{gid}/{uid}"})
    @ResponseBody
    public String param(@PathVariable("gid") String gid,
                        @PathVariable("uid") int uid,
                        @RequestParam(name="type", defaultValue = "10") int type,
                        @RequestParam(name="key", defaultValue = "heihei", required = false) String key)
    {
        return String.format("%s: %d, type:%d, key:%s", gid, uid, type, key);
    }

    @RequestMapping(path = {"/template"}, method = {RequestMethod.GET})
    public String template(Model model)
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

        return "index";
    }

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
}

















