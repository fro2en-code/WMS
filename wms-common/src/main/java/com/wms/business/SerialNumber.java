package com.wms.business;

import java.util.Date;

import com.plat.common.beans.BaseModel;

/**
 * 序列号生成
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年5月2日
 */
public class SerialNumber extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 8156170238396631706L;
    private Integer number;
    private Date startTime;

    public Integer getNumber() {
        return number;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
