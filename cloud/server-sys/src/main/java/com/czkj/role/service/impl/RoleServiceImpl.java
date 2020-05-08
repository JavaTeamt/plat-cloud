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
    public boolean validateRoleExit(String roleName, String roleCode,String keyid) {
        if (StringUtils.isNotBlank(roleName)) {
            TabRole tabRole = roleDao.selectKeyOfValue("name", new String(roleName).toUpperCase(),keyid);
            if (tabRole != null) {
                return false;
            }
        } else if (StringUtils.isNotBlank(roleCode)) {
            TabRole tabRole = roleDao.selectKeyOfValue("code", new String(roleCode).toUpperCase(),keyid);
            if (tabRole != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(String name, String code, String[] pids) {

        try {
            //执行添加方法，返回主键id

            String id = roleDao.addRole(name,code);
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
        TabRolePermission tabRolePermission = new TabRolePermission();
        try {
            //修改角色
            roleDao.updateRoleById(tabRole);
            if (pids != null && pids.length > 0) {
                //删除原有数据
                roleDao.deleteRoleAndPer(tabRole.getId());
                for (String pid : pids) {
                    //存储角色id
                    tabRolePermission.setSysRoleId(tabRole.getId());
                    //存储权限id
                    tabRolePermission.setSysPermissionId(pid);
                    tabRolePermission.setLastUpdateTime(new Date());
                    roleDao.addRoleAndPermission(tabRolePermission);
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
        try {
            roleDao.updateRoleAvailable(roleId);
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
