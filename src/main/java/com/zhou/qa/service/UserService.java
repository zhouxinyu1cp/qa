package com.zhou.qa.service;

import com.zhou.qa.dao.LoginTicketDao;
import com.zhou.qa.dao.UserDao;
import com.zhou.qa.model.LoginTicket;
import com.zhou.qa.model.User;
import com.zhou.qa.util.QaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created by zhouxinyu1cp on 2018/4/16.
 */

@Service
public class UserService
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    public User getUserById(int id)
    {
        return userDao.selectById(id);
    }

    // 检查用户名和密码
    private boolean checkUsernameAndPasswd(String username, String password,
                                           Map<String, Object> msg_map)
    {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password))
        {
            msg_map.put("msg", "用户名或密码不能为空");
            return false;
        }

        return true;
    }

    // 添加一条 token 到数据库表 login_ticket 中
    private String addLoginTicket(int userId)
    {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId); // 该 token 关联的 user_id
        Date date = new Date();
        date.setTime(3600 * 24 * 100 + date.getTime());
        ticket.setExpired(date); // token 有效期
        ticket.setStatus(0);  // token 状态，0有效，1无效
        ticket.setTicket(UUID.randomUUID().toString().replace("-", "")); // 生成的随机 token

        loginTicketDao.addLoginTicket(ticket);

        return ticket.getTicket();
    }

    // 处理用户注册
    public Map<String, Object> register(String username, String password)
    {
        Map<String, Object> map = new HashMap<>();

        if(!checkUsernameAndPasswd(username, password, map))
        {
            return map;
        }

        User user = userDao.selectByName(username);
        if(user != null)
        {
            map.put("msg", "该用户名已经存在");
            return map;
        }

        // 数据库中添加一条 user 记录
        User u = new User(username);
        u.setSalt(UUID.randomUUID().toString().replace("-", "").substring(0, 10));  // 一个随机独特的字符串
        u.setPassword(QaUtil.MD5(password + u.getSalt()));  // salt 用于加密密码
        u.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));

        userDao.addUser(u);

        // 注册的时候，数据库中添加一条登录的 token 记录，设置有效期和有效状态
        String ticket = addLoginTicket(u.getId());
        map.put("ticket", ticket);

        return map;
    }

    // 处理用户登录
    public Map<String, Object> login(String username, String password)
    {
        Map<String, Object> map = new HashMap<>();

        if(!checkUsernameAndPasswd(username, password, map))
        {
            return map;
        }

        User user = userDao.selectByName(username);
        if(user == null)
        {
            map.put("msg", "该用户不存在");
            return map;
        }

        if(!user.getPassword().equals(QaUtil.MD5(password + user.getSalt())))
        {
            map.put("msg", "该用户密码不正确");
            return map;
        }

        // 数据库中添加一条登录的 token 记录，设置有效期和有效状态
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;
    }

    public void logout(String ticket)
    {
        // 登出时，把数据库中该 token 对应的记录删除即可
//        loginTicketDao.deleteByTicket(ticket);

        // 登出时，把数据库中该 token 对应的记录状态置为无效，
        // 一般不删除，历史 token 对应的都是用户的登录记录
        loginTicketDao.updateByTicket(ticket, 1);
    }
}









