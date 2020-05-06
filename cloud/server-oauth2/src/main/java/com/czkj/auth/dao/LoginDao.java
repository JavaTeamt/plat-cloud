package com.czkj.auth.dao;


import com.czkj.auth.entity.TabSubscriber;

public interface LoginDao {
    /**
     * 用户登录
     *
     * @return
     */
    TabSubscriber login(String username);

    /**
     * 修改对应用户登录状态
     * @param username
     */
    void updateLoginStatus(String username);
}
