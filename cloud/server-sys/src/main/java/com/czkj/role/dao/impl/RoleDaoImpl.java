package com.czkj.role.dao.impl;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabRolePermission;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
        String permissionSql = "select p.id,p.name,p.type,p.url from tab_permission p RIGHT JOIN  tab_role_permission rp on p.id=rp.sys_permission_id left JOIN  tab_role r on r.id=rp.sys_role_id where r.id=?";
        List<TabPermission> permissions = jdbcTemplate.query(permissionSql, new BeanPropertyRowMapper<>(TabPermission.class), rid);
        return permissions;
    }

    @Override
    public String addRole(TabRole tabRole) {
        //通过jdbctemplate返回主键
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into tab_role(code,name,available,create_time) values(?,?,?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, new String(tabRole.getCode()).toUpperCase());
                ps.setString(2, new String(tabRole.getName()).toUpperCase());
                ps.setString(3, tabRole.getAvailable());
                ps.setString(4, tabRole.getCreateTime());
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
                tabRolePermission.getCreateTime(),
                tabRolePermission.getLastUpdateTime());
    }

    @Override
    public TabRole queryRoleById(String roleId) {
        TabRole tabRole = selectKeyOfValue("id", roleId);
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
                tabRole.getLastUpdateTime(),
                tabRole.getId());
    }

    @Override
    public TabRolePermission queryRoleAndPerForRow(String roleId, String perId) {
        String sql = "select create_time from tab_role_permission where 1=1 and sys_role_id=? and sys_permission_id=?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, roleId, perId);
        while (sqlRowSet.next()) {
            TabRolePermission tabRolePermission = new TabRolePermission();
            tabRolePermission.setCreateTime(sqlRowSet.getString("create_time"));
            return tabRolePermission;
        }
        return null;
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
    public void deleteRoleAndPer(String keyId) {
        jdbcTemplate.update("delete from tab_role_permission where id=?", keyId);
    }

    @Override
    public void updateRoleAvailable(String available, String roleId) {
        jdbcTemplate.update("update tab_role set available=? where id=?", available, roleId);
    }

    @Override
    public TabRole selectKeyOfValue(String key, String value) {
        String sql = "select id,code,name,create_time from tab_role where " + key + "=?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, value);
        while (sqlRowSet.next()) {
            TabRole tabRole = new TabRole();
            tabRole.setId(sqlRowSet.getString("id"));
            tabRole.setCode(sqlRowSet.getString("code"));
            tabRole.setName(sqlRowSet.getString("name"));
            tabRole.setCreateTime(sqlRowSet.getString("create_time"));
            return tabRole;
        }
        return null;
    }
}
