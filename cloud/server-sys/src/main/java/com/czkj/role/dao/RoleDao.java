package com.czkj.role.dao;

import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.utils.PageResult;

import java.util.List;

/**
 * @Author: SunMin
 * @Description:角色管理
 * @Date:Create：in 2020/4/24 9:34
 * @Modified By：
 */
public interface RoleDao {
    /**
     * 查询角色列表-分页
     *
     * @param page      当前页
     * @param size      每页显示条数
     * @param roleName  模糊查询-角色名
     * @param available 是否可用
     * @return
     */
    PageResult<TabRole> queryRoleList(int page, int size, String roleName, String available);

    /**
     * 添加角色信息
     *
     * @param tabRole 实体类接收参数
     */
    String addRole(TabRole tabRole);

    /**
     * 添加角色及对应权限关系
     *
     * @param tabRolePermission
     */
    void addRoleAndPermission(TabRolePermission tabRolePermission);

    /**
     * 查询对应角色信息
     *
     * @param roleId 角色id
     * @return
     */
    TabRole queryRoleById(String roleId);

    /**
     * 修改角色信息
     *
     * @param tabRole 实体类接收参数
     */
    void updateRoleById(TabRole tabRole);


    /**
     * 根据角色id和对应权限id获取关系表数据
     *
     * @param roleId 角色id
     * @param perId  权限id
     * @return
     */
    TabRolePermission queryRoleAndPerForRow(String roleId, String perId);

    /**
     * 根据角色id或者对应权限id获取关系表数据
     *
     * @param roleId 角色id
     * @param perId  权限id
     * @return
     */
    List<TabRolePermission> queryRoleOrPerForList(String roleId, String perId);

    /**
     * 删除角色权限关系表指定主键数据
     *
     * @param keyId 主键id
     */
    void deleteRoleAndPer(String keyId);

    /**
     * 修改指定角色可用标识
     * @param available 可用标识
     * @param roleId 角色id
     */
    void updateRoleAvailable(String available, String roleId);

    /**
     * 根据指定字段和值查询数据
     * @param key 指定字段
     * @param value 值
     * @return
     */
    TabRole selectKeyOfValue(String key, String value);

}
