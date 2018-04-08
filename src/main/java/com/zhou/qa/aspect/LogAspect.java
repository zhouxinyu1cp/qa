package com.zhou.qa.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by zhouxinyu1cp on 2018/4/8.
 */

// 演示 AOP，面向切面的编程
@Aspect
@Component
public class LogAspect
{
    public static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    // 切 IndexController 中的所有方法，在 IndexController 中所有方法执行前，
    // 先执行 before()
    @Before("execution(* com.zhou.qa.controller.IndexController.*(..))")
    public void before(JoinPoint jp)
    {
        logger.info("before method: " + jp.toLongString());
    }

    // 切 controller 包中所有以 Controller 结尾的类的方法，在他们所有方法执行后，
    // 再执行 after()
    @After("execution(* com.zhou.qa.controller.*Controller.*(..))")
    public void after()
    {
        logger.info("after method:" + new Date());
    }
}




