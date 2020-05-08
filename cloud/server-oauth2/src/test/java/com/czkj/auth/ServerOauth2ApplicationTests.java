package com.czkj.auth;

import com.czkj.auth.entity.TabSubscriber;
import com.czkj.auth.service.LoginService;
import com.czkj.res.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ServerOauth2ApplicationTests {

    @Resource
   private LoginService loginService;

    @Test
    public void contextLoads() {
        Response<TabSubscriber> response = loginService.showUserAndRoleById("1973849951");
        TabSubscriber data = response.getData();
        System.out.println("角色："+data.getTabRoleList());
    }

}
