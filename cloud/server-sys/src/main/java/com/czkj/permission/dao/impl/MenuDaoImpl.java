package com.czkj.permission.dao.impl;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabPermissionUrl;
import com.czkj.permission.dao.MenuDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SunMin
 * @Description:实现
 * @Date:Create：in 2020/4/22 11:27
 * @Modified By：
 */
@Repository
@Slf4j
public class MenuDaoImpl implements MenuDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

   private SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<TabPermission> queryAllList(String available) {
        List<TabPermission> permissionList = new ArrayList<>();
        String sql = "select id,name,remark from tab_permission where 1=1 ";

        if (StringUtils.isNotBlank(available)) {
            sql += "available = ?";
            permissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class), available);
        } else {
            permissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class));
        }
        //遍历过去URL信息
        if (permissionList.size() > 0) {
            for (int i = 0; i < permissionList.size(); i++) {
                //根据权限id获取对应URL
                List<TabPermissionUrl> tabPermissionUrls = queryAllUrlList(permissionList.get(i).getId());
                permissionList.get(i).setUrlList(tabPermissionUrls);
            }
        }

        return permissionList;
    }

    @Override
    public List<TabPermissionUrl> queryAllUrlList(String perId) {
        String sql = "select name,remark from tab_permission_url where per_id =?";
        List<TabPermissionUrl> urlList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermissionUrl.class), perId);
        return urlList;
    }

    @Override
    public String savePermission(String name, String remark) {
        log.info("url类型="+name.getClass().toString()+",描述信息类型="+remark.getClass().toString());
        //获取当前时间
        Timestamp timestamp = new Timestamp(new Date().getTime());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "insert into tab_permission(name,available,remark,create_time) values(?,?,?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, "1");
                ps.setString(3, remark);
                ps.setTimestamp(4, timestamp);
                return ps;
            }
        }, keyHolder);
        String key = keyHolder.getKey().toString();
        return key;
    }

    @Override
    public void savePerUrl(String url, String perId, String remark, Date lastUpdateTime) {
        String sql = "insert into tab_permission_url(name,per_id,available,remark,create_time,last_update_time) values(?,?,?,?,?,?)";
        System.out.println("创建时间为：" + new Date() + ",最后修改日期为：" + lastUpdateTime);
        jdbcTemplate.update(sql, url, perId,"1",remark, new Date(), lastUpdateTime);
    }

    @Override
    public TabPermission queryPermission(String key) {
        String sql = "select id,name,remark from tab_permission where id=?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, key);
        while (sqlRowSet.next()) {
            TabPermission tabPermission = new TabPermission();
            tabPermission.setId(sqlRowSet.getString("id"));
            tabPermission.setName(sqlRowSet.getString("name"));
            tabPermission.setRemark(sqlRowSet.getString("remark"));
            List<TabPermissionUrl> permissionUrls = queryAllUrlList(tabPermission.getId());
            if (permissionUrls.size() > 0) {
                tabPermission.setUrlList(permissionUrls);
            }
            return tabPermission;
        }
        return null;
    }

    @Override
    public List<TabPermissionUrl> queryPerUrlList(String perId) {
        String sql = "select name from tab_permission_url where per_id=?";
        List<TabPermissionUrl> permissionUrls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermissionUrl.class), perId);
        return permissionUrls;
    }

    @Override
    public void updatePermission(String name, String remark, String key) {
        String sql = "update tab_permission set name=?,remark = ?,last_update_time=? where id =?";
        jdbcTemplate.update(sql, name, remark, new Date(), key);
    }

    @Override
    public void deletePerUrlByPerId(String perId) {
        String sql = "delete from tab_permission_url where per_id=?";
        jdbcTemplate.update(sql, perId);
    }

    @Override
    public TabPermission queryPerByName(String name, String keyId) {
        SqlRowSet sqlRowSet = null;
        if (StringUtils.isNotBlank(keyId)) {
            String sql = "select name from tab_permission where name=? and id<>?";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, name, keyId);
        } else {
            String sql = "select name from tab_permission where name=?";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, name);
        }
        while (sqlRowSet.next()) {
            TabPermission tabPermission = new TabPermission();
            tabPermission.setName(sqlRowSet.getString("name"));
            return tabPermission;
        }
        return null;
    }

    @Override
    public TabPermissionUrl queryPerUrlByUrl(String url) {
        String sql = "select name from tab_permission_url where name=?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, url);
        while (sqlRowSet.next()) {
            TabPermissionUrl tabPermissionUrl = new TabPermissionUrl();
            tabPermissionUrl.setName(url);
        }
        return null;
    }

    @Override
    public void updatePermissionForAvlia(String available, String id) {
        String sql = "update tab_permission set available=? where id=? ";
        jdbcTemplate.update(sql, available, id);
    }

    @Override
    public void updatePerUrlAvailable(String available, String perId) {
        String sql = "update tab_permission_url set available=? where per_id=? ";
        jdbcTemplate.update(sql, available, perId);
    }

    @Override
    public TabPermission queryPermissionAndRole(String pid) {
        String sql = "select p.name from tab_permission p RIGHT JOIN tab_role_permission rp on p.id=rp.sys_permission_id LEFT JOIN tab_role r on r.id=sys_role_id where p.id=?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, pid);
        while (sqlRowSet.next()) {
            TabPermission tabRolePermission = new TabPermission();
            tabRolePermission.setName(sqlRowSet.getString("name"));
            return tabRolePermission;
        }
        return null;
    }
}
