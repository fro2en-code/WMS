package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.page.PageData;
import com.wms.business.WmsWaybillList;

import wms.business.biz.WaybillBiz;

@Controller
@RequestMapping("/waybillList")
public class WaybillListAction extends BaseAction {
    @Resource
    private WaybillBiz waybillBiz;

    /**
     * 运单信息明细分页
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<WmsWaybillList> getDataWaybill(HttpSession session, int page, int rows,
            WmsWaybillList wmsWaybillList) {
        wmsWaybillList.setWhCode(getBindWhCode(session));
        return waybillBiz.getPageDataWaybillList(page, rows, wmsWaybillList);
    }
}
