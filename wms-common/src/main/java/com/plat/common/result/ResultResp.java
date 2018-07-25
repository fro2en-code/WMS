package com.plat.common.result;

import java.io.Serializable;

public class ResultResp implements Serializable {
    public static final String ERROR_CODE = "-1";
    public static final String ERROR_MSG = "内部错误";
    /**
     *
     */
    private static final long serialVersionUID = 8004031538824509294L;
    public static final String SKIP_CODE = "1";
    public static final String SUCCESS_CODE = "0";
    public static final String SUCCESS_MSG = "操作成功";

    private String billId;

    private String retcode = "-1";

    private String retmsg = "系统内部错误";

    public ResultResp() {
        super();
    }

    public ResultResp(cn.rtzltech.user.result.ResultResp resultResp) {
        this(resultResp.getRetcode(), resultResp.getRetmsg());
    }

    public ResultResp(String retcode, String retmsg) {
        this(retcode, retmsg, null);
    }

    public ResultResp(String retcode, String retmsg, String billId) {
        super();
        this.setRetcode(retcode);
        this.setRetmsg(retmsg);
        this.setBillId(billId);
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

    @Override
    public String toString() {
        return "ResultResp [retcode=" + retcode + ", retmsg=" + retmsg + "]";
    }

}
