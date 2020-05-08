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
     * 查询所有权限信息
     *
     * @param available 可用标识
     * @return
     */
    List<TabPermission> getAllList(String available);

    /**
     * 新增权限
     *
     * @param name 权限名
     * @param urls url数组，一个权限可以新增多条url
     * @return
     */
    boolean savePermission(String name, String[] urls);

    /**
     * 根据主键获取权限信息
     *
     * @param key 主键
     */
    TabPermission getPermissionByKey(String key);

    /**
     * 修改权限数据
     *
     * @param name 权限名
     * @param key  权限主键
     * @param urls url数组
     */
    boolean updatePerByKey(String name, String key, String[] urls);

    /**
     * 检验信息是否存在
     * @param name 权限名
     * @param url 权限url
     * @param keyId 主键id-用于修改校验标识
     * @return
     */
    boolean queryExit(String name,String url,String keyId);

    /**
     * 废弃指定资源
     * @param key 主键id
     * @return
     */
    boolean deleteTabpermission(String key);

    /**
     * 启用资源
     * @param key 主键id
     * @return
     */
    boolean enablePermission(String key);

    /**
     * 查询资源及对应角色信息
     * @param pid 资源id
     * @return
     */
    TabPermission getPermissionAndRole(String pid);

}
