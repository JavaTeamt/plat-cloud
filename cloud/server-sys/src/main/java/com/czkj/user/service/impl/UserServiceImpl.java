package com.czkj.user.service.impl;

import com.czkj.common.SystemConstant;
import com.czkj.common.entity.vo.CustomerAndUser;
import com.czkj.user.dao.UserDao;
import com.czkj.user.service.UserService;
import com.czkj.utils.IdWorker;
import com.czkj.utils.MD5Util;
import com.czkj.utils.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import com.czkj.common.entity.TabCustomer;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabSubscriber;
import com.czkj.common.entity.TabUserRole;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author SunMin
 * @description 用户实现
 * @create 2020/4/9
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean userRegister(TabSubscriber user) {

        //手动设置一些非用户填入的属性
        //创建时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setCreateTime(df.format(new Date()));
        //刚注册用户属于未登录状态
        user.setLoginStatus("0");
        //用户进行加密
        String password = MD5Util.md5Encode(user.getPassword());
        user.setPassword(password);

        //根据系统选择存放路径
        String OSName = System.getProperty("os.name");
        String profilesPath = OSName.toLowerCase().startsWith("win") ? SystemConstant.WINDOWS_PROFILES_PATH
                : SystemConstant.LINUX_PROFILES_PATH;
        String defaultPath = profilesPath + "/timg.jpg";
        //用户默认头像
        user.setHeadImg(defaultPath);
        //添加,按照顺序传入参数，登录账号，手机号，密码，登录状态，头像，创建时间

        try {
            userDao.addUserJ(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    public boolean vUserExits(String id, String mobile) {
        TabSubscriber oneUser = null;
        if (StringUtils.isNotBlank(id)) {
            //验证用户名是否存在
            oneUser = userDao.selectUserByKey("id", id);
        } else {
            //电话校验-判断是否存在
            oneUser = userDao.selectUserByKey("mobile", mobile);
        }
        if (oneUser != null) {
            return true;
        }
        return false;
    }

    @Override
    public String vPhoneExits(String id) {
        String mobile = userDao.selectUserByKey("id", id).getMobile();
        return mobile;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean forgetPassoword(String password, String id) {
        //存储加密后的密码
        password = MD5Util.md5Encode(password);
        try {
            userDao.updateUserPassword(password, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserPhone(String phone, String id) {
        int result = userDao.updateUserJ(id, phone, null);
        if (result > 0) {
            String custid = userDao.selectUserByKey("id", id).getCustid();
            if (StringUtils.isNotBlank(custid)) {
                try {
                    userDao.updateCustomerMobile(phone, custid);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        }
        return false;
    }

    @Override
    public TabSubscriber getUserById(String id) {
        TabSubscriber user = userDao.selectUserByKey("id", id);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateHeadImg(MultipartFile headImg, String id){
        // 根据Windows和Linux配置不同的头像保存路径
        String OSName = System.getProperty("os.name");
        String profilesPath = OSName.toLowerCase().startsWith("win") ? SystemConstant.WINDOWS_PROFILES_PATH
                : SystemConstant.LINUX_PROFILES_PATH;

        if (!headImg.isEmpty()) {

            String newProfileName = profilesPath + UUID.randomUUID().toString() + headImg.getOriginalFilename();

            try {
            // 磁盘保存
            BufferedOutputStream out = null;

            File folder = new File(profilesPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            out = new BufferedOutputStream(new FileOutputStream(newProfileName));
            // 写入新文件
            out.write(headImg.getBytes());
            out.flush();
            out.close();
            // 路径存库
                userDao.updateUserJ(id, null, newProfileName);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean Authentication(TabCustomer tabCustomer, String id) {
        try {
            //手动设置默认值
            //设置身份认证标识，0-未认证，1-已认证
            tabCustomer.setCustidentify("1");
            //设置创建时间为当前时间 //设置日期格式
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tabCustomer.setCreatetime(df.format(new Date()));

            //获取手机号，如果没有手机号绑定当前用户的手机号
            if (StringUtils.isBlank(tabCustomer.getMobile())) {
                //查询当前用户信息获取手机号
                String mobile = userDao.selectUserByKey("id", id).getMobile();
                tabCustomer.setMobile(mobile);
            }
            //根据身份证编码获取客户数据，判断是否存在数据，存在则不重复添加相同客户，直接用户绑定此客户,反之添加在绑定
            TabCustomer tabCustomerForCustId = userDao.selectCustomerByCertid(tabCustomer.getCertid());

            String custid = "";
            if (tabCustomerForCustId != null) {
                custid = tabCustomerForCustId.getId();
            }
            if (StringUtils.isNotBlank(custid)) {
                //与当前用户进行绑定
                userDao.updateUcustid(custid, id);
                //为保证身份信息除身份证号一致其他不一致特殊情况执行修改方法
                //不能接收到客户id
                tabCustomer.setId(custid);
//                    userDao.updateCustomer(tabCustomer);
            } else {
                //执行添加返回客户id
                String cid = userDao.addCustomer(tabCustomer);
                userDao.updateUcustid(cid, id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    public PageResult getUserList(int currentPage, int size) {
        PageResult pageResult = userDao.seletAllUser(currentPage, size);
        return pageResult;
    }

    @Override
    public List<TabRole> getRoleListByUId(String userId) {
        return userDao.queryRoleList(userId);
    }

    @Override
    public TabCustomer getCustomerByUid(String id) {
        return userDao.selectCustomerByUid(id);
    }

    @Override
    public TabSubscriber getAllUserByUid(String id) {
        TabSubscriber tabSubscriber = userDao.selectAllUserByUid(id);
        return tabSubscriber;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserAndRole(TabSubscriber tabSubscriber, String[] roleIds) {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //修改用户信息
            MD5Util.md5Encode(tabSubscriber.getPassword());
            //密码加密
            tabSubscriber.setPassword(MD5Util.md5Encode(tabSubscriber.getPassword()));
            userDao.updateUser(tabSubscriber);
//                //在用户存在的情况下判断是否存在客户
//                if (customerAndUser.getTabCustomer() != null) {
//                    //调用实名认证的方法
//                    Authentication(customerAndUser.getTabCustomer(), customerAndUser.getTabSubscriber().getId());
//                }
            if (roleIds != null && roleIds.length > 0) {
                IdWorker idWorker = new IdWorker();
                //查询当前用户在关系表中是否存在记录或者说是否绑定角色,如果存在删除原来记录，新增记录
                List<TabUserRole> userRoles = queryListByUserId(tabSubscriber.getId());
                for (String roleId : roleIds) {
                    //根据当前的用户id和角色id查询是否存在记录，存在记录则取出创建时间,存入新纪录中
                    TabUserRole tabUserRoleByQ = userDao.queryRow(tabSubscriber.getId(), roleId);
                    //创建用户角色关系实体类，用于存储要添加的数据
                    TabUserRole tabUserRoleByAdd = new TabUserRole();
                    //设置主键
                    tabUserRoleByAdd.setId(idWorker.nextId() + "");
                    //设置最后修改时间
                    tabUserRoleByAdd.setLastUpdateTime(df.format(new Date()));
                    //设置用户id
                    tabUserRoleByAdd.setSysUserId(tabSubscriber.getId());
                    //存入角色id
                    tabUserRoleByAdd.setSysRoleId(roleId);

                    if (tabUserRoleByQ != null) {
                        String createTime = tabUserRoleByQ.getCreateTime();
                        tabUserRoleByAdd.setCreateTime(createTime);
                    } else {
                        tabUserRoleByAdd.setCreateTime(df.format(new Date()));
                    }
                    userDao.saveUserAndRole(tabUserRoleByAdd);
                }
                if (userRoles.size() > 0) {
                    for (TabUserRole userRole : userRoles) {
                        userDao.deleteUserAndRole(userRole.getId());
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
    public boolean addUserAndRole(TabSubscriber tabSubscriber, String[] roleIds) {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //手动设置默认登录状态为未登录
        tabSubscriber.setLoginStatus("0");
        //手动设置创建时间为当前时间
        tabSubscriber.setCreateTime(df.format(new Date()));
        //获取密码，进行加密
        String password = MD5Util.md5Encode(tabSubscriber.getPassword());
        tabSubscriber.setPassword(password);
        //根据系统选择存放路径
        String OSName = System.getProperty("os.name");
        String profilesPath = OSName.toLowerCase().startsWith("win") ? SystemConstant.WINDOWS_PROFILES_PATH
                : SystemConstant.LINUX_PROFILES_PATH;
        String defaultPath = profilesPath + "/timg.jpg";
        //设置默认头像
        tabSubscriber.setHeadImg(defaultPath);
        try {
            userDao.addUserJ(tabSubscriber);
//                //如果需要添加客户信息则执行添加客户方法
//                if (customerAndUser.getTabCustomer() != null) {
////                    //设置默认标识，为已认证
////                    customerAndUser.getTabCustomer().setCustidentify("1");
////                    //设置默认创建时间为当前时间
////                    customerAndUser.getTabCustomer().setCreatetime(new Date(System.currentTimeMillis()));
////                    //获取用户的手机号作为客户手机号
////                    customerAndUser.getTabCustomer().setMobile(customerAndUser.getTabCustomer().getMobile());
////                    //查询客户信息判断输入的是否存在，已存在则不进行重复添加，直接绑定id
////                    TabCustomer tabCustomer = userDao.selectCustomerByCertid(customerAndUser.getTabCustomer().getCertid());
////                    if (tabCustomer != null) {
////                        userDao.updateUcustid(tabCustomer.getId(), customerAndUser.getTabSubscriber().getId());
////                    } else {
////                        //添加客户信息
////                        long custid = userDao.addCustomer(customerAndUser.getTabCustomer());
////                        ////获取用户的手机号作为客户手机号进行绑定一致
////                        userDao.updateCustomerMobile(customerAndUser.getTabSubscriber().getMobile(), custid);
////                        //客户绑定用户
////                        userDao.updateUcustid(custid, customerAndUser.getTabSubscriber().getId());
////                    }
//                    //调用实名认证的方法
//                    Authentication(customerAndUser.getTabCustomer(), customerAndUser.getTabSubscriber().getId());
//                }
            /**
             * 如果用户选择了对应角色，就添加用户角色关系绑定
             */
            if (roleIds != null && roleIds.length > 0) {
                for (String roleId : roleIds) {
                    TabUserRole tabUserRole = new TabUserRole();
                    //手动设置关系表主键
                    IdWorker idWorker = new IdWorker();
                    tabUserRole.setId(idWorker.nextId() + "");
                    //绑定用户角色
                    tabUserRole.setSysUserId(tabSubscriber.getId());
                    tabUserRole.setSysRoleId(roleId);
                    //设置创建时间
                    tabUserRole.setCreateTime(df.format(new Date()));
                    userDao.saveUserAndRole(tabUserRole);
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
    public boolean deleteUserAndRole(String userid) {
        try {
            userDao.deleteUser(userid);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    public List<TabUserRole> queryListByUserId(String userId) {
        return userDao.queryRoleList(userId);
    }
}
