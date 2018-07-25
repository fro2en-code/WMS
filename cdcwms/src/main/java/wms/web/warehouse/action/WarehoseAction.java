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
import com.wms.warehouse.WmsWarehouse;

import wms.business.biz.WarehouseManagementBiz;

@Controller
@RequestMapping(value = "/warehouse")
public class WarehoseAction extends BaseAction {

	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;

	/**
	 * 删除仓库
	 *
	 * @return
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp del(HttpServletResponse resp, String id) {
		return warehouseManagementBiz.delWarehouseById(id);
	}

	/**
	 * 获取下拉仓库列表
	 *
	 * @return
	 */
	@RequestMapping("/getCombobox.action")
	@ResponseBody
	public List<Map<String, String>> getCombobox(HttpServletResponse resp) {
		return warehouseManagementBiz.getCombobox();
	}

	/**
	 * 仓库列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/list.action")
	@ResponseBody
	public PageData<WmsWarehouse> list(HttpServletResponse resp, int page, int rows, String name) {
		return warehouseManagementBiz.getPageDataWmsWarehouse(page, rows, name);
	}

	/**
	 * 新增、修改仓库
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save.action")
	public ResultResp save(HttpServletResponse resp, HttpSession session, WmsWarehouse wms) {
		UserBean user = getUserInfo(session);
		wms.setUpdateUser(user.getLoginname());
		wms.setInsertUser(user.getLoginname());
		return warehouseManagementBiz.saveWarehouse(wms);
	}

	@RequestMapping(value = "/toList.action")
	public String toList() {
		return "warehouseList";
	}

}
