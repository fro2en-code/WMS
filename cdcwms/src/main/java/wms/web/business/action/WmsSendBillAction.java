package wms.web.business.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsSendBill;
import com.wms.business.WmsSendBillList;
import com.wms.business.WmsWaybill;
import com.ymt.utils.Lock;

import wms.business.biz.WaybillBiz;

@Controller
@RequestMapping("/wmsSendBill")
public class WmsSendBillAction extends BaseAction {
    @Resource
    private WaybillBiz waybillBiz;

    @RequestMapping("/toList.action")
    public String toList() {
        return "wmsSendBill";
    }

    @RequestMapping("/send.action")
    @ResponseBody
    @Lock(type = Lock.LOCK_static, value = "WmsSendBillAction.send")
    public ResultResp send(HttpSession session, WmsWaybill wmsWaybill,
            @RequestParam(value = "lesBill[]", required = false) String[] lesBill,
            @RequestParam(value = "handBill[]", required = false) String[] handBill) {
        UserBean user = getUserInfo(session);
        wmsWaybill.setInsertUser(user.getLoginname());
        wmsWaybill.setWhCode(getBindWhCode(session));
        waybillBiz.sendTruck(wmsWaybill, lesBill, handBill);
        return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_CODE);
    }

    @RequestMapping("/list.action")
    @ResponseBody
    public List<WmsSendBill> getWmsSendBill(HttpSession session, WmsSendBill wmsSendBill) {
        wmsSendBill.setWhCode(getBindWhCode(session));
        return waybillBiz.getDataWmsSendBill(wmsSendBill);
    }

    @RequestMapping("/listDetail.action")
    @ResponseBody
    public PageData<WmsSendBillList> getWmsSendBill(HttpSession session, int page, int rows,
            WmsSendBillList wmsSendBillList) {
        wmsSendBillList.setWhCode(getBindWhCode(session));
        return waybillBiz.getPageDataWmsSendBillList(page, rows, wmsSendBillList);
    }

}
