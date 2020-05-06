package com.czkj.permission.service.impl;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.permission.dao.MenuDao;
import com.czkj.permission.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Author: SunMin
 * @Description:实现
 * @Date:Create：in 2020/4/22 11:39
 * @Modified By：
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<TabPermission> queryMenuByParentId(String parentId, String available) {
        //给予一个标识，选择对应执行方法
        boolean notButton = false;
        return getMenuList(parentId, notButton, available);
    }

    /**
     * 递归获取菜单列表
     *
     * @param parentId  父级id
     * @param notButton 是否需要查询按钮的标识
     * @return
     */
    private List<TabPermission> getMenuList(String parentId, boolean notButton, String available) {

        List<TabPermission> tabPermissionList = new ArrayList<>();
        if (notButton) {
            tabPermissionList = menuDao.queryAllMenuNotButton(parentId);
        } else {
            tabPermissionList = menuDao.queryMenuByParentId(parentId, available);
        }
        if (tabPermissionList.size() > 0) {
            for (int i = 0; i < tabPermissionList.size(); i++) {
                parentId = tabPermissionList.get(i).getId();
                List<TabPermission> dtos = getMenuList(parentId, notButton, available);
                tabPermissionList.get(i).setChildMenu(dtos);
            }
        }
        return tabPermissionList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePermission(TabPermission tabPermission) {
        //设置创建时间
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tabPermission.setCreateTime(df.format(new Date()));
        //默认为可用
        tabPermission.setAvailable("1");
        if (StringUtils.isBlank(tabPermission.getSortString())){
            tabPermission.setSortString("0");
        }
        try {
            menuDao.addPermission(tabPermission);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public TabPermission getTabPermission(String id) {
        return menuDao.selectTabPermissionById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TabPermission> queryAllMenuNotButton(String parentId) {
        //给予一个标识，选择对应执行方法
        boolean notButton = true;
        return getMenuList(parentId,notButton,"");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTabPermission(TabPermission tabPermission) {
            try {
                //手动设置默认属性
                //设置日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                tabPermission.setLastUpdateTime(df.format(new Date()));

                menuDao.updateTabPermission(tabPermission);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public TabRolePermission selectPermissionAndRole(String pid) {
        return menuDao.selectPermissionAndRole(pid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTabpermission(String id) {
        //不进行真正的删除，修改标识为0表示废弃
        try {
            menuDao.updatePermissionForAvlia("0", id, null);
            //废弃之后子级也废弃
            menuDao.updatePermissionForAvlia("0", null, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enablePermission(String id) {
        try {
            menuDao.updatePermissionForAvlia("1", id, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }
}
