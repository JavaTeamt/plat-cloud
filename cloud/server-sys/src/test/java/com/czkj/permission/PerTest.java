package com.czkj.permission;

import com.czkj.common.entity.TabPermission;
import com.czkj.permission.dao.MenuDao;
import com.czkj.permission.service.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/4/28 14:46
 * @Modified By：
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PerTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    /**
     * 新增资源
     */
    @Test
    public void test1(){
        TabPermission tabPermission = new TabPermission();
        tabPermission.setName("test");
        tabPermission.setType("button");
        tabPermission.setParentId("5");
        tabPermission.setPerCode("role:add");
        boolean result = menuService.savePermission(tabPermission);
        System.out.println("结果=="+result);
    }

    /**
     * 根据对应资源标识获取对应资源信息
     */
    @Test
    public void test2(){

        TabPermission tabPermission = menuService.getTabPermission("1");
        System.out.println("数据=="+tabPermission);
    }

    /**
     * 修改对应信息
     */
    @Test
    public void test3(){
        TabPermission tabPermission1 = menuService.getTabPermission("1");
        tabPermission1.setParentId("1");
        boolean b = menuService.updateTabPermission(tabPermission1);
        System.out.println("结果=="+b);
    }


}
