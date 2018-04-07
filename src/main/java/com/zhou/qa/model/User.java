package com.zhou.qa.model;

/**
 * Created by zhouxinyu1cp on 2018/4/7.
 */
public class User
{
    private String name;
    private String description;

    public User() {}

    public User(String name) {
        this.name = name;
        this.description = "this is a description";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
