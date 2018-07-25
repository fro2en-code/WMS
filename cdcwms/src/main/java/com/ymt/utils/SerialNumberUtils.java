package com.ymt.utils;

import java.util.Date;

import wms.business.biz.ToolBiz;

/**
 * 获取流水号(调用方法参考:com.ymt.api.SerialNumberUtilsTest)
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年5月2日
 */
public abstract class SerialNumberUtils {

    protected Integer getInitNumber() {
        return 1;
    }

    /**
     * 获取流水号
     *
     * @param length
     *            长度,不足的话补 0
     */
    public String getSerialNumber(int length) {
        String className = this.getClass().getName();// className 作为ID
        LockUtils lockUnit = SpringContex.getBean("lockUtils");
        lockUnit.lock(className, "com.ymt.utils.SerialNumberUtils.getSerialNumber", 10);
        try {
            ToolBiz toolBiz = SpringContex.getBean("toolBiz");
            return toolBiz.getSerialNumber(this, className, getInitNumber(), length);
        } finally {
            lockUnit.releaseLock(className);
        }
    }

    /**
     * 是否过期从头再来(比如每天或每月清零,如果需要从初始值开始,返回false,否则返回true)
     *
     * @param startTime
     *            本次序列号开始时间
     */
    public abstract boolean isExpired(Date startTime);
}
