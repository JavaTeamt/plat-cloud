package com.czkj.role;

import com.czkj.common.entity.TabRole;
import com.czkj.role.dao.RoleDao;
import com.czkj.role.service.RoleService;
import com.czkj.utils.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/4/28 14:48
 * @Modified By：
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RoleTest {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleService roleService;

    /**
     * 所有角色信息
     */
    @Test
    public void test1(){
        PageResult<TabRole> tabRolePageResult = roleDao.queryRoleList(1, 2,null, "1");
        List<TabRole> items = tabRolePageResult.getItems();
        for (TabRole item : items) {
            System.out.println("数据=="+item);
        }
        System.out.println("当前页="+tabRolePageResult.getCurrentPage());
        System.out.println("总条数="+tabRolePageResult.getTotalCount());
        System.out.println("每页显示条数=="+tabRolePageResult.getSize());
        System.out.println("总页数=="+tabRolePageResult.getTotalPage());
    }

    /**
     * 添加角色
     */
    @Test
    public void test2(){
        TabRole tabRole = new TabRole();
        tabRole.setCode("ddddd");
        tabRole.setName("test权限");
        String pids[] = new String[]{"1","1161220339896324097"};
        boolean result = roleService.saveRole(tabRole, pids);
        System.out.println("结果=="+result);
    }

    /**
     * 获取对应角色信息数据以及对应权限
     */
    @Test
    public void test3(){
        TabRole tabRole = roleService.queryRoleById("1");
        System.out.println("角色信息数据="+tabRole);
    }

    /**
     * 修改角色
     */
    @Test
    public void test4(){
        TabRole tabRole = new TabRole();
        tabRole.setName("SALE");
        tabRole.setCode("客户经理权限");
        tabRole.setId("1");
        String[] pids = new String[]{"1"};
        boolean result = roleService.updateRoleById(tabRole, pids);
        System.out.println("结果=="+result);
    }

}
