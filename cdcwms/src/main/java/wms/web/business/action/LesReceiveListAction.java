package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsLesReceiveList;

import wms.business.biz.WmsLesReceiveBiz;

@Controller
@RequestMapping("/lesReceiveList")
public class LesReceiveListAction extends BaseAction {
    @Resource
    private WmsLesReceiveBiz wmsLesReceiveBiz;

    /**
     * les收货单明细分页
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<WmsLesReceiveList> getDataLesReceiveList(HttpSession session, int page, int rows,
            WmsLesReceiveList wmsLesReceiveList) {
        wmsLesReceiveList.setWhCode(getBindWhCode(session));
        return wmsLesReceiveBiz.getPageDataLesReceiveList(page, rows, wmsLesReceiveList);
    }

    private void validata(WmsLesReceiveList wmsLesReceiveList) {
        if (StringUtils.isEmpty(wmsLesReceiveList.getPartNo())) {
            throw new RuntimeException("零件请从下拉框中选取");
        }
        if (StringUtils.isEmpty(wmsLesReceiveList.getSupplNo())) {
            throw new RuntimeException("零件请从下拉框中选取");
        }
    }

    /**
     * 新增,修改les收货单明细
     */
    @RequestMapping("/save.action")
    @ResponseBody
    public ResultResp saveHandwork(HttpServletResponse resp, HttpSession session, WmsLesReceiveList wmsLesReceiveList) {
        validata(wmsLesReceiveList);
        UserBean user = getUserInfo(session);
        wmsLesReceiveList.setUpdateUser(user.getLoginname());
        wmsLesReceiveList.setWhCode(getBindWhCode(session));
        return wmsLesReceiveBiz.saveLesReceiveList(wmsLesReceiveList);
    }

    /**
     * 删除les收货单明细
     */
    @RequestMapping("/del.action")
    @ResponseBody
    public ResultResp delHandwork(HttpSession session, WmsLesReceiveList wmsLesReceiveList) {
        wmsLesReceiveList.setWhCode(getBindWhCode(session));
        return wmsLesReceiveBiz.deleteLesReceiveList(wmsLesReceiveList);
    }
}
