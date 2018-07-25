package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.page.PageData;
import com.wms.business.WmsWaybill;

import wms.business.biz.WaybillBiz;

@Controller
@RequestMapping("/waybill")
public class WaybillAction extends BaseAction {
    @Resource
    private WaybillBiz waybillBiz;

    @RequestMapping("/toList.action")
    public String toList() {
        return "waybillList";
    }

    /**
     * 运单信息分页
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<WmsWaybill> getDataWaybill(HttpSession session, int page, int rows, WmsWaybill wmsWaybill) {
        wmsWaybill.setWhCode(getBindWhCode(session));
        return waybillBiz.getPageDataWaybill(page, rows, wmsWaybill);
    }
}
