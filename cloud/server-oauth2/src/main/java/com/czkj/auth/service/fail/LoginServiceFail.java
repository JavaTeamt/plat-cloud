package com.czkj.auth.service.fail;

import com.czkj.auth.entity.TabSubscriber;
import com.czkj.auth.service.LoginService;
import com.czkj.res.Response;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangqh
 * @date 2020-03-18
 */

@Component
public class LoginServiceFail implements LoginService {
    @Override
  public Response<TabSubscriber> showUserAndRoleById(@RequestParam("id")String userName) {
        return Response.failure("网络繁忙，请稍后再试-_-。PS：服务消费者自己提供的信息");
    }
}
