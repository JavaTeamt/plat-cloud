package com.czkj.common.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @ApiModelProperty(value="主键id",name="id")
    private String id;

    /**
     * 角色编码
     */
    @ApiModelProperty(value="角色编码",name="code")
    private String code;

    /**
     * 角色名
     */
    @ApiModelProperty(value="角色名",name="name")
    private String name;

    /**
     * 是否可用
     */
    @ApiModelProperty(value="是否可用",name="available")
    private String available;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间",name="createTime")
    private String  createTime;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value="最后修改时间",name="lastUpdateTime")
    private String lastUpdateTime;

    @ApiModelProperty(value="资源",hidden=true)
    private List<TabPermission> tabPermissions = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available == null ? null : available.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<TabPermission> getTabPermissions() {
        return tabPermissions;
    }

    public void setTabPermissions(List<TabPermission> tabPermissions) {
        this.tabPermissions = tabPermissions;
    }

    @Override
    public String toString() {
        return "TabRole{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", available='" + available + '\'' +
                ", createTime='" + createTime + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", tabPermissions=" + tabPermissions +
                '}';
    }
}