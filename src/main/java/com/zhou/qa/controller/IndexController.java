package com.zhou.qa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by zhouxinyu1cp on 2018/4/4.
 */

@Controller
public class IndexController
{
    public IndexController() {}

    @RequestMapping(path={"/", "/index"})
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
}








