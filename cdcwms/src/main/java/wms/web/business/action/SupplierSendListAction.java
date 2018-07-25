package wms.web.business.action;

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
import com.wms.business.WmsSupplierSend;
import com.wms.business.WmsSupplierSendList;

import wms.business.biz.WmsSupplierSendBiz;

@Controller
@RequestMapping("/supplierSendList")
public class SupplierSendListAction extends BaseAction {

	@Resource
	private WmsSupplierSendBiz wmsSupplierSendBiz;

	/**
	 * 供应商发货单据明细分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsSupplierSendList> list(HttpSession session, int page, int rows,
			WmsSupplierSendList wmsSupplierSendList) {
		wmsSupplierSendList.setWhCode(getBindWhCode(session));
		return wmsSupplierSendBiz.getPageDataSupplierSendList(page, rows, wmsSupplierSendList);
	}

	private void validata(WmsSupplierSendList send) {
		if (StringUtils.isEmpty(send.getPartNo())) {
			throw new RuntimeException("零件号请在下拉框中选择");
		}
		if (StringUtils.isEmpty(send.getSupplNo())) {
			throw new RuntimeException("零件号请在下拉框中选择");
		}
	}

	/**
	 * 新增,修改供应商发货明细单
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp saveHandwork(HttpSession session, WmsSupplierSendList wmsSupplierSendList) {
		validata(wmsSupplierSendList);
		wmsSupplierSendList.setWhCode(getBindWhCode(session));
		WmsSupplierSend supplierSend = wmsSupplierSendBiz
				.getWmsSupplierSendByMapSheetNo(wmsSupplierSendList.getMapSheetNo(), wmsSupplierSendList.getWhCode());
		if (BaseModel.INT_INIT.equals(supplierSend.getStatus())) {
			UserBean user = getUserInfo(session);
			wmsSupplierSendList.setUpdateUser(user.getLoginname());
			return wmsSupplierSendBiz.saveWmsSupplierSendList(wmsSupplierSendList);
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "已发货完成不能对明细表数据进行新增或修改!");
		}
	}

	/**
	 * 删除供应商发货明细单
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp delHandwork(HttpSession session, WmsSupplierSendList wmsSupplierSendList) {
		WmsSupplierSendList supplierSendList = wmsSupplierSendBiz.getEntity(wmsSupplierSendList.getId());
		WmsSupplierSend supplierSend = wmsSupplierSendBiz
				.getWmsSupplierSendByMapSheetNo(supplierSendList.getMapSheetNo(), getBindWhCode(session));
		if (BaseModel.INT_INIT.equals(supplierSend.getStatus())
				|| BaseModel.INT_ERROR.equals(supplierSend.getStatus())) {
			wmsSupplierSendBiz.deleteSupplierSendList(supplierSendList);
			return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "已发货完成,不允许删除!");
		}
	}

	/**
	 * 发货完成,生成手工发收货单
	 */
}
