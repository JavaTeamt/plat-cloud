package com.czkj.common.entity;

import java.io.Serializable;

public class TabRolePermission implements Serializable {
    private String id;

    private String sysRoleId;

    private String sysPermissionId;

    private String createTime;

    private String lastUpdateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(String sysRoleId) {
        this.sysRoleId = sysRoleId == null ? null : sysRoleId.trim();
    }

    public String getSysPermissionId() {
        return sysPermissionId;
    }

    public void setSysPermissionId(String sysPermissionId) {
        this.sysPermissionId = sysPermissionId == null ? null : sysPermissionId.trim();
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
        return "TSysRolePermission{" +
                "id='" + id + '\'' +
                ", sysRoleId='" + sysRoleId + '\'' +
                ", sysPermissionId='" + sysPermissionId + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}