package wms.web.orginfo.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.orginfo.ExtOraTruck;

import wms.business.biz.WarehouseManagementBiz;

/**
 * 车辆管理
 */
@Controller
@RequestMapping("/truck")
public class TruckAction extends BaseAction {

    @Resource
    private WarehouseManagementBiz warehouseManagementBiz;

    /**
     * 渲染页面
     */
    @RequestMapping("/toList.action")
    public String toList() {
        return "truckList";
    }

    @RequestMapping("/getTruckList.action")
    @ResponseBody
    public List<ExtOraTruck> getTruckList(HttpSession session,
            @RequestParam(value = "q", defaultValue = "") String key) {
        return warehouseManagementBiz.getTruck(key, getBindWhCode(session));
    }

    /**
     * 查询车辆分页
     *
     * @return
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<ExtOraTruck> list(HttpSession session, HttpServletResponse resp, int page, int rows,
            ExtOraTruck extOraTruck) {
        extOraTruck.setWhCode(getBindWhCode(session));
        return warehouseManagementBiz.getPageData(page, rows, extOraTruck);
    }

    /**
     * 保存、修改车辆
     *
     * @return
     */
    @RequestMapping("save.action")
    @ResponseBody
    public ResultResp saveRoleMenu(HttpServletResponse resp, HttpSession session, ExtOraTruck et) {
        UserBean user = getUserInfo(session);
        et.setUpdateUser(user.getLoginname());
        et.setWhCode(getBindWhCode(session));
        return warehouseManagementBiz.save(et);
    }

    /**
     * 删除车辆
     *
     * @return
     */
    @RequestMapping("del.action")
    @ResponseBody
    public ResultResp delRoleMenu(HttpServletResponse resp, String id) {
        return warehouseManagementBiz.delExtOraTruck(id);
    }

}
