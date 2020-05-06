package com.czkj.user;


import com.czkj.common.entity.vo.CustomerAndUser;
import com.czkj.user.dao.UserDao;
import com.czkj.common.entity.TabCustomer;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabSubscriber;
import com.czkj.common.entity.TabUserRole;
import com.czkj.user.service.UserService;
import com.czkj.utils.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;


    /**
     * 用户注册
     */
    @Test
    public void test() throws ParseException {
        TabSubscriber tabSubscriber = new TabSubscriber();
        tabSubscriber.setId("1964160321");
        tabSubscriber.setMobile("15580046120");
        tabSubscriber.setPassword("666");
        boolean b = userService.userRegister(tabSubscriber);
        System.out.println("注册结果=="+b);
    }

    /**
     * 获取用户信息
     */
    @Test
    public void test2(){
        TabSubscriber userById = userService.getUserById("1964160321");

        System.out.println("用户信息=="+userById);
    }

    /**
     * 修改密码
     */
    @Test
    public void test3(){
        boolean b = userService.forgetPassoword("666", "1973849951");
        System.out.println("结果=="+b);
    }

    /**
     * 更换手机号
     */
    @Test
    public void test4(){
        boolean b = userService.updateUserPhone("15387391581", "1973849951");
        System.out.println("结果=="+b);
    }

    /**
     * 检验账号手机号是否存在
     */
    @Test
    public void test5(){
        boolean b = userService.vUserExits(null, "15580046120");
        System.out.println("结果=="+b);
    }

    /**
     * 检验手机号是否正确/显示手机号
     */
    @Test
    public void test6(){
        String s = userService.vPhoneExits("1973849951");
        System.out.println("结果=="+s);
    }

    /**
     * 身份认证
     */
    @Test
    public void test7(){
        TabCustomer tabCustomer = new TabCustomer();
        tabCustomer.setName("孙威");
        tabCustomer.setSex("1");
        tabCustomer.setCertType("身份证");
        tabCustomer.setCertid("1305221997103058954");
        boolean authentication = userService.Authentication(tabCustomer, "1964160321");
        System.out.println("结果=="+authentication);
    }

    /**
     * 显示用户客户信息-分页
     */
    @Test
    public void test8(){
        PageResult pageResult = userDao.seletAllUser(1, 3);
        List<TabSubscriber> list = pageResult.getItems();
        for (TabSubscriber tabSubscriber : list) {
            System.out.println("数据=="+tabSubscriber);
        }

        System.out.println("当前页="+pageResult.getCurrentPage());
        System.out.println("总条数="+pageResult.getTotalCount());
        System.out.println("每页显示条数=="+pageResult.getSize());
        System.out.println("总页数=="+pageResult.getTotalPage());

    }

    /**
     * 查询指定用户客户信息
     */
    @Test
    public void test9(){
        TabSubscriber tabSubscriber = userService.getAllUserByUid("1964160300");
        System.out.println("数据="+tabSubscriber);
    }

    /**
     * 修改用户信息
     */
    @Test
    public void test10(){
        CustomerAndUser customerAndUser = new CustomerAndUser();
        TabSubscriber subscriber = new TabSubscriber();
        subscriber.setId("1964160347");
        subscriber.setPassword("1212");
        subscriber.setMobile("15387391518");
//        TabCustomer tabCustomer = new TabCustomer();
//        tabCustomer.setMobile("15580046122");
//        tabCustomer.setName("孙凯");
//        tabCustomer.setSex("1");
//        tabCustomer.setCertType("身份证");
//        tabCustomer.setCertid("130522199610305895");
//        customerAndUser.setTabCustomer(tabCustomer);
//        customerAndUser.setTabSubscriber(subscriber);
        String[] roleIds = new String[]{"1128560889039405057","1087631246069563394"};
        boolean b = userService.updateUserAndRole(subscriber,roleIds);
        System.out.println("结果="+b);
    }

    /**
     * 添加用户客户信息
     */
    @Test
    public void test11(){
//        CustomerAndUser customerAndUser = new CustomerAndUser();
        TabSubscriber tabSubscriber = new TabSubscriber();
//        TabCustomer customer = new TabCustomer();
        tabSubscriber.setId("1964160347");
        tabSubscriber.setPassword("123");
        tabSubscriber.setMobile("13873984611");
//        customer.setName("孙凯");
//        customer.setSex("男");
//        customer.setCertType("身份证");
//        customer.setCertid("430522199610305895");
//        customerAndUser.setTabSubscriber(tabSubscriber);
//        customerAndUser.setTabCustomer(customer);
        String[] roleIds = new String[]{"1"};
        boolean b = userService.addUserAndRole(tabSubscriber,roleIds);
        System.out.println("结果=="+b);
    }

    /**
     * 删除用户
     */
    @Test
    public void test12(){
        String id = "1556845315";
        boolean b = userService.deleteUserAndRole(id);
        System.out.println("结果=="+b);
    }

    @Test
    public void test000() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =df.parse(df.format(new Date()));
        System.out.println("日期格式=="+date);
    }
}

