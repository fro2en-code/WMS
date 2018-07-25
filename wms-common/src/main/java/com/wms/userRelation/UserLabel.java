package com.wms.userRelation;

import com.plat.common.beans.BaseModel;

// default package

/**
 * UserLabel entity. @author MyEclipse Persistence Tools
 */

public class UserLabel extends BaseModel {

    // Fields

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String tid;
    private String userId;
    private String userLoginname;

    public String getTid() {
        return tid;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getUserLoginname() {
        return this.userLoginname;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserLoginname(String userLoginname) {
        this.userLoginname = userLoginname;
    }

}