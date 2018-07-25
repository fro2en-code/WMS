package wms.web.warehouse.action;

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
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsDock;

import wms.business.biz.WarehouseManagementBiz;

/**
 * 月台信息
 */
@Controller
@RequestMapping("/dock")
public class DockAction extends BaseAction {

	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;

	@RequestMapping("/bindDock.action")
	@ResponseBody
	public ResultResp bindDock(HttpSession session, WmsDock dock) {
		UserBean user = getUserInfo(session);
		warehouseManagementBiz.bindWmsDock(dock.getDockCode(), user.getLoginname());
		session.setAttribute(StringUtil.DEFAULT_DOCK, dock.getDockCode());
		return new ResultResp(ResultResp.SUCCESS_CODE, "绑定道口操作完成");
	}

	/**
	 * 删除月台
	 *
	 * @return
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp delRoleMenu(HttpServletResponse resp, String id) {
		return warehouseManagementBiz.delWmsDock(id);
	}

	/**
	 * 查询月台分页
	 *
	 * @return
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsDock> list(HttpServletResponse resp, HttpSession session, int page, int rows, String name) {
		String whCode = getBindWhCode(session);
		return warehouseManagementBiz.getPageDataWmsDock(page, rows, whCode);
	}

	public void validata(WmsDock dock) {
		if (StringUtil.isEmpty(dock.getZoneCode())) {
			throw new RuntimeException("所属库位请从下拉框里选取");
		}
	}

	/**
	 * 保存、修改月台
	 *
	 * @return
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp saveRoleMenu(HttpServletResponse resp, HttpSession session, WmsDock dock) {
		validata(dock);
		UserBean user = getUserInfo(session);
		dock.setWhCode(getBindWhCode(session));
		dock.setUpdateUser(user.getLoginname());
		dock.setInsertUser(user.getLoginname());
		return warehouseManagementBiz.save(dock);
	}

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList() {
		return "dockList";
	}

}
