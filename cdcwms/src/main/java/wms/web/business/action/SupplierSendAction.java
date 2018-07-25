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
import com.wms.business.WmsSupplierSend;
import com.wms.business.WmsSupplierSendList;
import com.wms.userRelation.UserCompany;
import com.ymt.utils.Lock;

import wms.business.biz.UserBiz;
import wms.business.biz.WmsHandworkReceiveBiz;
import wms.business.biz.WmsSupplierSendBiz;

/**
 * 供应商发货单管理
 *
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/supplierSend")
public class SupplierSendAction extends BaseAction {
	@Resource
	private WmsSupplierSendBiz wmsSupplierSendBiz;
	@Resource
	private UserBiz userBiz;
	@Resource
	private WmsHandworkReceiveBiz wmsHandworkReceiveBiz;

	@RequestMapping("/toList.action")
	public String toList() {
		return "supplierSendList";
	}

	/**
	 * 供应商发货单分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsSupplierSend> list(HttpServletResponse resp, HttpSession session, int page, int rows,
			WmsSupplierSend supplierSend) {
		supplierSend.setWhCode(getBindWhCode(session));
		// 查询用户绑定的供应商
		UserBean userBean = getUserInfo(session);
		UserCompany userCompany = new UserCompany();
		userCompany.setUserLoginname(userBean.getLoginname());
		userCompany.setCompanyType("供应商");
		supplierSend.setUpdateUser(userBean.getLoginname());
		return wmsSupplierSendBiz.getPageDataSupplierSend(page, rows, supplierSend);
	}

	/**
	 * 保存,修改
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp save(HttpSession session, WmsSupplierSend supplierSend) {
		supplierSend.setWhCode(getBindWhCode(session));
		if (StringUtil.isEmpty(supplierSend.getId())) {// 新增
			// 查询用户绑定的供应商
			UserBean userBean = getUserInfo(session);
			UserCompany userCompany = new UserCompany();
			userCompany.setUserLoginname(userBean.getLoginname());
			userCompany.setCompanyType("供应商");
			userCompany.setWhCode(getBindWhCode(session));
			List<UserCompany> list = userBiz.getUserCompany(userCompany);
			if (list.size() == 0 || list == null) {
				return new ResultResp(ResultResp.ERROR_CODE, "该用户未绑定供应商!");
			}
			// 因为一个用户只有一个供应商,所以用get(0)
			supplierSend.setSupplNo(list.get(0).getCompanyid());
			supplierSend.setUpdateUser(userBean.getLoginname());
			return wmsSupplierSendBiz.saveWmsSupplierSend(supplierSend);
		} else {// 修改
			WmsSupplierSend wmsSupplierSend = wmsSupplierSendBiz.getWmsSupplierSendEntity(supplierSend.getId());
			if (wmsSupplierSend.getStatus().equals(BaseModel.INT_INIT)) {
				UserBean userBean = getUserInfo(session);
				supplierSend.setUpdateUser(userBean.getLoginname());
				return wmsSupplierSendBiz.saveWmsSupplierSend(supplierSend);
			} else {
				return new ResultResp(ResultResp.ERROR_CODE, "已发货完成不能修改!");
			}
		}

	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete.action")
	@ResponseBody
	public ResultResp delete(HttpSession session, WmsSupplierSend supplierSend) {
		WmsSupplierSend wmsSupplierSend = wmsSupplierSendBiz.getWmsSupplierSendEntity(supplierSend.getId());
		if (wmsSupplierSend.getStatus().equals(BaseModel.INT_INIT)) {
			wmsSupplierSendBiz.deleteSupplierSend(supplierSend);
			return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "单据已经发货完成,不能删除!");
		}
	}

	/**
	 * 发货完成,生成手工发收货单
	 */
	@RequestMapping("/complete.action")
	@ResponseBody
	@Lock
	public ResultResp complete(HttpSession session, WmsSupplierSend supplierSend) {
		ResultResp resp = new ResultResp();
		String whCode = getBindWhCode(session);
		WmsSupplierSend wmsSupplierSend = wmsSupplierSendBiz.getWmsSupplierSendEntity(supplierSend.getId());
		List<WmsSupplierSendList> wmsSupplierSendList = wmsSupplierSendBiz
				.getWmsSupplierSendList(wmsSupplierSend.getMapSheetNo(), whCode);
		if (wmsSupplierSendList.size() == 0) {
			return new ResultResp(ResultResp.ERROR_CODE, "此单据没有明细,无法发货!");
		}
		// 查询配送编号是否在手工收货单里面存在
		WmsHandworkReceive wmsHandworkReceive = wmsHandworkReceiveBiz
				.getReceiveByMapSheetNo(wmsSupplierSend.getMapSheetNo(), whCode);
		if (wmsHandworkReceive != null) {
			return new ResultResp(ResultResp.ERROR_CODE, "该配送编号在手工收货单里已存在,无法生成!");
		}
		if (wmsSupplierSend.getStatus().equals(BaseModel.INT_INIT)) {
			// 创建手工收货单
			WmsHandworkReceive handworkReceive = new WmsHandworkReceive();
			handworkReceive.setMapSheetNo(wmsSupplierSend.getMapSheetNo());
			handworkReceive.setIsEmerge(wmsSupplierSend.getIsEmerge());
			handworkReceive.setStatus(BaseModel.INT_INIT);
			handworkReceive.setWhCode(wmsSupplierSend.getWhCode());
			UserBean user = getUserInfo(session);
			handworkReceive.setUpdateUser(user.getLoginname());
			handworkReceive.setUpdateTime(StringUtil.getCurStringDate(StringUtil.PATTERN));

			// 创建手工收货单明细
			for (WmsSupplierSendList supplierSendList : wmsSupplierSendList) {
				WmsHandworkReceiveList wmsHandworkReceiveList = new WmsHandworkReceiveList();
				wmsHandworkReceiveList.setMapSheetNo(supplierSendList.getMapSheetNo());
				wmsHandworkReceiveList.setPartNo(supplierSendList.getPartNo());
				wmsHandworkReceiveList.setSupplNo(supplierSendList.getSupplNo());
				wmsHandworkReceiveList.setSxCardNo(supplierSendList.getSxCardNo());
				wmsHandworkReceiveList.setReqQty(supplierSendList.getReqQty());
				wmsHandworkReceiveList.setWhCode(supplierSendList.getWhCode());
				wmsHandworkReceiveList.setUpdateUser(user.getLoginname());
				wmsHandworkReceiveBiz.saveEntity(wmsHandworkReceiveList);
			}
			wmsHandworkReceiveBiz.saveEntity(handworkReceive);
			wmsSupplierSend.setStatus(BaseModel.INT_COMPLETE);
			wmsSupplierSendBiz.updateEntity(wmsSupplierSend);
			resp.setRetcode("0");
			resp.setRetmsg("新增发货明细单成功！");
		} else {
			resp.setRetcode("-1");
			resp.setRetmsg("该单据已经发货完成,无法再次发货!");
		}
		return resp;
	}
}
