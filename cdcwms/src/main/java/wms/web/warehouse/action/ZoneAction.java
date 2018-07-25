package wms.web.warehouse.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.ExcelUtil.Excel;
import com.wms.warehouse.WmsZone;

import wms.business.biz.WarehouseManagementBiz;

/**
 * 库区
 */
@Controller
@RequestMapping("/zone")
public class ZoneAction extends BaseAction {

	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;

	/**
	 * 删除库区
	 *
	 * @return
	 */
	@RequestMapping("del.action")
	@ResponseBody
	public ResultResp delRoleMenu(HttpServletResponse resp, String id) {
		return warehouseManagementBiz.delZone(id);
	}

	/**
	 * 获取下拉库区列表
	 *
	 * @return
	 */
	@RequestMapping("/getCombobox.action")
	@ResponseBody
	public List<Map<String, String>> getCombobox(HttpServletResponse resp, HttpSession session) {
		return warehouseManagementBiz.getZoneCombobox(getBindWhCode(session));
	}

	/**
	 * 根据仓库代码获取库区信息
	 *
	 * @return
	 */
	@RequestMapping("/getComboboxByWhCode.action")
	@ResponseBody
	public List<Map<String, String>> getComboboxByWhCode(HttpServletResponse resp, String whCode) {
		return warehouseManagementBiz.getComboboxByWhCode(whCode);
	}

	/**
	 * 查询库区分页
	 *
	 * @return
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsZone> list(HttpServletResponse resp, HttpSession session, int page, int rows, WmsZone zone) {
		zone.setWhCode(getBindWhCode(session));
		return warehouseManagementBiz.getPageData(page, rows, zone);
	}

	/**
	 * 保存、修改库区
	 *
	 * @return
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ResultResp saveRoleMenu(HttpServletResponse resp, HttpSession session, WmsZone zone) {
		zone.setWhCode(getBindWhCode(session));
		UserBean user = getUserInfo(session);
		zone.setUpdateUser(user.getLoginname());
		zone.setInsertUser(user.getLoginname());
		return warehouseManagementBiz.save(zone);
	}

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		new Excel().setColumn("whCode", "仓库代码").setColumn("ZName", "库区名称").setColumn("zoneCode", "库区代码")
				.setColumn("statu", "库区状态(0启用 1停用)").setColumn("descrip", "库区备注").setTempName("库区导入模板")
				.setClassName(WmsZone.class.getName()).Finish(request);
		return "zoneList";
	}

}
