package com.czkj.role.service.impl;

import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.role.dao.RoleDao;
import com.czkj.role.service.RoleService;
import com.czkj.utils.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/4/24 12:51
 * @Modified By：
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public PageResult queryRoleList(int page, int size, String roleName, String available) {
        return roleDao.queryRoleList(page, size, roleName, available);
    }

    @Override
    public boolean validateRoleExit(String roleName, String roleCode) {
        if (StringUtils.isNotBlank(roleName)) {
            TabRole tabRole = roleDao.selectKeyOfValue("name", new String(roleName).toUpperCase());
            if (tabRole != null) {
                return false;
            }
        } else if (StringUtils.isNotBlank(roleCode)) {
            TabRole tabRole = roleDao.selectKeyOfValue("code", new String(roleCode).toUpperCase());
            if (tabRole != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(TabRole tabRole, String[] pids) {
        //新增时，角色为可用
        tabRole.setAvailable("1");
        //设置创建时间
        //日期转换-转换格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tabRole.setCreateTime(df.format(new Date()));
        try {
            //执行添加方法，返回主键id
            String id = roleDao.addRole(tabRole);
            //绑定权限-如果赋予的权限
            if (pids != null && pids.length > 0) {
                savePermissionAndRole(id, pids);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePermissionAndRole(String roleId, String[] pids) {
        //如果选择对应权限则绑定对应权限,可选择多个权限
        for (String pid : pids) {
            //绑定角色权限关系
            TabRolePermission tabRolePermission = new TabRolePermission();
            //设置创建时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tabRolePermission.setCreateTime(df.format(new Date()));
            //绑定角色id
            tabRolePermission.setSysRoleId(roleId);
            tabRolePermission.setSysPermissionId(pid);
            try {
                roleDao.addRoleAndPermission(tabRolePermission);
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        return true;
    }

    @Override
    public TabRole queryRoleById(String roleId) {
        TabRole tabRole = roleDao.queryRoleById(roleId);
        return tabRole;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleById(TabRole tabRole, String[] pids) {
        //实体化实体类,用来接收更新的数据
        TabRolePermission tabRolePermissionLater = new TabRolePermission();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tabRole.setLastUpdateTime(df.format(new Date()));

        try {
            //修改角色
            roleDao.updateRoleById(tabRole);

            if (pids != null && pids.length > 0) {
                //获取权限id
                //查询选择的权限与当前角色在关系表中的所有数据，获取主键id
                List<TabRolePermission> tabRolePermissionList = queryByRoleId(tabRole.getId());

                for (String pid : pids) {
                    //查询对应角色的角色权限关系的数据
                    //查询选择的权限与当前角色在关系表中的数据
                    TabRolePermission tabRolePermissionMiddle = roleDao.queryRoleAndPerForRow(tabRole.getId(), pid);
                    String createTime = "";
                    //如果存在数据则用之前的创建日期，反之则为当前日期
                    if (tabRolePermissionMiddle != null) {
                        createTime = tabRolePermissionMiddle.getCreateTime();
                        //设置创建时间
                        tabRolePermissionLater.setCreateTime(createTime);
                    } else {
                        //设置创建时间
                        tabRolePermissionLater.setCreateTime(df.format(new Date()));
                    }
                    //设置最后修改时间
                    tabRolePermissionLater.setLastUpdateTime(df.format(new Date()));
                    //存储角色id
                    tabRolePermissionLater.setSysRoleId(tabRole.getId());
                    //存储权限id
                    tabRolePermissionLater.setSysPermissionId(pid);
                    roleDao.addRoleAndPermission(tabRolePermissionLater);
                }

                if (tabRolePermissionList != null && tabRolePermissionList.size() > 0) {
                    //删除所有此角色所有记录
                    for (TabRolePermission tabRolePermissionBefore : tabRolePermissionList) {
                        roleDao.deleteRoleAndPer(tabRolePermissionBefore.getId());
                    }
                }
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(String roleId) {
        //设置可用标识不可用，标识删除
        String availabe = "0";
        try {
            roleDao.updateRoleAvailable(availabe, roleId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    public List<TabRolePermission> queryByRoleId(String roleId) {
        return roleDao.queryRoleOrPerForList(roleId, null);
    }
}
