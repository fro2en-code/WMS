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
import com.wms.business.WmsHandworkSend;
import com.wms.business.WmsTask;
import com.ymt.utils.Lock;

import wms.business.biz.TaskBiz;
import wms.business.biz.WmsHandworkSendBiz;

/**
 * 手工发货单据
 *
 */
@Controller
@RequestMapping("/handworkSend")
public class HandworkSendAction extends BaseAction {
	@Resource
	private TaskBiz taskBiz;
	@Resource
	private WmsHandworkSendBiz wmsHandworkSendBiz;

	@RequestMapping("/addTask.action")
	@ResponseBody
	@Lock("billId")
	public ResultResp addTask(HttpSession session, String billId) {
		WmsTask task = wmsHandworkSendBiz.createDefaultTaskByBillID(billId);
		task.setWhCode(getBindWhCode(session));
		return taskBiz.addTask(task);
	}

	@RequestMapping("/toList.action")
	public String toList() {
		return "handSendList";
	}

	@RequestMapping("/setReturn.action")
	@ResponseBody
	@Lock
	public ResultResp setReturn(String id) {
		return wmsHandworkSendBiz.setHandworkSendReturn(id);
	}

	/**
	 * 手工发货单据分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsHandworkSend> list(HttpServletResponse resp, HttpSession session, int page, int rows,
			WmsHandworkSend wmsHandworkSend) {
		wmsHandworkSend.setWhCode(getBindWhCode(session));
		return wmsHandworkSendBiz.getPageDataHandworkSend(page, rows, wmsHandworkSend);
	}

	/**
	 * 保存,修改手工发货单
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp saveHandwork(HttpSession session, WmsHandworkSend wmsHandworkSend) {
		if (StringUtil.isEmpty(wmsHandworkSend.getId())) {// 新增
			wmsHandworkSend.setWhCode((String) session.getAttribute("defaultWhCode"));
			UserBean user = getUserInfo(session);
			wmsHandworkSend.setUpdateUser(user.getLoginname());
			return wmsHandworkSendBiz.saveHandworkSend(wmsHandworkSend);
		} else {
			WmsHandworkSend handworkSend = wmsHandworkSendBiz.getHandworkSendEntity(wmsHandworkSend.getId());
			if (BaseModel.INT_INIT.equals(handworkSend.getStatus())) {
				wmsHandworkSend.setWhCode((String) session.getAttribute("defaultWhCode"));
				UserBean user = getUserInfo(session);
				wmsHandworkSend.setUpdateUser(user.getLoginname());
				return wmsHandworkSendBiz.saveHandworkSend(wmsHandworkSend);
			} else {
				return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不可以修改!");
			}
		}
	}

	/**
	 * 删除手工发货单
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp delHandwork(HttpSession session, WmsHandworkSend wmsHandworkSend) {
		WmsHandworkSend handworkSend = wmsHandworkSendBiz.getHandworkSendEntity(wmsHandworkSend.getId());
		if (BaseModel.INT_INIT.equals(handworkSend.getStatus())) {
			wmsHandworkSendBiz.deleteHandworkSend(wmsHandworkSend);
			return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不可以删除!");
		}
	}

	/**
	 * 附属单反馈
	 */
	@RequestMapping("/feedback.action")
	@ResponseBody
	@Lock
	public ResultResp feedback(HttpSession session, WmsHandworkSend wmsHandworkSend) {
		WmsHandworkSend handworkSend = wmsHandworkSendBiz.getHandworkSendEntity(wmsHandworkSend.getId());
		if (handworkSend.getStatus().equals(BaseModel.INT_COMPLETE)
				|| handworkSend.getStatus().equals(BaseModel.INT_RETURN)) {
			handworkSend.setOriginalNo(wmsHandworkSend.getOriginalNo());
			handworkSend.setRemark(wmsHandworkSend.getRemark());
			wmsHandworkSendBiz.feedbackHandworkSend(handworkSend);
			return new ResultResp(ResultResp.SUCCESS_CODE, "反馈成功!");
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "该单据没有完成,不能进行反馈操作");
		}
	}
}
