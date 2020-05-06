package com.czkj.permission.dao;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabRolePermission;

import java.util.List;

/**
 * @Author: SunMin
 * @Description:菜单管理
 * @Date:Create：in 2020/4/22 11:22
 * @Modified By：
 */
public interface MenuDao {
    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param available 是否可用
     */
    List<TabPermission> queryMenuByParentId(String parentId, String available);

//    /**
//     * 查询所有资源信息
//     * @return
//     */
//    List<TabPermission> queryList();

    /**
     * 添加资源
     * @param tabPermission
     * @return
     */
    void addPermission(TabPermission tabPermission);

    /**
     * 根据对应id查询资源信息
     * @param id 资源id
     * @return
     */
    TabPermission selectTabPermissionById(String id);

    /**
     * 查询所有菜单（不包含view和button）
     * @param parentId 父级id
     * @return
     */
    List<TabPermission> queryAllMenuNotButton(String parentId);

    /**
     * 修改资源列表
     * @param tabPermission 实体接收参数
     * @return
     */
    void updateTabPermission(TabPermission tabPermission);

    /**
     * 查询资源及对应角色信息
     * @param pid 资源id
     * @return
     */
    TabRolePermission selectPermissionAndRole(String pid);

    /**
     * 废弃资源
     * @param id 资源id
     * @param available 是否可用
     * @param parentId 父级id
     */
    void updatePermissionForAvlia(String available, String id, String parentId);


}
