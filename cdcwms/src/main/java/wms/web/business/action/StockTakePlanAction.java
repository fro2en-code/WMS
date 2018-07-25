package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.BaseModel;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsStocktakePlan;
import com.wms.business.WmsTask;
import com.ymt.utils.Lock;

import wms.business.biz.StocktakeBiz;
import wms.business.biz.TaskBiz;

/**
 * 盘库申请表
 *
 * @author wangzz
 *
 */
@Controller
@RequestMapping("/stocktakePlan")
public class StockTakePlanAction extends BaseAction {
	@Resource
	private TaskBiz taskBiz;
	@Resource
	private StocktakeBiz stocktakeBiz;

	/**
	 * 生成盘库任务
	 *
	 */
	@RequestMapping("/addTask.action")
	@ResponseBody
	@Lock("billId")
	public ResultResp addTask(HttpSession session, String billId) {
		WmsStocktakePlan wmsStocktakePlan = stocktakeBiz.getWmsStocktakePlanEntity(billId);
		if (BaseModel.INT_INIT.equals(wmsStocktakePlan.getStatus())) {
			// 任务描述,单据ID,仓库代码,任务优先级,单据类型,(任务执行人,任务执行岗位 至少有一个)
			WmsTask task = new WmsTask();
			task.setBillid(billId);
			if (wmsStocktakePlan.getTakeType().equals(BaseModel.INT_INIT)) {
				task.setTaskdesc("盘库-" + wmsStocktakePlan.getStorageCode());
			}
			task.setLevel(0);
			task.setType(9);
			task.setStatus(0);
			task.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
			task.setWhCode((String) session.getAttribute("defaultWhCode"));
			task.setExecutorId(wmsStocktakePlan.getTakeWokerCode());
			return taskBiz.addTask(task);
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "该单据已生成任务!");
		}
	}

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList() {
		return "stockTakePlanList";
	}

	/**
	 * 盘库申请表分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsStocktakePlan> getPageDataStockTakePlan(int page, int rows, HttpSession session,
			WmsStocktakePlan wmsStocktakePlan) {
		String whCode = getBindWhCode(session);
		wmsStocktakePlan.setWhCode(whCode);
		return stocktakeBiz.getPageDataStockTakePlan(page, rows, wmsStocktakePlan);
	}

	/**
	 * 新增,修改
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ResultResp save(HttpServletResponse resp, HttpSession session, WmsStocktakePlan wmsStocktakePlan) {
		if (StringUtil.isEmpty(wmsStocktakePlan.getId())) {
			String whCode = getBindWhCode(session);
			wmsStocktakePlan.setWhCode(whCode);
			UserBean user = getUserInfo(session);
			wmsStocktakePlan.setUpdateUser(user.getLoginname());
			return stocktakeBiz.saveStockTakePlan(wmsStocktakePlan);
		} else {
			WmsStocktakePlan stocktakePlan = stocktakeBiz.getWmsStocktakePlanEntity(wmsStocktakePlan.getId());
			if (BaseModel.INT_INIT.equals(stocktakePlan.getStatus())) {
				String whCode = getBindWhCode(session);
				wmsStocktakePlan.setWhCode(whCode);
				UserBean user = getUserInfo(session);
				wmsStocktakePlan.setUpdateUser(user.getLoginname());
				return stocktakeBiz.saveStockTakePlan(wmsStocktakePlan);
			} else {
				return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不能修改!");
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
		return stocktakeBiz.deleteStockTakePlan(id);
	}
}
