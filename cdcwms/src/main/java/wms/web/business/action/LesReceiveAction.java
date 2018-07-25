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
import com.wms.business.WmsLesReceive;
import com.wms.business.WmsLesReceiveList;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.ymt.utils.DockUtils;
import com.ymt.utils.Lock;

import wms.business.biz.TaskBiz;
import wms.business.biz.WmsLesReceiveBiz;

@Controller
@RequestMapping("/lesReceive")
public class LesReceiveAction extends BaseAction {
	@Resource
	private DockUtils dockUtils;
	@Resource
	private TaskBiz taskBiz;
	@Resource
	private WmsLesReceiveBiz wmsLesReceiveBiz;

	@RequestMapping("/toList.action")
	public String toList() {
		return "lesReceiveList";
	}

	@RequestMapping("/addTask.action")
	@ResponseBody
	@Lock
	public ResultResp addTask(HttpSession session, String billId) {
		UserBean user = getUserInfo(session);
		String reciveStorageCode = dockUtils.getStorage(user.getLoginname(), getBindWhCode(session));
		if (null == reciveStorageCode || null == session.getAttribute(StringUtil.DEFAULT_DOCK)) {
			return new ResultResp(ResultResp.ERROR_CODE, "请先绑定收货道口");
		}
		WmsLesReceive wmsLesReceive = wmsLesReceiveBiz.getWmsLesReceiveEntity(billId);
		// 判断该单据明细是否为空
		List<WmsLesReceiveList> wmsLesReceiveLists = wmsLesReceiveBiz.getLesReceiveList(wmsLesReceive.getMapSheetNo(),
				wmsLesReceive.getWhCode());
		if (BaseModel.INT_INIT.equals(wmsLesReceiveLists.size())) {
			return new ResultResp(ResultResp.ERROR_CODE, "该单据没有明细,无法生成任务!");
		}
		// 判断该单据是否已经生成任务
		if (BaseModel.INT_INIT.equals(wmsLesReceive.getStatus())) {
			WmsTask task = new WmsTask();
			task.setBillid(billId);
			task.setTaskdesc(
					"LES收货-" + session.getAttribute(StringUtil.DEFAULT_DOCK) + "-" + wmsLesReceive.getMapSheetNo());
			task.setLevel(0);
			task.setType(2);
			task.setStatus(0);
			task.setWhCode((String) session.getAttribute("defaultWhCode"));
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
	 * les收货单据分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsLesReceive> getDataLesReceive(int page, int rows, WmsLesReceive wmsLesReceive,
			HttpSession session) {
		wmsLesReceive.setWhCode(getBindWhCode(session));
		return wmsLesReceiveBiz.getPageDataLesReceive(page, rows, wmsLesReceive);
	}

	/**
	 * 保存,修改les收货单据
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp saveHandwork(HttpServletResponse resp, HttpSession session, WmsLesReceive wmsLesReceive) {

		if (StringUtil.isEmpty(wmsLesReceive.getId())) {
			UserBean user = getUserInfo(session);
			wmsLesReceive.setWhCode(getBindWhCode(session));
			wmsLesReceive.setUpdateUser(user.getLoginname());
			return wmsLesReceiveBiz.saveLesReceive(wmsLesReceive);
		} else {
			WmsLesReceive lesReceive = wmsLesReceiveBiz.getWmsLesReceiveEntity(wmsLesReceive.getId());
			if (BaseModel.INT_INIT.equals(lesReceive.getStatus())) {
				UserBean user = getUserInfo(session);
				wmsLesReceive.setWhCode(getBindWhCode(session));
				wmsLesReceive.setUpdateUser(user.getLoginname());
				return wmsLesReceiveBiz.saveLesReceive(wmsLesReceive);
			} else {
				return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不能修改!");
			}
		}

	}

	/**
	 * 删除les收货单据
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp delHandwork(HttpSession session, WmsLesReceive wmsLesReceive) {
		WmsLesReceive wmsLesReceives = wmsLesReceiveBiz.getWmsLesReceiveEntity(wmsLesReceive.getId());
		if (BaseModel.INT_INIT.equals(wmsLesReceives.getStatus())) {
			wmsLesReceiveBiz.deleteLesReceive(wmsLesReceives);
			return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "该收货单已经生成任务,无法删除!");
		}

	}
}
