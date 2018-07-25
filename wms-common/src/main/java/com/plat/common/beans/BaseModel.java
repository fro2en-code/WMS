package com.plat.common.beans;

import java.io.Serializable;

public abstract class BaseModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final Integer INT_INIT = Integer.valueOf(0);
    public static final Integer INT_CREATE = Integer.valueOf(1);
    public static final Integer INT_COMPLETE = Integer.valueOf(2);
    public static final Integer INT_CANCEL = Integer.valueOf(3);
    public static final Integer INT_RETURN = Integer.valueOf(4);
    public static final Integer INT_ERROR = Integer.valueOf(5);

    /**
     * 删除标志
     */
    private Integer delFlag;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 插入时间
     */
    private String insertTime;

    /**
     * 插入操作人
     */
    private String insertUser;

    /**
     * 说明
     */
    private String remark;

    /**
     * 排序号
     */
    private Integer sortNum;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 更新操作人
     */
    private String updateUser;
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseModel other = (BaseModel) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
            return super.equals(other);
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public String getId() {
        return id;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public String getInsertUser() {
        return insertUser;
    }

    public String getRemark() {
        return remark;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? super.hashCode() : id.hashCode());
        return result;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public void setInsertUser(String insertUser) {
        this.insertUser = insertUser;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
