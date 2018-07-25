package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.BaseModel;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsAdj;
import com.ymt.utils.Lock;

import wms.business.biz.WarehouseManagementBiz;
import wms.business.biz.WmsAdjBiz;
import wms.business.biz.WmsStockBiz;
import wms.business.unit.InventoryUnit;

/**
 * 库存调整申请单
 *
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/adj")
public class AdjAction extends BaseAction {
	@Resource
	private InventoryUnit inventoryUnit;
	@Resource
	private WmsAdjBiz wmsAdjBiz;
	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;
	@Resource
	private WmsStockBiz wmsStockBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList() {
		return "adjList";
	}

	/**
	 * 库存调整申请单分页
	 *
	 * @return
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsAdj> list(HttpServletResponse resp, int page, int rows, HttpSession session, WmsAdj wmsAdj) {
		wmsAdj.setWhCode(getBindWhCode(session));
		return wmsAdjBiz.getPageData(page, rows, wmsAdj);
	}

	private void validata(WmsAdj wmsAdj) {
		if (StringUtils.isEmpty(wmsAdj.getOraCode())) {
			throw new RuntimeException("零件请从下拉框中选取");
		}
		if (StringUtils.isEmpty(wmsAdj.getGcode())) {
			throw new RuntimeException("零件请从下拉框中选取");
		}
		if (StringUtils.isEmpty(wmsAdj.getGtype())) {
			throw new RuntimeException("零件请从下拉框中选取");
		}
	}

	/**
	 * 新增,修改
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ResultResp save(HttpServletResponse resp, HttpSession session, WmsAdj wmsAdj) {
		validata(wmsAdj);
		if (wmsAdj.getAdjNum() <= BaseModel.INT_INIT) {
			return new ResultResp(ResultResp.ERROR_CODE, "调整数量不允许小于0!");
		}
		if (StringUtil.isEmpty(wmsAdj.getId())) {
			wmsAdj.setWhCode(getBindWhCode(session));
			UserBean user = getUserInfo(session);
			wmsAdj.setUpdateUser(user.getLoginname());
			return wmsAdjBiz.save(wmsAdj);
		} else {
			WmsAdj adj = wmsAdjBiz.getEntity(wmsAdj.getId());
			if (BaseModel.INT_INIT.equals(adj.getStatu())) {
				wmsAdj.setWhCode(getBindWhCode(session));
				UserBean user = getUserInfo(session);
				wmsAdj.setUpdateUser(user.getLoginname());
				return wmsAdjBiz.save(wmsAdj);
			} else {
				return new ResultResp(ResultResp.ERROR_CODE, "当前单据已操作,不允许修改!");
			}
		}
	}

	/**
	 * 删除
	 *
	 * @return
	 */
	@RequestMapping("del.action")
	@ResponseBody
	public ResultResp del(HttpServletResponse resp, String id) {
		return wmsAdjBiz.delete(id);
	}

	/**
	 * 根据库存调整申请单变更库存
	 *
	 * @return
	 */
	@RequestMapping("update.action")
	@ResponseBody
	@Lock
	public ResultResp updateStock(HttpServletResponse resp, HttpSession session, WmsAdj wmsAdjs) throws Exception {
		return wmsAdjBiz.updateStock(wmsAdjs);
	}
}
