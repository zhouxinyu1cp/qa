package com.zhou.qa.model;

import java.util.Date;

/**
 * Created by zhouxinyu1cp on 2018/4/22.
 */

// LoginTicket对象，对应数据库表 login_ticket 中的每条数据
public class LoginTicket
{
    private int id;
    private int userId;
    private String ticket;
    private Date expired;
    private int status;  // status = 1，表示该条 LoginTicket 失效；status = 0，表示有效

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
