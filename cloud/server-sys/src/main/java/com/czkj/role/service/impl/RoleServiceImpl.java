package com.czkj.role.service.impl;

import com.czkj.common.entity.TabPermission;
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
    public boolean saveRole(TabRole tabRole) {

        try {
            //执行添加方法，返回主键id
            String keyId = roleDao.addRole(tabRole.getName(),tabRole.getCode());
            //将返回的主键放进实体类存储
            tabRole.setId(keyId);
            //绑定权限-如果赋予的权限
            if (tabRole.getTabPermissions() != null && tabRole.getTabPermissions().size() > 0) {
                savePermissionAndRole(tabRole);
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
    public boolean savePermissionAndRole(TabRole tabRole) {
        //如果选择对应权限则绑定对应权限,可选择多个权限
        for (TabPermission tabPermission : tabRole.getTabPermissions()) {
            //绑定角色权限关系
            TabRolePermission tabRolePermission = new TabRolePermission();
            //绑定角色id
            tabRolePermission.setSysRoleId(tabRole.getId());
            tabRolePermission.setSysPermissionId(tabPermission.getId());
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
    public boolean updateRoleById(TabRole tabRole) {
        //实体化实体类,用来接收更新的数据
        TabRolePermission tabRolePermission = new TabRolePermission();
        try {
            //修改角色
            roleDao.updateRoleById(tabRole);
            //删除原有关系表中指定角色id记录
            roleDao.deleteRoleAndPer(tabRole.getId());
            if (tabRole.getTabPermissions() != null && tabRole.getTabPermissions().size() > 0) {
                for (TabPermission tabPermission: tabRole.getTabPermissions()) {
                    //存储角色id
                    tabRolePermission.setSysRoleId(tabRole.getId());
                    //存储权限id
                    tabRolePermission.setSysPermissionId(tabPermission.getId());
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
