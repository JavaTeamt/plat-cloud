package com.czkj.permission.controller;


import com.czkj.common.SystemConstant;
import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.permission.service.MenuService;
import com.czkj.res.Response;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SunMin
 * @Description:菜单管理web层
 * @Date:Create：in 2020/4/22 11:49
 * @Modified By：
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "显示所有菜单列表-树状结构", notes = "显示所有菜单列表-树状结构")
    @ApiImplicitParams({@ApiImplicitParam(name = "parentId", value = "父级id", paramType ="path", required = true, dataType = "String"),
            @ApiImplicitParam(name = "available", value = " 是否可用标识", required =false , dataType = "String")})
    @GetMapping("/menuList/{parentId}")
    public List<TabPermission> menuList(@PathVariable("parentId") String parentId, String available) {
        return menuService.queryMenuByParentId(parentId, available);
    }

    @ApiOperation(value = "新增资源", notes = "新增资源")
    @PostMapping("/savePermission")
    public Response addTabpermission(TabPermission tabPermission) {
        Response response = verifyForm(tabPermission);
        if (response.getCode() == "0") {
            boolean result = menuService.savePermission(tabPermission);
            if (result) {
                return Response.success().message("新增成功");
            }
            return Response.failure("新增失败");
        }
        return response;
    }

    /**
     * 验证参数是否正确
     */
    private Response verifyForm(TabPermission tabPermission) {
        if (StringUtils.isBlank(tabPermission.getName())) {
            return Response.failure("4001", "菜单名称不能为空");
        }

        if (tabPermission.getParentId() == null) {
            return Response.failure("4002", "上级菜单不能为空");
        }

        //菜单
        if (tabPermission.getType().equals(SystemConstant.MenuType.MENU.getValue())) {
            if (StringUtils.isBlank(tabPermission.getUrl())) {
                return Response.failure("4003", "菜单URL不能为空");
            }
        }

        //上级菜单类型
        String parentType = SystemConstant.MenuType.CATALOG.getValue();
        if (StringUtils.isNotBlank(tabPermission.getParentId())) {
            TabPermission parentMenu = menuService.getTabPermission(tabPermission.getParentId());
            parentType = parentMenu.getType();
        }

//        //目录、菜单
//        if (tabPermission.getType() == SystemConstant.MenuType.CATALOG.getValue() ||
//                tabPermission.getType() == SystemConstant.MenuType.MENU.getValue()) {
//            if (parentType != SystemConstant.MenuType.CATALOG.getValue()) {
//                return Response.failure("4004", "上级菜单只能为目录类型");
//            }
//        }

        //按钮
        if (tabPermission.getType() == SystemConstant.MenuType.BUTTON.getValue()) {
            if (parentType != SystemConstant.MenuType.MENU.getValue()) {
                return Response.failure("4005", "上级菜单只能为菜单类型");
            }

        }
        return Response.success();
    }

    @ApiOperation(value = "显示所有菜单，不包含按钮", notes = "显示所有菜单，不包含按钮")
    @ApiImplicitParam(name = "parentId", value = "父级id", paramType ="path", required = true, dataType = "String")
    @GetMapping("/getAllMenuNotButton/{parentId}")
    public List<TabPermission> getAllMenuNotButton(@PathVariable("parentId") String parentId) {
        return menuService.queryAllMenuNotButton(parentId);
    }

    @ApiOperation(value = "根据对应id获取对应资源信息显示", notes = "根据对应id获取对应资源信息显示")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "String")
    @GetMapping("/getTabPermissionById")
    public Response getTabPermissionById(String id) {
        TabPermission tabPermission = menuService.getTabPermission(id);
        return Response.success(tabPermission);
    }

    @ApiOperation(value = "修改对应资源信息", notes = "修改对应资源信息")
    @PutMapping("/updateTabPermissionById")
    public Response updateTabPermissionById(TabPermission tabPermission) {
        boolean result = menuService.updateTabPermission(tabPermission);
        if (result) {
            return Response.success().message("修改成功");
        }
        return Response.failure().message("修改失败");
    }

    @ApiOperation(value = "废弃资源", notes = "废弃资源")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "String")
    @DeleteMapping("/deleteTabPermission")
    public Response deleteTabPermission(String id) {
        TabRolePermission tabRolePermission = menuService.selectPermissionAndRole(id);
        if (tabRolePermission != null) {
            return Response.failure("4008", "资源不能废弃，有角色关联-_-");
        }
        boolean result = menuService.deleteTabpermission(id);
        if (result) {
            return Response.success().message("废弃成功");
        }
        return Response.failure("废弃失败");
    }

    @ApiOperation(value = "启用资源", notes = "废弃资源")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "String")
    @PutMapping("/enablePerById")
    public Response enablePerById(String id) {
        boolean b = menuService.enablePermission(id);
        if (b) {
            return Response.success().message("启用成功");
        }
        return Response.failure("启用失败-_-");
    }

}
