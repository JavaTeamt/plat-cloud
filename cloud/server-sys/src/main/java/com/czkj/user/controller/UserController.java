package com.czkj.user.controller;


import com.czkj.common.entity.TabRole;
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

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @RequestMapping(value = "/userRegister",produces = "application/json",method = RequestMethod.POST)
    public Response UserRegister(@RequestBody TabSubscriber tabSubscriber /*, String smsCode*/) {
        Response response = vUserExit(tabSubscriber.getId(), tabSubscriber.getMobile(), null);
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
    private Response vUserExit(String id, String mobile, String keyId) {
        //先进行表单的一些校验，校验成功进行用户注册

        if (StringUtils.isNotBlank(id)) {
            boolean result = userService.vUserExits(id, null, keyId);
            if (result) {
                //用户名已存在
                return Response.failure("5003", "用户名已注册");
            }
        }
        if (StringUtils.isNotBlank(mobile)) {
            boolean result = userService.vUserExits(null, mobile, keyId);
            if (result) {
                //电话已存在
                return Response.failure("5004", "电话已注册");
            }
        }
        return Response.success();
    }

    @ApiOperation(value = "显示用户注册时的手机号", notes = "显示用户注册时的手机号")
    @ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String")
    @GetMapping("/vPhoneExit")
    public Response vPhoneExit(String userName) {
        String mobile = userService.vPhoneExits(userName);
        if (StringUtils.isNotBlank(mobile)) {
            return Response.success(mobile);
        }
        return Response.failure("5014", "未绑定手机号");
    }

    @ApiOperation(value = "修改用户手机号", notes = "修改用户手机号")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "新手机号", paramType = "query", required = true, dataType = "String")})
    @PutMapping("/updatePhone")
    public Response updatePhone(String phone, String userName) {
        Response response = vUserExit(userName, phone,userName);
        if (response.getCode().equals("0")) {
            boolean b = userService.updateUserPhone(phone, userName);
            if (b) {
                return Response.success().message("手机号修改成功");
            }
            return Response.failure("手机号更换失败");
        }
        return response;
    }

    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "要修改的密码", paramType = "query", required = true, dataType = "String")})
    @PutMapping("/forgetPassword")
    public Response forgetPassword(String password, String userName) {
        log.info("修改密码为：" + password);
        boolean result = userService.forgetPassoword(password, userName);
        if (result) {
            return Response.success().message("密码修改成功");
        }
        return Response.failure("修改失败");
    }

    @ApiOperation(value = "头像上传", notes = "头像上传")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "file", value = "文件（头像）", required = true, dataType = "MultipartFile")})
    @PostMapping("/uploadFile")
    public Response uploadProfile(MultipartFile file,String userName) {
        //获取当前用户的登录账号
        boolean result = userService.updateHeadImg(file, userName);
        if (result) {
            //上传成功，获取头像路径，显示
            String headImg = userService.getUserById(userName).getHeadImg();
            return Response.success(headImg).message("上传成功");
        }
        return Response.failure("上传头像失败");
    }

    @ApiOperation(value = "根据登录账号获取对应用户信息", notes = "根据登录账号获取对应用户信息")
    @ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String")
    @GetMapping("/getUserById")
    public Response getUserById(String userName) {
        log.info("用户名:" + userName);
        TabSubscriber user = userService.getUserById(userName);
        log.info("用户数据:" + user);
        return Response.success(user);
    }

    @ApiOperation(value = "身份认证", notes = "身份认证")
    @ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String")
    @RequestMapping(value = "/authentication",produces = "application/json",method = RequestMethod.POST)
    public Response Authentication(@RequestBody TabCustomer tabCustomer,String userName) {
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
    @ApiImplicitParams({@ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", paramType = "query", required = true, dataType = "int")})
    @GetMapping("/showUserList")
    public PageResult showUserList(int currentPage, int size) {
        if (currentPage > 0 && size > 0) {
            return userService.getUserList(currentPage, size);
        }
        return null;
    }

    @ApiOperation(value = "修改用户数据", notes = "修改用户数据")
    @RequestMapping(value = "/updateUserAndRole",produces = "application/json",method = RequestMethod.PUT)
    public Response updateUserAndRole(@RequestBody TabSubscriber tabSubscriber) {
        log.info("用户数据==" + tabSubscriber + ",角色id=" + tabSubscriber.getTabRoleList());
        Response response = vUserExit(tabSubscriber.getId(), tabSubscriber.getMobile(), tabSubscriber.getId());
        if (response.getCode().equals("0")) {
            boolean result = userService.updateUserAndRole(tabSubscriber);
            if (result) {
                return Response.success().message("修改成功");
            }
            return Response.failure().message("修改失败");
        }
        return response;
    }

    @ApiOperation(value = "新增用户数据", notes = "新增用户数据")
    @RequestMapping(value = "/addUserAndRole",produces = "application/json",method = RequestMethod.POST)
    public Response addUserAndRole(@RequestBody TabSubscriber tabSubscriber) {
        Response response = vUserExit(tabSubscriber.getId(), tabSubscriber.getMobile(), null);
        if (response.getCode().equals("0")) {
            boolean b = userService.addUserAndRole(tabSubscriber);
            if (b) {
                return Response.success().message("新增成功");
            }
            return Response.failure("新增失败");
        }
        return response;
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParam(name = "userid", value = "登录账号", paramType = "query", required = true, dataType = "String")
    @DeleteMapping("/deleteUserAndRole")
    public Response deleteUser(String userid) {
        log.info("登录账号：" + userid);
        //查询用户是否绑定角色
        List<TabRole> roleListByUId = userService.getRoleListByUId(userid);
        if (roleListByUId.size() > 0) {
            return Response.failure("4018", "抱歉不能删除用户,有角色绑定");
        } else {
            boolean b = userService.deleteUser(userid);
            if (b) {
                return Response.success().message("删除成功");
            }
        }
        return Response.failure("删除失败");
    }

}
