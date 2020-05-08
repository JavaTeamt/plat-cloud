package com.czkj.common.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

public class TabPermissionUrl implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id",value = "主键",hidden = true)
    private Integer id;

    @ApiModelProperty(name = "name",value = "URL")
    private String name;

    @ApiModelProperty(name = "perId",value = "权限id")
    private Integer perId;

    @ApiModelProperty(name = "createTime",value = "创建时间",hidden = true)
    private Date createTime;

    @ApiModelProperty(name = "lastUpdateTime",value = "修改时间",hidden = true)
    private Date lastUpdateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPerId() {
        return perId;
    }

    public void setPerId(Integer perId) {
        this.perId = perId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "TabPermissionUrl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", perId=" + perId +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}