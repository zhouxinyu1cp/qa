package com.zhou.qa.controller;

import com.zhou.qa.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by zhouxinyu1cp on 2018/4/22.
 */

//  处理登录/注册相关的请求
@Controller
public class LoginController
{
    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    // 注册登录页面
    @RequestMapping(value = {"/reglogin"}, method = {RequestMethod.GET})
    public String reglogin(Model model,
                           @RequestParam(value = "next", required = false) String nextUrl)
    {
        // 若有跳转请求，获取要跳转的页面的url，将其埋入页面数据中，传递到下一个页面请求中
        model.addAttribute("next", nextUrl);
        return "login";
    }

    // 注册请求
    @RequestMapping(path={"/reg"}, method = {RequestMethod.POST})
    public String register(Model model,
                           @RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberMe,
                           @RequestParam(value = "next", required = false) String nextUrl,
                           HttpServletResponse response)
    {
        try
        {
            Map<String, Object> map = userService.register(username, password);
            if(map.containsKey("msg"))
            {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath("/");
            if(rememberMe)
            {
                cookie.setMaxAge(3600 * 24 * 30);
            }
            response.addCookie(cookie);

            // 有跳转请求，注册完成后跳转到指定url
            if(StringUtils.isNotBlank(nextUrl))
            {
                return ("redirect:" + nextUrl);
            }

            return "redirect:/";
        }
        catch (Exception e)
        {
            logger.error("注册异常：" + e.getMessage());
            return "login";
        }
    }

    // 登录请求
    @RequestMapping(value = {"/login"}, method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberMe,
                        @RequestParam(value = "next", required = false) String nextUrl,
                        HttpServletResponse response)
    {
        try
        {
            Map<String, Object> map = userService.login(username, password);
            if(map.containsKey("msg"))
            {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath("/");
            if(rememberMe)
            {
                cookie.setMaxAge(3600 * 24 * 30);
            }
            response.addCookie(cookie);

            // 有跳转请求，登录完成后跳转到指定url
            if(StringUtils.isNotBlank(nextUrl))
            {
                return ("redirect:" + nextUrl);
            }

            return "redirect:/";
        }
        catch (Exception e)
        {
            logger.error("登录异常：" + e.getMessage());
            return "login";
        }
    }

    // 登出请求
    @RequestMapping(value = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue(name = "ticket") String ticket)
    {
        userService.logout(ticket);

        return "redirect:/";
    }
}















