package com.zhou.qa.controller;

import com.zhou.qa.service.QaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by zhouxinyu1cp on 2018/4/8.
 */

@Controller
public class SettingController
{
    // 申明依赖的对象，运行时框架自动注入该对象，无须初始化，可直接使用
    @Autowired
    private QaService qaService;

    @RequestMapping(path = {"/setting"}, method = {RequestMethod.GET})
    @ResponseBody
    public String setting()
    {
        return "Setting ok, " + qaService.getMsg(100);
    }
}
