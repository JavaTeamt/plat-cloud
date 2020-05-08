package com.czkj.role.dao.impl;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabPermissionUrl;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.permission.dao.MenuDao;
import com.czkj.role.dao.RoleDao;
import com.czkj.utils.PageResult;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/4/24 9:39
 * @Modified By：
 */
@Repository
public class RoleDaoImpl implements RoleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MenuDao menuDao;

    @Override
    public PageResult<TabRole> queryRoleList(int page, int size, String roleName, String available) {

        //初始化
        Integer totalCount = null;

        //初始化
        List<TabRole> roleList = new ArrayList<>();

        //查询总条数
        String sqlForCount = "select count(1) from tab_role where 1=1 ";

        //查询全部角色信息
        String sql = "select id,code,name from tab_role where 1=1 ";

        //如果满足条件就加条件查询
        if (StringUtils.isNotBlank(roleName) && StringUtils.isNotBlank(available)) {
            sql += "and available=? and name like concat('%',?,'%') limit ?,?";
            sqlForCount += "and available=? and name like concat('%',?,'%') ";
            totalCount = jdbcTemplate.queryForObject(sqlForCount, Integer.class, available, roleName);
            roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), available, roleName, (page - 1) * size, size);
        } else if (StringUtils.isNotBlank(roleName)) {
            sql += "and name like ? limit ?,?";
            sqlForCount += "and name=? ";
            totalCount = jdbcTemplate.queryForObject(sqlForCount, Integer.class, roleName);
            roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), roleName, (page - 1) * size, size);
        } else if (StringUtils.isNotBlank(available)) {
            sql += "and available=? limit ?,?";
            sqlForCount += "and available=? ";
            totalCount = jdbcTemplate.queryForObject(sqlForCount, Integer.class, available);
            roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), available, (page - 1) * size, size);
        } else {
            sql += "limit ?,?";
            totalCount = jdbcTemplate.queryForObject(sqlForCount, Integer.class);
            roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), (page - 1) * size, size);
        }

        //遍历角色信息，通过角色id获取对应权限信息存入角色实体类封装资源实体（一对多）
        for (TabRole tabRole : roleList) {
            for (TabPermission permission : getPermissionListByRole(tabRole.getId())) {
                tabRole.getTabPermissions().add(permission);
            }
        }
        return new PageResult<>(page, size, totalCount, roleList);
    }

    /**
     * 根据角色id获取对应权限信息
     *
     * @param rid 角色id
     * @return
     */
    private List<TabPermission> getPermissionListByRole(String rid) {
        String permissionSql = "select p.id,p.name from tab_permission p RIGHT JOIN  tab_role_permission rp on p.id=rp.sys_permission_id left JOIN  tab_role r on r.id=rp.sys_role_id where r.id=?";
        List<TabPermission> permissions = jdbcTemplate.query(permissionSql, new BeanPropertyRowMapper<>(TabPermission.class), rid);
        if (permissions.size() > 0) {
            for (int i = 0; i < permissions.size(); i++) {
                List<TabPermissionUrl> permissionUrls = menuDao.queryPerUrlList(permissions.get(i).getId());
                permissions.get(i).setUrlList(permissionUrls);
            }
        }
        return permissions;
    }

    @Override
    public String addRole(String name, String code) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        //通过jdbctemplate返回主键
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into tab_role(code,name,available,create_time) values(?,?,?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, new String(code).toUpperCase());
                ps.setString(2, new String(name).toUpperCase());
                ps.setString(3, "1");
                ps.setTimestamp(4, timestamp);
                return ps;
            }
        }, keyHolder);
        //获取主键id进行返回
        String keyid = keyHolder.getKey().toString();
        return keyid;
    }

    @Override
    public void addRoleAndPermission(TabRolePermission tabRolePermission) {
        String sql = "insert into tab_role_permission(sys_role_id,sys_permission_id,create_time,last_update_time) values(?,?,?,?)";
        jdbcTemplate.update(sql, tabRolePermission.getSysRoleId(),
                tabRolePermission.getSysPermissionId(),
                new Date(),
                tabRolePermission.getLastUpdateTime());
    }

    @Override
    public TabRole queryRoleById(String roleId) {
        TabRole tabRole = selectKeyOfValue("id", roleId, null);
        if (tabRole != null) {
            //获取对应角色
            List<TabPermission> permissionListByRole = getPermissionListByRole(tabRole.getId());
            for (TabPermission tabPermission : permissionListByRole) {
                tabRole.getTabPermissions().add(tabPermission);
            }
            return tabRole;
        }
        return null;
    }

    @Override
    public void updateRoleById(TabRole tabRole) {
        String sql = "update tab_role set code=?,name=?,last_update_time=? where id=?";
        jdbcTemplate.update(sql, tabRole.getCode(),
                tabRole.getName(),
                new Date(),
                tabRole.getId());
    }

    @Override
    public List<TabRolePermission> queryRoleOrPerForList(String roleId, String perId) {
        List<TabRolePermission> tabRolePermissionList = new ArrayList<>();
        String sql = "select id from tab_role_permission where 1=1 ";
        if (StringUtils.isNotBlank(roleId)) {
            sql += "and sys_role_id=? ";
            tabRolePermissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRolePermission.class), roleId);
        } else if (StringUtils.isNotBlank(perId)) {
            sql += "and sys_permission_id=?";
            tabRolePermissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRolePermission.class), perId);
        } else {
            tabRolePermissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRolePermission.class));
        }
        return tabRolePermissionList;
    }

    @Override
    public void deleteRoleAndPer(String roleId) {
        jdbcTemplate.update("delete from tab_role_permission where sys_role_id=?", roleId);
    }

    @Override
    public void updateRoleAvailable(String roleId) {
        jdbcTemplate.update("update tab_role set available=? where id=?", roleId);
    }

    @Override
    public TabRole selectKeyOfValue(String key, String value, String keyid) {
        SqlRowSet sqlRowSet = null;
        if (StringUtils.isNotBlank(keyid)) {
            String sql = "select id,code,name from tab_role where " + key + "=? and id<>?";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, value, keyid);
        } else {
            String sql = "select id,code,name from tab_role where " + key + "=?";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, value);
        }
        while (sqlRowSet.next()) {
            TabRole tabRole = new TabRole();
            tabRole.setId(sqlRowSet.getString("id"));
            tabRole.setCode(sqlRowSet.getString("code"));
            tabRole.setName(sqlRowSet.getString("name"));
            return tabRole;
        }
        return null;
    }
}
