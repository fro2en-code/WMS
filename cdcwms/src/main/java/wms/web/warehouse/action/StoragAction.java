package wms.web.warehouse.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.ExcelUtil.Excel;
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsStorag;
import com.ymt.utils.Lock;

import wms.business.biz.WarehouseManagementBiz;

/**
 * 库位信息
 */
@Controller
@RequestMapping("/storag")
public class StoragAction extends BaseAction {

	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList(HttpServletRequest req) {

		new Excel().setColumn("storageCode", "库位编号").setColumn("zoneCode", "所属库区").setColumn("whCode", "所属仓库编号")
				.setColumn("groupCode", "所属库位组编码").setColumn("lineNo", "道编号").setColumn("colNo", "列编号")
				.setColumn("rowNo", "排编号").setColumn("layerNo", "层编号").setColumn("storageType", "库位功能 (0存储+拣货 1存储)")
				.setColumn("sType", "库位类型(1普通货架 2地面平仓 3 高位货架)").setColumn("layType", "物品放置类别(1.托盘放置 2堆码放置)")
				.setColumn("mulSup", "供应商混放标志(0不允许 1允许)").setColumn("mulBth", "批次混放标志(0 不允许、1允许)")
				.setColumn("statu", "库位状态(0正常 1停用)").setColumn("gStatu", "货物状态(0空库位 1预分配 2.有货)")
				.setClassName(WmsStorag.class.getName()).setTempName("库位信息导入").Finish(req);
		return "storagList";
	}

	/**
	 * 查询库位分页
	 *
	 * @return
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsStorag> list(HttpServletResponse resp, HttpSession session, int page, int rows,
			WmsStorag storag) {
		storag.setWhCode(getBindWhCode(session));
		return warehouseManagementBiz.getPageData(page, rows, storag);
	}

	private void validata(WmsStorag storag) {
		if (StringUtils.isEmpty(storag.getGroupCode())) {
			storag.setGroupCode(null);
		}
	}

	/**
	 * 保存、修改库位
	 *
	 * @return
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ResultResp saveRoleMenu(HttpServletResponse resp, HttpSession session, WmsStorag storag) {
		validata(storag);
		storag.setWhCode(getBindWhCode(session));
		UserBean user = getUserInfo(session);
		storag.setUpdateUser(user.getLoginname());
		storag.setInsertUser(user.getLoginname());
		return warehouseManagementBiz.save(storag);
	}

	/**
	 * 删除库位
	 *
	 * @return
	 */
	@RequestMapping("del.action")
	@ResponseBody
	public ResultResp delRoleMenu(HttpServletResponse resp, String id) {
		return warehouseManagementBiz.delStorag(id);
	}

	/**
	 * 获取库位下拉框
	 */
	@RequestMapping("getComboBoxStorage.action")
	@ResponseBody
	public List<Map<String, String>> getComboBoxStorage(HttpSession session, String storageCode) {
		return warehouseManagementBiz.getComboBoxStorage(storageCode, getBindWhCode(session));
	}

	/**
	 * 获取库位下拉框(必须输入3个以上的字符才能查询)
	 */
	@RequestMapping("getComboBoxStorageCode.action")
	@ResponseBody
	public List<Map<String, String>> getComboBoxStorageCode(HttpServletResponse resp,
			@RequestParam(value = "q", defaultValue = "") String key, HttpSession session) {
		String whCode = getBindWhCode(session);
		return warehouseManagementBiz.getComboBoxStorageCode(key, whCode);
	}

	/**
	 * 批量绑定库位组
	 */
	@RequestMapping("bind.action")
	@ResponseBody
	@Lock(type = Lock.LOCK_static, value = "StoragAction.bindStoragGroup")
	public ResultResp bindStoragGroup(HttpSession session, WmsStorag storag,
			@RequestParam(value = "storags[]", required = false) String[] storags) {
		storag.setWhCode(getBindWhCode(session));
		UserBean user = getUserInfo(session);
		storag.setUpdateUser(user.getLoginname());
		storag.setUpdateTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
		return warehouseManagementBiz.bindStoragGroup(storag, storags);
	}
}
