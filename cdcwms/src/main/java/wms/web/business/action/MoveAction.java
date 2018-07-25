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
import com.wms.business.WmsMove;
import com.wms.business.WmsTask;
import com.ymt.utils.Lock;

import wms.business.biz.MoveBiz;
import wms.business.biz.TaskBiz;

/**
 * 移库申请单
 *
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/move")
public class MoveAction extends BaseAction {
	@Resource
	private MoveBiz moveBiz;
	@Resource
	private TaskBiz taskBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList() {
		return "moveList";
	}

	@RequestMapping("/addTask.action")
	@ResponseBody
	@Lock
	public ResultResp addTask(HttpSession session, String id, String whCode) {
		WmsMove wmsMove = moveBiz.getEntity(id);
		if (BaseModel.INT_INIT.equals(wmsMove.getStatu())) {
			UserBean user = getUserInfo(session);
			// 任务描述,单据ID,仓库代码,任务优先级,单据类型,(任务执行人,任务执行岗位 至少有一个)
			WmsTask task = new WmsTask();
			// 把移库表的id当做移库任务表的billid
			task.setBillid(id);
			task.setTaskdesc("移库-" + wmsMove.getUsedStorageCode());
			task.setLevel(0);
			task.setType(8);
			task.setWhCode(whCode);
			task.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
			task.setExecutorName(user.getLoginname());
			task.setStatus(0);
			return taskBiz.addTask(task);
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "该单据已生成任务!");
		}
	}

	/**
	 * 库存调整申请单分页
	 *
	 * @return
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsMove> list(HttpServletResponse resp, HttpSession session, int page, int rows, WmsMove wmsMove) {
		String whCode = getBindWhCode(session);
		wmsMove.setWhCode(whCode);
		return moveBiz.getPageDataMove(page, rows, wmsMove);
	}

	private void validata(WmsMove wmsMove) {
		if (StringUtils.isEmpty(wmsMove.getGcode())) {
			throw new RuntimeException("零件请从下拉框中选取");
		}
		if (StringUtils.isEmpty(wmsMove.getGtype())) {
			throw new RuntimeException("零件请从下拉框中选取");
		}
		if (StringUtils.isEmpty(wmsMove.getSupCode())) {
			throw new RuntimeException("零件请从下拉框中选取");
		}
	}

	/**
	 * 新增,修改
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ResultResp save(HttpServletResponse resp, HttpSession session, WmsMove wmsMove) {
		validata(wmsMove);
		if (StringUtil.isEmpty(wmsMove.getId())) {
			String whCode = getBindWhCode(session);
			wmsMove.setWhCode(whCode);
			UserBean user = getUserInfo(session);
			wmsMove.setUpdateUser(user.getLoginname());
			return moveBiz.saveMove(wmsMove);
		} else {
			WmsMove move = moveBiz.getEntity(wmsMove.getId());
			if (BaseModel.INT_INIT.equals(move.getStatu())) {
				String whCode = getBindWhCode(session);
				wmsMove.setWhCode(whCode);
				UserBean user = getUserInfo(session);
				wmsMove.setUpdateUser(user.getLoginname());
				return moveBiz.saveMove(wmsMove);
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
		return moveBiz.deleteMove(id);
	}

}
