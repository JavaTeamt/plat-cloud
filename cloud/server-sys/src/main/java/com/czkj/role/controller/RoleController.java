package com.czkj.role.controller;

import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.exception.ExceptionHandleAdvice;
import com.czkj.res.Response;
import com.czkj.role.service.RoleService;
import com.czkj.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/4/24 12:52
 * @Modified By：
 */
@Api(description = "角色管理生产接口")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    private static final Logger log = LoggerFactory.getLogger(RoleController.class);

    @ApiOperation(value = "显示角色列表（分页显示）", notes = "显示角色列表（分页显示）")
    @ApiImplicitParams({@ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleName", value = " 角色名-用于模糊查询", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "available", value = " 是否可用标识", paramType = "query", required = false, dataType = "String")})
    @GetMapping("/getRoleList")
    public PageResult getRoleList(int currentPage, int size, String roleName, String available) {
        return roleService.queryRoleList(currentPage, size, roleName, available);
    }

    /**
     * 检验角色信息是否存在
     *
     * @param name 角色名
     * @param code 角色编码
     * @return
     */
    private Response validateRoleExit(String name, String code, String keyid) {
        log.info("角色名:" + name + "," + "角色编码:" + code);
        if (StringUtils.isNotBlank(name)) {
            boolean result = roleService.validateRoleExit(name, null, keyid);
            if (!result) {
                return Response.failure("4015", "角色名已存在");
            }
        }
        if (StringUtils.isNotBlank(code)) {
            boolean result = roleService.validateRoleExit(null, code, keyid);
            if (!result) {
                return Response.failure("4016", "角色编码已存在");
            }
        }
        return Response.success();
    }

    @ApiOperation(value = "保存角色信息", notes = "保存角色信息")
    @RequestMapping(value = "/saveRole",produces = "application/json",method = RequestMethod.POST)
    public Response saveRole(@RequestBody TabRole tabRole) {
        log.info("角色名:" + tabRole.getName(), "," + "角色编码:" + tabRole.getCode());
        log.info("权限id:" + tabRole.getTabPermissions());
        Response response = validateRoleExit(tabRole.getName(), tabRole.getCode(), null);
        //验证成功则进行添加
        if (response.getCode().equals("0")) {
            log.info("检验结果:" + response.getCode());
            boolean result = roleService.saveRole(tabRole);
            if (result) {
                return Response.success().message("保存成功");
            }
            return Response.failure("保存失败");
        }
        return response;
    }

    @ApiOperation(value = "获取对应角色信息", notes = "获取对应角色信息")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "query", dataType = "String")
    @GetMapping("/getRoleById")
    public Response getRoleById(@RequestParam("id") String roleId) {
        TabRole tabRole = roleService.queryRoleById(roleId);
        return Response.success(tabRole);
    }

    @ApiOperation(value = "修改指定角色信息", notes = "修改指定角色信息")
    @RequestMapping(value = "/updateRole",produces = "application/json",method = RequestMethod.PUT)
    public Response updateRole(@RequestBody TabRole tabRole) {
        System.out.println("角色id："+tabRole.getId());
        Response response = validateRoleExit(tabRole.getName(), tabRole.getCode(),tabRole.getId());
        //验证成功则进行添加
        if (response.getCode().equals("0")) {
            log.info("权限id:" + tabRole.getTabPermissions());
            boolean result = roleService.updateRoleById(tabRole);
            if (result) {
                return Response.success().message("修改成功");
            }
            return Response.failure("修改失败");
        }
        return response;
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(name = "roleId", value = "角色id，删除", paramType = "query", required = true, dataType = "String[]")
    @DeleteMapping("/deleteRole")
    public Response deleteRole(String roleId) {
        List<TabRolePermission> tabRolePermissionList = roleService.queryByRoleId(roleId);
        if (tabRolePermissionList.size() > 0) {
            return Response.failure("4014", "不能删除角色+" + roleId + "，有权限-_-");
        }
        boolean result = roleService.deleteRole(roleId);
        if (result) {
            return Response.success().message("删除成功");
        }
        return Response.failure("删除失败");

    }
}
