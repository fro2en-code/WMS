package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.TaskPrePick;
import com.wms.business.WmsTask;
import com.ymt.utils.Lock;

import wms.business.biz.TaskBiz;
import wms.business.biz.TaskPrePickBiz;

/**
 * 预拣货任务定义
 *
 * @author wangzz
 *
 */
@Controller
@RequestMapping("/taskPrePick")
public class TaskPrePickAction extends BaseAction {
	@Resource
	private TaskPrePickBiz taskPrePickBiz;
	@Resource
	private TaskBiz taskBiz;

	@RequestMapping("/addTask.action")
	@ResponseBody
	@Lock("billId")
	public ResultResp addTask(String billId) {
		WmsTask task = taskPrePickBiz.createDefaultTaskByBillID(billId);
		return taskBiz.addTask(task);
	}

	/**
	 * 删除预拣货任务
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp delPick(HttpSession session, TaskPrePick taskPrePick) throws Exception {
		taskPrePickBiz.delPick(taskPrePick);
		return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
	}

	/**
	 * 预拣货任务分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<TaskPrePick> list(HttpServletResponse resp, HttpSession session, int page, int rows,
			TaskPrePick taskPrePick) {
		taskPrePick.setWhCode(getBindWhCode(session));
		return taskPrePickBiz.getPageDataPick(page, rows, taskPrePick);
	}

	private void validata(TaskPrePick taskPrePick) {
		if (StringUtils.isEmpty(taskPrePick.getGcode())) {
			throw new RuntimeException("零件请从下拉框中选取");
		}
		if (StringUtils.isEmpty(taskPrePick.getOraCode())) {
			throw new RuntimeException("零件请从下拉框中选取");
		}
		if (StringUtils.isEmpty(taskPrePick.getGtype())) {
			throw new RuntimeException("零件请从下拉框中选取");
		}
	}

	/**
	 * 保存,修改预拣货任务
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp savePick(HttpSession session, TaskPrePick taskPrePick) throws Exception {
		validata(taskPrePick);
		taskPrePick.setWhCode((String) session.getAttribute("defaultWhCode"));
		taskPrePick.setWhName((String) session.getAttribute("defaultWhName"));
		UserBean user = getUserInfo(session);
		taskPrePick.setInsertUser(user.getLoginname());
		return taskPrePickBiz.savePick(taskPrePick);
	}

	@RequestMapping("/toList.action")
	public String toList() {
		return "taskPrePickList";
	}
}
