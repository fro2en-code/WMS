package com.wms.business;

import com.plat.common.beans.BaseModel;

public class TaskFlow extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = -4058970628961925395L;
    private String nextTask;
    private Integer nextTaskCode;
    private String nowPerson;
    private String nowRole;
    private String nowTask;
    private Integer nowTaskCode;
    private String whCode;
    private String cron;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getNextTask() {
        return nextTask;
    }

    public String getNowPerson() {
        return nowPerson;
    }

    public void setNowPerson(String nowPerson) {
        this.nowPerson = nowPerson;
    }

    public String getNowRole() {
        return nowRole;
    }

    public void setNowRole(String nowRole) {
        this.nowRole = nowRole;
    }

    public String getNowTask() {
        return nowTask;
    }

    public void setNowTask(String nowTask) {
        this.nowTask = nowTask;
    }

    public Integer getNextTaskCode() {
        return nextTaskCode;
    }

    public void setNextTaskCode(Integer nextTaskCode) {
        this.nextTaskCode = nextTaskCode;
    }

    public Integer getNowTaskCode() {
        return nowTaskCode;
    }

    public void setNowTaskCode(Integer nowTaskCode) {
        this.nowTaskCode = nowTaskCode;
    }

    public void setNextTask(String nextTask) {
        this.nextTask = nextTask;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

}
