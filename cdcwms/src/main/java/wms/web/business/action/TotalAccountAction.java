package wms.web.business.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import com.plat.common.utils.ExcelUtil.Excel;
import com.wms.business.WmsAccount;
import com.wms.business.WmsTotalAccount;
import com.wms.userRelation.UserCompany;
import com.ymt.utils.ExcelUtils;
import com.ymt.utils.ExcelUtils.ExportExcelHelper;

import wms.business.biz.AccountBiz;
import wms.business.biz.UserBiz;

/**
 * 总账
 */
@Controller
@RequestMapping("/totalAccount")
public class TotalAccountAction extends BaseAction {
	@Resource
	private AccountBiz accountBiz;
	@Resource
	private UserBiz userBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList(HttpServletRequest req) {
		// 子表
		new Excel().setColumn("dealCode", "单据号").setColumn("gcode", "物料代码").setColumn("gname", "物料名称")
				.setColumn("gtype", "零件用途").setColumn("batchCode", "批次号").setColumn("oraCode", "供应商代码")
				.setColumn("oraName", "供应商名称").setColumn("whCode", "仓库代码").setColumn("whName", "仓库名称")
				.setColumn("accountType", "台账类型(0.出， 1.入)").setColumn("dealType", "流水类型(0.出库， 1.收货, 2.红冲)")
				.setColumn("dealNum", "流水数量").setColumn("zoneCode", "库区编码").setColumn("inTime", "入库时间")
				.setColumn("totalNum", "结余").setColumn("dealTime", "台账时间").setClassName(WmsAccount.class.getName())
				.setTempName("台账列表").Finish(req);
		req.setAttribute("keyNoList", req.getAttribute("keyNo"));
		// 主表
		new Excel().setColumn("gcode", "物料代码").setColumn("gname", "物料名称").setColumn("gtype", "零件用途")
				.setColumn("oraCode", "供应商代码").setColumn("oraName", "供应商名称").setColumn("whCode", "仓库代码")
				.setColumn("whName", "仓库名称").setColumn("totalNum", "库存数量").setClassName(WmsTotalAccount.class.getName())
				.setTempName("台账总账").Finish(req);
		return "accountList";
	}

	/**
	 * 查询总账表分页
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsTotalAccount> list(HttpSession session, int page, int rows, WmsTotalAccount totalaccount) {
		UserBean userBean = getUserInfo(session);
		totalaccount.setWhCode(getBindWhCode(session));
		if (StringUtils.isNotEmpty(totalaccount.getRemark()) && "1".equals(totalaccount.getRemark())) {// 供应商查询
			UserCompany userCompany = new UserCompany();
			userCompany.setUserLoginname(userBean.getLoginname());
			userCompany.setCompanyType("供应商");
			userCompany.setWhCode(getBindWhCode(session));
			List<UserCompany> list = userBiz.getUserCompany(userCompany);
			List<String> companys = new ArrayList<>();
			companys.add("");// 清加一个空字符串,防止因为没有供应商查出所有数据
			for (UserCompany userCompany2 : list) {
				companys.add(userCompany2.getCompanyName());
			}
			return accountBiz.getPageData(page, rows, totalaccount, companys.toArray(new String[companys.size()]));
		} else {
			return accountBiz.getPageData(page, rows, totalaccount);
		}
	}

	@RequestMapping("/export.action")
	@ResponseBody
	public void export(HttpServletResponse resp, HttpSession session, final String keyNo,
			final WmsTotalAccount totalaccount) {
		totalaccount.setWhCode(getBindWhCode(session));
		ExportExcelHelper helper = new ExcelUtils.ExportExcelHelper() {

			@Override
			public PageData<? extends BaseModel> getPageDate(int page, int size) throws Exception {
				return accountBiz.getPageData(page, ExcelUtils.pageSize, totalaccount);
			}

			@Override
			public String getKey() {
				return keyNo;
			}
		};
		helper.run(resp);
	}

}
