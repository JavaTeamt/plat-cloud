package com.czkj.common.entity;

import java.io.Serializable;

public class TabUserRole implements Serializable {
    private String id;

    private String sysUserId;

    private String sysRoleId;

    private String createTime;

    private String lastUpdateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId == null ? null : sysUserId.trim();
    }

    public String getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(String sysRoleId) {
        this.sysRoleId = sysRoleId == null ? null : sysRoleId.trim();
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

    @Override
    public String toString() {
        return "TabUserRole{" +
                "id='" + id + '\'' +
                ", sysUserId='" + sysUserId + '\'' +
                ", sysRoleId='" + sysRoleId + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}