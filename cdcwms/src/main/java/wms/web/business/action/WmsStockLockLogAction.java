package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsStockLockLog;
import com.ymt.utils.Lock;

import wms.business.biz.WmsStockBiz;

@Controller
@RequestMapping("/wmsStockLockLog")
public class WmsStockLockLogAction extends BaseAction {
	@Resource
	private WmsStockBiz wmsStockBiz;

	@RequestMapping("/toList.action")
	public String toList() {
		return "WmsStockLockLogList";
	}

	/*
	 * 库存列表
	 */
	@RequestMapping(value = "/list.action")
	@ResponseBody
	public PageData<WmsStockLockLog> list(HttpSession session, int page, int rows, WmsStockLockLog bean) {
		bean.setWhCode(getBindWhCode(session));
		return wmsStockBiz.getPageData(page, rows, bean);
	}

	@RequestMapping(value = "/sure.action")
	@ResponseBody
	@Lock
	public ResultResp sure(HttpSession session, String id) {
		UserBean user = getUserInfo(session);
		wmsStockBiz.sure(id, user.getLoginname());
		return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
	}

	/**
	 * 新增,修改
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ResultResp save(HttpServletResponse resp, HttpSession session, WmsStockLockLog bean) {
		validataSave(bean);
		UserBean user = getUserInfo(session);
		bean.setWhCode(getBindWhCode(session));
		bean.setUpdateUser(user.getLoginname());
		wmsStockBiz.save(bean);
		return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
	}

	private void validataSave(WmsStockLockLog bean) {
		if ("".equals(bean.getId())) {
			bean.setId(null);
		}
		if ("".equals(bean.getState())) {
			bean.setState(null);
		}
		if ("".equals(bean.getStartTime())) {
			bean.setStartTime(null);
		}
		if ("".equals(bean.getEndTime())) {
			bean.setEndTime(null);
		}
		if ("".equals(bean.getOraName())) {
			bean.setOraName(null);
		}
		if (null == bean.getOraName()) {
			bean.setOraCode(null);
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
		wmsStockBiz.delWmsStockLockLog(id);
		return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
	}
}
