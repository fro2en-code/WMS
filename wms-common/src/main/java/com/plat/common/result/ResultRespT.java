package com.plat.common.result;

import java.io.Serializable;

public class ResultRespT<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 376877487911259861L;
    private String billId;
    private String retcode = "-1";
    private String retmsg = "系统内部错误";
    private T t;

    public ResultRespT() {
        super();
    }

    public ResultRespT(cn.rtzltech.user.result.ResultRespT<T> result) {
        this(result.getRetcode(), result.getRetmsg(), result.getT());
    }

    public ResultRespT(String retcode, String retmsg) {
        this(retcode, retmsg, null);
    }

    public ResultRespT(String retcode, String retmsg, T t) {
        super();
        this.setRetcode(retcode);
        this.setRetmsg(retmsg);
        this.setT(t);
    }

    public String getBillId() {
        return billId;
    }

    public String getRetcode() {
        return retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public T getT() {
        return t;
    }

    public boolean isSuc() {
        return ResultResp.SUCCESS_CODE.equals(this.getRetcode());
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public void setT(T t) {
        this.t = t;
    }

    public void setValue(String retcode, String retmsg, T t) {
        this.setRetcode(retcode);
        this.setRetmsg(retmsg);
        this.setT(t);
    }

    @Override
    public String toString() {
        return "ResultResp [retcode=" + retcode + ", retmsg=" + retmsg + "]";
    }

}
