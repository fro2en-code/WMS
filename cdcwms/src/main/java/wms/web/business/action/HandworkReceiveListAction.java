package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsHandworkReceiveList;

import wms.business.biz.WmsHandworkReceiveBiz;

/**
 * 手工收货单据明细
 *
 */
@Controller
@RequestMapping("/handworkReceiveList")
public class HandworkReceiveListAction extends BaseAction {
    @Resource
    private WmsHandworkReceiveBiz wmsHandworkReceiveBiz;

    /**
     * 手工收货单据分页
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<WmsHandworkReceiveList> list(HttpSession session, int page, int rows,
            WmsHandworkReceiveList wmsHandworkReceiveList) {
        wmsHandworkReceiveList.setWhCode(getBindWhCode(session));
        return wmsHandworkReceiveBiz.getPageHandworkReceiveList(page, rows, wmsHandworkReceiveList);
    }

    private void validata(WmsHandworkReceiveList wmsHandworkReceiveList) {
        if (StringUtils.isEmpty(wmsHandworkReceiveList.getPartNo())) {
            throw new RuntimeException("零件请从下拉框中选取");
        }
        if (StringUtils.isEmpty(wmsHandworkReceiveList.getSupplNo())) {
            throw new RuntimeException("零件请从下拉框中选取");
        }
    }

    /**
     * 新增,修改手工收货明细单
     */
    @RequestMapping("/save.action")
    @ResponseBody
    public ResultResp saveHandwork(HttpSession session, WmsHandworkReceiveList wmsHandworkReceiveList) {
        validata(wmsHandworkReceiveList);
        UserBean user = getUserInfo(session);
        wmsHandworkReceiveList.setWhCode(getBindWhCode(session));
        wmsHandworkReceiveList.setUpdateUser(user.getLoginname());
        return wmsHandworkReceiveBiz.saveHandworkReceiveList(wmsHandworkReceiveList);
    }

    /**
     * 删除手工收货明细单
     */
    @RequestMapping("/del.action")
    @ResponseBody
    public ResultResp delHandwork(HttpSession session, WmsHandworkReceiveList wmsHandworkReceiveList) {
        wmsHandworkReceiveList.setWhCode(getBindWhCode(session));
        return wmsHandworkReceiveBiz.deleteHandworkReceiveList(wmsHandworkReceiveList);
    }
}
