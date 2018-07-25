package wms.web.warehouse.action;

import java.util.List;
import java.util.Map;
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
import com.wms.warehouse.WmsStoragGroup;

import wms.business.biz.WarehouseManagementBiz;

/**
 * 库位组信息
 *
 * @author wzz
 *
 */
@Controller
@RequestMapping("/storagGroup")
public class StoragGroupAction extends BaseAction {

	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList() {
		return "storagGroupList";
	}

	/**
	 * 查询库位组信息分页
	 *
	 * @return
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsStoragGroup> list(HttpServletResponse resp, HttpSession session, int page, int rows,
			WmsStoragGroup storaggroup) {
		storaggroup.setWhCode(getBindWhCode(session));
		return warehouseManagementBiz.getPageData(page, rows, storaggroup);
	}

	/**
	 * 保存、修改库位组信息
	 * 
	 * @return
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ResultResp saveRoleMenu(HttpServletResponse resp, HttpSession session, WmsStoragGroup storaggroup) {
		storaggroup.setWhCode(getBindWhCode(session));
		UserBean user = getUserInfo(session);
		storaggroup.setUpdateUser(user.getLoginname());
		storaggroup.setInsertUser(user.getLoginname());
		return warehouseManagementBiz.save(storaggroup);
	}

	/**
	 * 删除库位组信息
	 *
	 * @return
	 */
	@RequestMapping("del.action")
	@ResponseBody
	public ResultResp delRoleMenu(HttpServletResponse resp, String id) {
		return warehouseManagementBiz.delWmsStoragGroup(id);
	}

	/**
	 * 获取下拉库位组列表
	 */
	@RequestMapping("/getCombobox.action")
	@ResponseBody
	public List<Map<String, String>> getCombobox(HttpServletResponse resp, HttpSession session,
			@RequestParam(value = "q", defaultValue = "") String key) {
		String whCode = getBindWhCode(session);
		return warehouseManagementBiz.getGroupCombobox(key, whCode);
	}
}
