package com.czkj.auth.service;

import com.czkj.auth.entity.TabSubscriber;
import com.czkj.auth.service.fail.LoginServiceFail;
import com.czkj.res.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/5/8 11:06
 * @Modified By：
 */
@FeignClient(value = "server-sys",fallback = LoginServiceFail.class)
public interface LoginService {
    //获取用户对应角色
    @GetMapping(value = "/user/showUserAndRoleById")
    Response<TabSubscriber> showUserAndRoleById(@RequestParam("id") String userName);
}
