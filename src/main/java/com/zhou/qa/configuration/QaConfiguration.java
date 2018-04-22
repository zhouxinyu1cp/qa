package com.zhou.qa.configuration;

import com.zhou.qa.interceptor.LoginRequiredInterceptor;
import com.zhou.qa.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by zhouxinyu1cp on 2018/4/22.
 */

// 继承自 SpringMVC 的自定义配置

@Component
public class QaConfiguration extends WebMvcConfigurerAdapter
{
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    // 重写 addInterceptors()方法，把自定义的拦截器注册到Spring框架中
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器执行有顺序之分，先注册的拦截器先调用，后注册的拦截器后调用
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*"); // 拦截指定路径请求的 http请求
        super.addInterceptors(registry);
    }
}








