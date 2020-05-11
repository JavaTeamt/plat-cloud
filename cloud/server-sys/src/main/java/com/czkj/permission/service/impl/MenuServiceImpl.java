package com.czkj.permission.service.impl;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabPermissionUrl;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.common.entity.TabSubscriber;
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
    public List<TabPermission> getAllList(String available) {
        return menuDao.queryAllList(available);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePermission(TabPermission tabPermission) {
        List<TabPermissionUrl> list = new ArrayList();
        try {
            //新增权限，返回主键id
            String key = menuDao.savePermission(tabPermission.getName(),tabPermission.getRemark());
            //获取url数据，添加权限对应url
            if (tabPermission.getUrlList()!= null && tabPermission.getUrlList().size()> 0) {
                //数组去重
                for (int i = 0; i < tabPermission.getUrlList().size(); i++) {
                    if (!list.contains(tabPermission.getUrlList().get(i))){
                        list.add(tabPermission.getUrlList().get(i));
                    }
                }
                for (TabPermissionUrl tabPermissionUrl : list) {
                    menuDao.savePerUrl(tabPermissionUrl.getName(), key, tabPermissionUrl.getRemark(),null);
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
    public TabPermission getPermissionByKey(String key) {
        return menuDao.queryPermission(key);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePerByKey(TabPermission tabPermission) {
        List<TabPermissionUrl> list = new ArrayList();
        try {
            //修改权限数据
            menuDao.updatePermission(tabPermission.getName(),tabPermission.getRemark(),tabPermission.getId());
            //先删除原先url记录,重新添加
            menuDao.deletePerUrlByPerId(tabPermission.getId());
            //获取url数据，添加权限对应url
            if (tabPermission.getUrlList()!= null && tabPermission.getUrlList().size()> 0) {
                //数组去重
                for (int i = 0; i < tabPermission.getUrlList().size(); i++) {
                    if (!list.contains(tabPermission.getUrlList().get(i))) {
                        list.add(tabPermission.getUrlList().get(i));
                    }
                }
                for (TabPermissionUrl tabPermissionUrl : list) {
                    menuDao.savePerUrl(tabPermissionUrl.getName(), tabPermission.getId(),tabPermissionUrl.getRemark(), new Date());
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
    public boolean queryExit(String name, String url,String keyId) {
        if (StringUtils.isNotBlank(name)) {
            TabPermission tabPermission = menuDao.queryPerByName(name,keyId);
            if (tabPermission != null) {
                return false;
            }
        }
        if (StringUtils.isNotBlank(url)) {
            TabPermissionUrl tabPermissionUrl = menuDao.queryPerUrlByUrl(url);
            if (tabPermissionUrl != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTabpermission(String key) {
        //不进行真正的删除，修改标识为0表示废弃
        try {
            menuDao.updatePermissionForAvlia("0", key);
            menuDao.updatePerUrlAvailable("0", key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enablePermission(String key) {
        try {
            menuDao.updatePermissionForAvlia("1", key);
            menuDao.updatePerUrlAvailable("1", key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    public TabPermission getPermissionAndRole(String pid) {
        return menuDao.queryPermissionAndRole(pid);
    }
}
