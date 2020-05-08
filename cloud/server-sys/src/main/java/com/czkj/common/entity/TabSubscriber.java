package com.czkj.common.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author SunMin
 * @description 用户实体类
 * @create 2020/4/14
 * @since 1.0.0
 */
public class TabSubscriber implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键id",name="id")
    private String id;

    @ApiModelProperty(value="手机号",name="mobile",required = true)
    private String mobile;

    @ApiModelProperty(value="密码",name="password",required = true)
    private String password;

    @ApiModelProperty(value="客户id",name="custid",hidden = true)
    private String  custid;

    @ApiModelProperty(value="登录状态",name="loginStatus",hidden = true)
    private String loginStatus;

    @ApiModelProperty(value="头像路径",name="headImg",hidden = true)
    private String headImg;

    @ApiModelProperty(value="创建时间",name="createTime",hidden = true)
    private Date createTime;

    @ApiModelProperty(value="客户实体类",hidden=true)
    private TabCustomer tabCustomer;

    @ApiModelProperty(value="角色实体类集合(一对多)",hidden=true)
    private List<TabRole> tabRoleList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public TabCustomer getTabCustomer() {
        return tabCustomer;
    }

    public void setTabCustomer(TabCustomer tabCustomer) {
        this.tabCustomer = tabCustomer;
    }

    public List<TabRole> getTabRoleList() {
        return tabRoleList;
    }

    public void setTabRoleList(List<TabRole> tabRoleList) {
        this.tabRoleList = tabRoleList;
    }

    @Override
    public String toString() {
        return "TabSubscriber{" +
                "id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", custid=" + custid +
                ", loginStatus='" + loginStatus + '\'' +
                ", headImg='" + headImg + '\'' +
                ", createTime='" + createTime + '\'' +
                ", tabCustomer=" + tabCustomer +
                ", tabRoleList=" + tabRoleList +
                '}';
    }
}
