package com.czkj.user.controller;


import com.czkj.res.Response;

import com.czkj.common.entity.TabCustomer;
import com.czkj.common.entity.TabSubscriber;
import com.czkj.common.entity.TabUserRole;
import com.czkj.user.service.UserService;
import com.czkj.utils.Base;
import com.czkj.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author SunMin
 * @description 用户web层控制
 * @create 2020/4/9
 * @since 1.0.0
 */
@Api(description = "用户功能生产api")
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Resource
    private HttpServletRequest request;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @ApiImplicitParam(name = "tabSubscriber", value = "用户实体，用于接收用户一些基本属性的值", required = true, dataType = "Object")
    @PostMapping("/userRegister")
    public Response UserRegister(TabSubscriber tabSubscriber /*, String smsCode*/) {
        Response response = vUserExit(tabSubscriber.getId(), tabSubscriber.getMobile());
        if ("0".equals(response.getCode())) {
            boolean result = userService.userRegister(tabSubscriber);
            if (result) {
                return Response.success().message("注册成功");
            }
            return Response.failure("注册失败");
        }
        return response;
    }

    /**
     * 校验手机号，登录账号是否存在
     *
     * @param id     登录账号
     * @param mobile 手机号
     * @return
     */
    private Response vUserExit(String id, String mobile) {
        //先进行表单的一些校验，校验成功进行用户注册
        boolean result = userService.vUserExits(id, mobile);
        if (StringUtils.isNotBlank(id)) {
            if (result) {
                //用户名已存在
                return Response.failure("5003", "用户名已存在");
            }
            return Response.success().message("用户名通过");
        }
        if (StringUtils.isNotBlank(mobile)) {
            if (result) {
                //电话已存在
                return Response.failure("5004", "电话已存在");
            }
            return Response.success().message("电话通过");
        }
        return Response.failure("5013", "不能为空");
    }

    @ApiOperation(value = "显示用户注册时的手机号", notes = "显示用户注册时的手机号")
    @GetMapping("/vPhoneExit")
    public Response vPhoneExit() {
        //获取登录账号
        String userName = Base.getUserName(request);
        String mobile = userService.vPhoneExits(userName);
        if (StringUtils.isNotBlank(mobile)) {
            return Response.success(mobile);
        }
        return Response.failure("5014", "未绑定手机号");
    }

    @ApiOperation(value = "修改用户手机号", notes = "修改用户手机号")
    @ApiImplicitParam(name = "phone", value = "新手机号", required = true, dataType = "String")
    @PutMapping("/updatePhone")
    public Response updatePhone(String phone) {
        //获取登录账号
        String userName = Base.getUserName(request);
        boolean b = userService.updateUserPhone(phone, userName);
        if (b) {
            return Response.success().message("手机号修改成功");
        }
        return Response.failure("手机号更换失败");
    }

    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @ApiImplicitParam(name = "password", value = "要修改的密码", required = true, dataType = "String")
    @PutMapping("/forgetPassword")
    public Response forgetPassword(String password) {
        //获取当前用户登录账号
        String userName = Base.getUserName(request);
        boolean result = userService.forgetPassoword(password, userName);
        if (result) {
            return Response.success().message("密码修改成功");
        }
        return Response.failure("修改失败");
    }

    @ApiOperation(value = "头像上传", notes = "头像上传")
    @ApiImplicitParam(name = "file", value = "文件（头像）", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadFile")
    public Response uploadProfile(MultipartFile file) {
        //获取当前用户的登录账号
        String userName = Base.getUserName(request);
        boolean result = userService.updateHeadImg(file, userName);
        if (result) {
            //上传成功，获取头像路径，显示
            String headImg = userService.getUserById(userName).getHeadImg();
            return Response.success(headImg).message("上传成功");
        }
        return Response.failure("上传头像失败");
    }

    @ApiOperation(value = "根据登录账号获取对应用户信息", notes = "根据登录账号获取对应用户信息")
    @GetMapping("/getUserById")
    public Response getUserById() {
        //获取当前用户登录账号
        String userName = Base.getUserName(request);
        log.info("用户名:" + userName);
        TabSubscriber user = userService.getUserById(userName);
        log.info("用户数据:" + user);
        return Response.success(user);
    }

    @ApiOperation(value = "身份认证", notes = "身份认证")
    @ApiImplicitParam(name = "tabCustomer", value = "实体类接收参数", required = true, dataType = "Object")
    @PostMapping("/authentication")
    public Response Authentication(TabCustomer tabCustomer) {
        String userName = Base.getUserName(request);
        String custid = userService.getUserById(userName).getCustid();
        //判断未实名状态下才可实名
        if (StringUtils.isBlank(custid)) {
            boolean result = userService.Authentication(tabCustomer, userName);
            if (result) {
                return Response.success().message("认证成功");
            }
        }
        return Response.failure("认证失败");

    }

    @ApiOperation(value = "显示用户列表（分页）", notes = "显示用户列表（分页）")
    @ApiImplicitParams({@ApiImplicitParam(name = "currentPage", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int")})
    @GetMapping("/showUserList")
    public PageResult showUserList(int currentPage, int size) {
        return userService.getUserList(currentPage, size);
    }

    @ApiOperation(value = "显示身份信息,判断是否实名，实名显示身份信息，未实名则提供实名操作", notes = "显示身份信息,判断是否实名，实名显示身份信息，未实名则提供实名操作")
    @GetMapping("/showCert")
    public Response showCert() {
        String userName = "1964160321"/*Base.getUserName(request)*/;
        TabCustomer customerByUid = userService.getCustomerByUid(userName);
        return Response.success(customerByUid);

    }

    @ApiOperation(value = "显示对应用户及客户相关信息,用于编辑用户前显示", notes = "显示对应用户及客户相关信息,用于编辑用户前显示")
    @GetMapping("/showUserAndRoleById")
    public Response showUserAndRoleById() {
        String userName = Base.getUserName(request);
        TabSubscriber tabSubscriber = userService.getAllUserByUid(userName);
        return Response.success(tabSubscriber);
    }

    @ApiOperation(value = "显示用户列表", notes = "显示用户客户基本信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "tabSubscriber", value = "用户实体，用于接收用户一些基本属性的值", required = true, dataType = "Object"),
            @ApiImplicitParam(name = "roleIds", value = "角色id，可接收多个角色", required = true, dataType = "String[]")
    })
    @PutMapping("/updateUserAndRole")
    public Response updateUserAndRole(TabSubscriber tabSubscriber, String[] roleIds) {
        boolean result = userService.updateUserAndRole(tabSubscriber, roleIds);
        if (result) {
            return Response.success().message("修改成功");
        }
        return Response.failure().message("修改失败");
    }

    @ApiOperation(value = "显示用户列表", notes = "显示用户客户基本信息")
    @ApiImplicitParam(name = "roleIds", value = "角色id，可接收多个角色", required = false, dataType = "String[]")
    @PostMapping("/addUserAndRole")
    public Response addUserAndRole(TabSubscriber tabSubscriber, String[] roleIds) {
        boolean result = userService.vUserExits(tabSubscriber.getId(), tabSubscriber.getMobile());
        if (!result) {
            boolean b = userService.addUserAndRole(tabSubscriber, roleIds);
            if (b) {
                return Response.success().message("新增成功");
            }
        }
        return Response.failure("新增失败");
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParam(name = "ids", value = "用户id数组", required = true, dataType = "String[]")
    @DeleteMapping("/deleteUserAndRole")
    public Response deleteUserAndRole(String[] ids) {
        log.info("用户id数组：" + ids);
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                //查询用户是否绑定角色
                List<TabUserRole> tabUserRoles = userService.queryListByUserId(id);
                if (tabUserRoles.size() <= 0) {
                    boolean b = userService.deleteUserAndRole(id);
                    if (b) {
                        return Response.success().message("删除成功");
                    }
                } else {
                    return Response.failure("4018", "抱歉不能删除用户" + id + "，有角色绑定,没有角色绑定用户已为您删除");
                }
            }
        }
        return Response.failure("删除失败");
    }

}
