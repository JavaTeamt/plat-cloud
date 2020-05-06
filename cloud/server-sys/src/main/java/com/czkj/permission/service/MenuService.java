package com.czkj.permission.service;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabRolePermission;

import java.util.List;

/**
 * @Author: SunMin
 * @Description:菜单管理逻辑处理
 * @Date:Create：in 2020/4/22 11:38
 * @Modified By：
 */
public interface MenuService {

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<TabPermission> queryMenuByParentId(String parentId, String available);


    /**
     * 新增资源
     * @param tabPermission 实体接收参数
     * @return
     */
    boolean savePermission(TabPermission tabPermission);

    /**
     * 根据对应资源标识获取对应资源信息
     * @param id 资源标识（id）
     * @return
     */
    TabPermission getTabPermission(String id);

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
    boolean updateTabPermission(TabPermission tabPermission);

    /**
     * 查询资源及对应角色信息
     * @param pid 资源id
     * @return
     */
    TabRolePermission selectPermissionAndRole(String pid);

    /**
     * 废弃指定资源
     * @param id 标识
     * @return
     */
    boolean deleteTabpermission(String id);

    /**
     * 启用资源
     * @return
     */
    boolean enablePermission(String id);
}
