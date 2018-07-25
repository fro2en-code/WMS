package wms.web.business.action;

import java.util.List;

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
import com.wms.business.WmsHandworkSend;
import com.wms.business.WmsHandworkSendList;

import wms.business.biz.WmsHandworkSendBiz;

/**
 * 手工发货单据明细
 *
 */
@Controller
@RequestMapping("/handworkSendList")
public class HandworkSendListAction extends BaseAction {
	@Resource
	private WmsHandworkSendBiz wmsHandworkSendBiz;

	/**
	 * 删除手工发货明细单
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp delHandwork(HttpSession session, WmsHandworkSendList wmsHandworkSendList) {
		WmsHandworkSendList handworkSendList = wmsHandworkSendBiz
				.getHandworkSendListEntity(wmsHandworkSendList.getId());
		handworkSendList.setWhCode(getBindWhCode(session));
		WmsHandworkSend handSend = wmsHandworkSendBiz.getInfo(handworkSendList.getMapSheetNo(),
				handworkSendList.getWhCode());
		if (BaseModel.INT_INIT.equals(handSend.getStatus())) {
			wmsHandworkSendBiz.deleteHandworkSendList(wmsHandworkSendList);
			return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不能删除!");
		}
	}

	/**
	 * 手工发货单据分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsHandworkSendList> list(HttpServletResponse resp, HttpSession session, int page, int rows,
			WmsHandworkSendList wmsHandworkSendList) {
		wmsHandworkSendList.setWhCode(getBindWhCode(session));
		return wmsHandworkSendBiz.getPageDataHandworkSendList(page, rows, wmsHandworkSendList);
	}

	/**
	 * 新增,修改手工发货明细单
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp saveHandwork(HttpSession session, WmsHandworkSendList wmsHandworkSendList) {
		validata(wmsHandworkSendList);
		wmsHandworkSendList.setWhCode(getBindWhCode(session));
		List<WmsHandworkSendList> list = wmsHandworkSendBiz.getWmsHandworkSendList(wmsHandworkSendList.getMapSheetNo(),
				wmsHandworkSendList.getWhCode());

		if (StringUtil.isEmpty(wmsHandworkSendList.getId())) {// 新增
			if (list.size() > 0) {// 获取明细单,如果供应商不同则不允许添加
				if (!list.get(0).getSupplNo().equals(wmsHandworkSendList.getSupplNo())) {
					return new ResultResp(ResultResp.ERROR_CODE, "一个发货单只能有一个供应商的零件,不允许有其他供应商的零件!");
				}
			}
			WmsHandworkSend handSend = wmsHandworkSendBiz.getInfo(wmsHandworkSendList.getMapSheetNo(),
					wmsHandworkSendList.getWhCode());
			if (BaseModel.INT_INIT.equals(handSend.getStatus())) {
				UserBean user = getUserInfo(session);
				wmsHandworkSendList.setUpdateUser(user.getLoginname());
				return wmsHandworkSendBiz.saveHandworkSendList(wmsHandworkSendList);
			} else {
				return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不能新增明细数据!");
			}
		} else {
			if (list.size() > 1) {// 获取明细单,如果供应商不同则不允许添加
				if (!list.get(0).getSupplNo().equals(wmsHandworkSendList.getSupplNo())) {
					return new ResultResp(ResultResp.ERROR_CODE, "一个发货单只能有一个供应商的零件,不允许有其他供应商的零件!");
				}
			}
			WmsHandworkSend handSend = wmsHandworkSendBiz.getInfo(wmsHandworkSendList.getMapSheetNo(),
					wmsHandworkSendList.getWhCode());
			if (BaseModel.INT_INIT.equals(handSend.getStatus())) {
				UserBean user = getUserInfo(session);
				wmsHandworkSendList.setUpdateUser(user.getLoginname());
				return wmsHandworkSendBiz.saveHandworkSendList(wmsHandworkSendList);
			} else {
				return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不能修改!");
			}
		}
	}

	@RequestMapping("/setReturn.action")
	@ResponseBody
	public ResultResp setReturn(WmsHandworkSendList wmsHandworkSendList) {
		wmsHandworkSendBiz.setHandworkSendListReturn(wmsHandworkSendList.getId(), wmsHandworkSendList.getReceiveQty());
		return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
	}

	private void validata(WmsHandworkSendList send) {
		if (StringUtils.isEmpty(send.getPartNo())) {
			throw new RuntimeException("零件号请在下拉框中选择");
		}
		if (StringUtils.isEmpty(send.getSupplNo())) {
			throw new RuntimeException("零件号请在下拉框中选择");
		}
	}
}
