package com.czkj.permission.dao.impl;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.permission.dao.MenuDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SunMin
 * @Description:实现
 * @Date:Create：in 2020/4/22 11:27
 * @Modified By：
 */
@Repository
public class MenuDaoImpl implements MenuDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<TabPermission> queryMenuByParentId(String parentId, String available) {
        String sql = "select id,name,type,url,per_code,clazz,available,sort_string from tab_permission where parent_id ="+parentId+" ";
        //获取上级名称
        String sqlForParentName = "select name from tab_permission where id = ? ";
        List<TabPermission> tabPermissionList = new ArrayList<>();
        if (StringUtils.isNotBlank(available)) {
            sql += "and available=? ";
            tabPermissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class), available);
        }
        tabPermissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class));
        //获取上级名称
        if (!parentId.equals("0")) {
        if (tabPermissionList.size() > 0) {
            for (int i = 0; i < tabPermissionList.size(); i++) {
                    SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlForParentName, parentId);
                    while (sqlRowSet.next()) {
                        tabPermissionList.get(i).setParentName(sqlRowSet.getString("name"));
                    }
                }
            }
        }
        return tabPermissionList;
    }

//    @Override
//    public List<TabPermission> queryList() {
//        String sql = "select * from tab_permission";
//
//        List<TabPermission> tabPermissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class));
//
//        return tabPermissionList;
//    }

    @Override
    public void addPermission(TabPermission tabPermission) {
        String sql = "insert into tab_permission(name," +
                "type," +
                "url," +
                "per_code," +
                "clazz," +
                "parent_id," +
                "sort_string," +
                "create_time) values(?,?,?,?,?,?,?,?) ";

        jdbcTemplate.update(sql,
                tabPermission.getName(),
                tabPermission.getType(),
                tabPermission.getUrl(),
                tabPermission.getPerCode(),
                tabPermission.getClazz(),
                tabPermission.getParentId(),
                tabPermission.getSortString(),
                tabPermission.getCreateTime());
    }

    @Override
    public TabPermission selectTabPermissionById(String id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select id,name,type,url,per_code,clazz,parent_id,sort_string from tab_permission where id=?", id);
        while (sqlRowSet.next()) {
            TabPermission tabPermission = new TabPermission();
            tabPermission.setId(sqlRowSet.getString("id"));
            tabPermission.setName(sqlRowSet.getString("name"));
            tabPermission.setType(sqlRowSet.getString("type"));
            tabPermission.setUrl(sqlRowSet.getString("url"));
            tabPermission.setPerCode(sqlRowSet.getString("per_code"));
            tabPermission.setClazz(sqlRowSet.getString("clazz"));
            tabPermission.setParentId(sqlRowSet.getString("parent_id"));
            tabPermission.setSortString(sqlRowSet.getString("sort_string"));
            return tabPermission;
        }
        return null;
    }

    @Override
    public List<TabPermission> queryAllMenuNotButton(String parentId) {
        String sql = "select name from tab_permission where type<>'1' and available='1' and parent_id="+parentId;
        List<TabPermission> menuList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class));
        return menuList;
    }

    @Override
    public void updateTabPermission(TabPermission tabPermission) {
        String sql = "";
        //如果是菜单
        if (tabPermission.getType().equals("1")) {
            sql = "update tab_permission set name=?," +
                    "type=?," +
                    "url=?," +
                    "per_code=?," +
                    "clazz=?," +
                    "parent_id=?," +
                    "sort_string=?," +
                    "last_update_time=? " +
                    "where id=?";
            jdbcTemplate.update(sql, tabPermission.getName(),
                    tabPermission.getType(),
                    tabPermission.getUrl(),
                    tabPermission.getPerCode(),
                    tabPermission.getClazz(),
                    tabPermission.getParentId(),
                    tabPermission.getSortString(),
                    tabPermission.getLastUpdateTime(),
                    tabPermission.getId());
        } else if (tabPermission.getType().equals("2")) {
            sql = "update tab_permission set name=?,type=?,per_code=?,parent_id=?,last_update_time=? where id=?";
            jdbcTemplate.update(sql, tabPermission.getName(),
                    tabPermission.getType(),
                    tabPermission.getPerCode(),
                    tabPermission.getParentId(),
                    tabPermission.getLastUpdateTime(),
                    tabPermission.getId());
        }

    }

    @Override
    public TabRolePermission selectPermissionAndRole(String pid) {
        String sql = "select sys_role_id from  tab_role_permission where sys_permission_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, pid);
        while (sqlRowSet.next()) {
            TabRolePermission tabRolePermission = new TabRolePermission();
            tabRolePermission.setSysRoleId(sqlRowSet.getString("sys_role_id"));
            return tabRolePermission;
        }
        return null;
    }

    @Override
    public void updatePermissionForAvlia(String available, String id, String parentId) {
        String sql = "update tab_permission set available=? where 1=1 ";
        if (StringUtils.isNotBlank(id)) {
            sql += "and id = ?";
            jdbcTemplate.update(sql, available, id);
        } else {
            sql += "and parent_id=?";
            jdbcTemplate.update(sql, available, parentId);
        }
    }
}
