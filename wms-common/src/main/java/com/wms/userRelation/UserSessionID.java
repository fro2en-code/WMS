package com.wms.userRelation;

import com.plat.common.beans.BaseModel;

public class UserSessionID extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String userLoginname;
    private String SessionId;
    private String dockCode;

    public String getDockCode() {
        return dockCode;
    }

    public void setDockCode(String dockCode) {
        this.dockCode = dockCode;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLoginname() {
        return userLoginname;
    }

    public void setUserLoginname(String userLoginname) {
        this.userLoginname = userLoginname;
    }

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String sessionId) {
        SessionId = sessionId;
    }

}
