package wms.web.business.action;

import java.util.List;

import javax.annotation.Resource;
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
import com.wms.business.WmsLesSend;
import com.wms.business.WmsLesSendList;

import wms.business.biz.WmsLesSendBiz;

/**
 * les发货单据明细
 *
 */
@Controller
@RequestMapping("/lesSendList")
public class LesSendListAction extends BaseAction {
	@Resource
	private WmsLesSendBiz wmsLesSendBiz;

	/**
	 * les发货单据分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsLesSendList> list(HttpSession session, int page, int rows, WmsLesSendList wmsLesSendList) {
		wmsLesSendList.setWhCode(getBindWhCode(session));
		return wmsLesSendBiz.getPageDataLesSendList(page, rows, wmsLesSendList);
	}

	private void validata(WmsLesSendList send) {
		if (StringUtils.isEmpty(send.getPartNo())) {
			throw new RuntimeException("零件号请在下拉框中选择");
		}
		if (StringUtils.isEmpty(send.getSupplNo())) {
			throw new RuntimeException("零件号请在下拉框中选择");
		}
	}

	/**
	 * 新增,修改les发货明细单
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp saveHandwork(HttpSession session, WmsLesSendList wmsLesSendList) {
		validata(wmsLesSendList);
		wmsLesSendList.setWhCode(getBindWhCode(session));
		List<WmsLesSendList> list = wmsLesSendBiz.getWmsLesSendList(wmsLesSendList.getMapSheetNo(),
				wmsLesSendList.getWhCode());
		if (StringUtil.isEmpty(wmsLesSendList.getId())) {// 新增
			if (list.size() > 0) {// 获取明细单,如果供应商不同则不允许添加
				if (!list.get(0).getSupplNo().equals(wmsLesSendList.getSupplNo())) {
					return new ResultResp(ResultResp.ERROR_CODE, "一个发货单只能有一个供应商的零件,不允许有其他供应商的零件!");
				}
			}
			WmsLesSend lesSend = wmsLesSendBiz.getInfo(wmsLesSendList.getMapSheetNo(), wmsLesSendList.getWhCode());
			if (BaseModel.INT_INIT.equals(lesSend.getStatus())) {
				UserBean user = getUserInfo(session);
				wmsLesSendList.setUpdateUser(user.getLoginname());
				return wmsLesSendBiz.saveLesSendList(wmsLesSendList);
			} else {
				return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不能新增明细数据!");
			}
		} else {
			if (list.size() > 1) {// 获取明细单,如果供应商不同则不允许添加
				if (!list.get(0).getSupplNo().equals(wmsLesSendList.getSupplNo())) {
					return new ResultResp(ResultResp.ERROR_CODE, "一个发货单只能有一个供应商的零件,不允许有其他供应商的零件!");
				}
			}
			WmsLesSend lesSend = wmsLesSendBiz.getInfo(wmsLesSendList.getMapSheetNo(), wmsLesSendList.getWhCode());
			if (BaseModel.INT_INIT.equals(lesSend.getStatus())) {
				UserBean user = getUserInfo(session);
				wmsLesSendList.setUpdateUser(user.getLoginname());
				return wmsLesSendBiz.saveLesSendList(wmsLesSendList);
			} else {
				return new ResultResp(ResultResp.ERROR_CODE, "已创建任务不能修改!");
			}
		}
	}

	/**
	 * 删除les发货明细单
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp delHandwork(HttpSession session, WmsLesSendList wmsLesSendList) {
		WmsLesSendList lesSendList = wmsLesSendBiz.getWmsLesSendListEntity(wmsLesSendList.getId());
		WmsLesSend lesSend = wmsLesSendBiz.getInfo(lesSendList.getMapSheetNo(), getBindWhCode(session));
		if (BaseModel.INT_INIT.equals(lesSend.getStatus()) || BaseModel.INT_ERROR.equals(lesSend.getStatus())) {
			wmsLesSendBiz.deleteLesSendList(wmsLesSendList);
			return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "已生成过任务,不允许删除!");
		}
	}

	@RequestMapping("/setReturn.action")
	@ResponseBody
	public ResultResp setReturn(WmsLesSendList wmsLesSendList) {
		wmsLesSendBiz.setLesSendListReturn(wmsLesSendList.getId(), wmsLesSendList.getReceiveQty());
		return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
	}
}
