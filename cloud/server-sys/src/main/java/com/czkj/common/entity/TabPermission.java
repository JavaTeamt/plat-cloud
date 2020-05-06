package com.czkj.common.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键id",name="id")
    private String id;

    @ApiModelProperty(value="资源名称",name="name")
    private String name;

    @ApiModelProperty(value="资源类型(menu,button,auth)",name="type")
    private String type;

    @ApiModelProperty(value="访问url地址",name="url")
    private String url;

    @ApiModelProperty(value="权限代码字符串/menu=>vue name",name="perCode")
    private String perCode;

    @ApiModelProperty(value="样式",name="clazz")
    private String clazz;

    @ApiModelProperty(value="父结点id",name="parentId")
    private String parentId;

    @ApiModelProperty(value="父结点名称",name="parentName")
    private String parentName;

    @ApiModelProperty(value="排序号",name="sortString")
    private String sortString;

    @ApiModelProperty(value="是否可用,1：可用，0不可用",name="available")
    private String available;

    @ApiModelProperty(value="创建时间",name="createTime")
    private String createTime;

    @ApiModelProperty(value="最后修改时间",name="lastUpdateTime")
    private String lastUpdateTime;

    @ApiModelProperty(value="菜单信息树",hidden=true)
    private List<TabPermission> childMenu =new ArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerCode() {
        return perCode;
    }

    public void setPerCode(String perCode) {
        this.perCode = perCode;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSortString() {
        return sortString;
    }

    public void setSortString(String sortString) {
        this.sortString = sortString;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
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

    public List<TabPermission> getChildMenu() {
        return childMenu;
    }

    public void setChildMenu(List<TabPermission> childMenu) {
        this.childMenu = childMenu;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String toString() {
        return "TabPermission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", perCode='" + perCode + '\'' +
                ", clazz='" + clazz + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", sortString='" + sortString + '\'' +
                ", available='" + available + '\'' +
                ", createTime='" + createTime + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", childMenu=" + childMenu +
                '}';
    }
}