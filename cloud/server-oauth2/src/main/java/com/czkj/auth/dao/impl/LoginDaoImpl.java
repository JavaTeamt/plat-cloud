package com.czkj.auth.dao.impl;

import com.czkj.auth.dao.LoginDao;
import com.czkj.auth.entity.TabSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

/**
 * @author SunMin
 * @description 实现
 * @create 2020/4/15
 * @since 1.0.0
 */
@Repository
public class LoginDaoImpl implements LoginDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public TabSubscriber login(String username) {
        //定义sql
        String sql = " select id,password from tab_subscriber where id=?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, username);

        while (sqlRowSet.next()){
            TabSubscriber tabSubscriber = new TabSubscriber();
            tabSubscriber.setId(sqlRowSet.getString("id"));
            tabSubscriber.setPassword(sqlRowSet.getString("password"));
            return tabSubscriber;
        }
        return null;
    }

    @Override
    public void updateLoginStatus(String username) {
        String sql = "update tab_subscriber set loginstatus=1 where id=?";
        jdbcTemplate.update(sql,username);
    }
}
