package wms.web.business.action;

import java.util.List;

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
import com.wms.business.WmsHandworkReceive;
import com.wms.business.WmsHandworkReceiveList;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.ymt.utils.DockUtils;
import com.ymt.utils.Lock;

import wms.business.biz.TaskBiz;
import wms.business.biz.WmsHandworkReceiveBiz;

/**
 * 手工收货单据
 *
 */
@Controller
@RequestMapping("/handReceive")
public class HandworkReceiveAction extends BaseAction {
	@Resource
	private DockUtils dockUtils;
	@Resource
	private TaskBiz taskBiz;
	@Resource
	private WmsHandworkReceiveBiz wmsHandworkReceiveBiz;

	@RequestMapping("/addTask.action")
	@ResponseBody
	@Lock("billId")
	public ResultResp addTask(HttpSession session, String billId) {
		UserBean user = getUserInfo(session);
		String reciveStorageCode = dockUtils.getStorage(user.getLoginname(), getBindWhCode(session));
		if (null == reciveStorageCode || null == session.getAttribute(StringUtil.DEFAULT_DOCK)) {
			return new ResultResp(ResultResp.ERROR_CODE, "请先绑定收货道口");
		}
		WmsHandworkReceive wmsHandworkReceive = wmsHandworkReceiveBiz.getWmsHandworkReceiveEntity(billId);
		// 判断任务是否有明细
		List<WmsHandworkReceiveList> wmsHandworkReceiveLists = wmsHandworkReceiveBiz
				.getWmsHandworkReceiveList(wmsHandworkReceive.getMapSheetNo(), wmsHandworkReceive.getWhCode());
		if (BaseModel.INT_INIT.equals(wmsHandworkReceiveLists.size())) {
			return new ResultResp(ResultResp.ERROR_CODE, "该单据没有明细,无法生成任务!");
		}
		if (BaseModel.INT_INIT.equals(wmsHandworkReceive.getStatus())
				|| BaseModel.INT_CANCEL.equals(wmsHandworkReceive.getStatus())) {
			WmsTask task = new WmsTask();
			task.setBillid(billId);
			task.setTaskdesc(
					"手工收货-" + session.getAttribute(StringUtil.DEFAULT_DOCK) + "-" + wmsHandworkReceive.getMapSheetNo());
			task.setLevel(0);
			task.setType(1);
			task.setStatus(0);
			task.setWhCode(getBindWhCode(session));
			task.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
			WmsTaskBill taskBill = new WmsTaskBill();
			taskBill.setNextStorageCode(reciveStorageCode);
			task.setWmsTaskBill(taskBill);
			return taskBiz.addTask(task);
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "当前任务已生成任务!");
		}
	}

	/**
	 * 删除手工收货单
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp delHandwork(HttpSession session, WmsHandworkReceive wmsHandworkReceive) {
		WmsHandworkReceive wmsHandworkReceives = wmsHandworkReceiveBiz
				.getWmsHandworkReceiveEntity(wmsHandworkReceive.getId());
		if (BaseModel.INT_INIT.equals(wmsHandworkReceives.getStatus())) {
			wmsHandworkReceiveBiz.deleteHandworkReceive(wmsHandworkReceives);
			return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "该单据已经生成任务,无法删除!");
		}
	}

	/**
	 * 手工收货单据分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsHandworkReceive> list(HttpServletResponse resp, int page, int rows,
			WmsHandworkReceive wmsHandworkReceive, HttpSession session) {
		wmsHandworkReceive.setWhCode(getBindWhCode(session));
		return wmsHandworkReceiveBiz.getPageDataHandworkReceive(page, rows, wmsHandworkReceive);
	}

	/**
	 * 保存,修改手工收货单
	 *
	 * @return
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp saveHandwork(HttpServletResponse resp, HttpSession session,
			WmsHandworkReceive wmsHandworkReceive) {
		if (StringUtil.isEmpty(wmsHandworkReceive.getId())) {
			UserBean user = getUserInfo(session);
			wmsHandworkReceive.setWhCode(getBindWhCode(session));
			wmsHandworkReceive.setUpdateUser(user.getLoginname());
			return wmsHandworkReceiveBiz.saveHandworkReceive(wmsHandworkReceive);
		} else {
			WmsHandworkReceive handworkReceive = wmsHandworkReceiveBiz
					.getWmsHandworkReceiveEntity(wmsHandworkReceive.getId());
			if (BaseModel.INT_INIT.equals(handworkReceive.getStatus())) {
				UserBean user = getUserInfo(session);
				wmsHandworkReceive.setWhCode(getBindWhCode(session));
				wmsHandworkReceive.setUpdateUser(user.getLoginname());
				return wmsHandworkReceiveBiz.saveHandworkReceive(wmsHandworkReceive);
			} else {
				return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不能修改!");
			}
		}
	}

	@RequestMapping("/toList.action")
	public String toList() {
		return "handReceiveList";
	}
}
