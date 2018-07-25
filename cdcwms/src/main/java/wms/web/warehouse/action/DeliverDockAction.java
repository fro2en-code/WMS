package wms.web.warehouse.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.warehouse.WmsDeliverDock;

import wms.business.biz.WarehouseManagementBiz;

/**
 * 送货到达道口信息
 */
@Controller
@RequestMapping("/deliverdock")
public class DeliverDockAction extends BaseAction {

	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList() {
		return "deliverDockList";
	}

	/**
	 * 查询送货到达道口信息分页
	 *
	 * @return
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsDeliverDock> list(HttpServletResponse resp, HttpSession session, int page, int rows,
			WmsDeliverDock dock) {
		dock.setWhCode(getBindWhCode(session));
		return warehouseManagementBiz.getPageData(page, rows, dock);
	}

	/**
	 * 保存、修改送货到达道口信息
	 *
	 * @return
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp saveRoleMenu(HttpServletResponse resp, HttpSession session, WmsDeliverDock dock) {
		UserBean user = getUserInfo(session);
		dock.setWhCode(getBindWhCode(session));
		dock.setUpdateUser(user.getLoginname());
		dock.setInsertUser(user.getLoginname());
		return warehouseManagementBiz.save(dock);
	}

	/**
	 * 删除送货到达道口信息
	 *
	 * @return
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp delRoleMenu(HttpServletResponse resp, String id) {
		return warehouseManagementBiz.delWmsDeliverDock(id);
	}

	/**
	 * 获取下拉道口列表
	 *
	 * @return
	 */
	@RequestMapping("/getCombobox.action")
	@ResponseBody
	public List<Map<String, String>> getCombobox(HttpServletResponse resp, HttpSession session) {
		return warehouseManagementBiz.getWmsDeliverDockCombobox(getBindWhCode(session));
	}
}