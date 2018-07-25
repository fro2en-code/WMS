package wms.web.userRelation.action;

import java.util.List;
import java.util.Map;

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
import com.wms.userRelation.UserCompany;

import wms.business.biz.CompanyBiz;
import wms.business.biz.UserBiz;
import wms.business.biz.WarehouseManagementBiz;

/**
 * 用户公司关系
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年3月16日
 */
@Controller
@RequestMapping("/userCompany")
public class UserCompanyAction extends BaseAction {
	@Resource
	private UserBiz userBiz;
	@Resource
	private CompanyBiz conpanyBiz;
	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping(value = "/toList.action")
	public String toList() {
		return "userCompanyList";
	}

	@RequestMapping("/getPage")
	@ResponseBody
	public PageData<UserCompany> getPage(HttpSession session, int page, int rows, UserCompany userCompany) {
		userCompany.setWhCode(getBindWhCode(session));
		return userBiz.getPageData(page, rows, userCompany);
	}

	@RequestMapping("/getUserCompany")
	@ResponseBody
	public List<UserCompany> getUserCompany(HttpSession session, UserCompany userCompany) {
		UserBean userBean = getUserInfo(session);
		userCompany.setUserLoginname(userBean.getLoginname());
		userCompany.setWhCode(getBindWhCode(session));
		return userBiz.getUserCompany(userCompany);
	}

	@RequestMapping("/del")
	@ResponseBody
	public ResultResp getDel(UserCompany userCompany) {
		userBiz.deleteEntity(userCompany);
		return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功");
	}

	private void validata(UserCompany userCompany) {
		if (StringUtils.isEmpty(userCompany.getCompanyid())) {
			throw new RuntimeException("公司请从下拉框中选取");
		}
		if (StringUtils.isEmpty(userCompany.getCompanyName())) {
			throw new RuntimeException("公司请从下拉框中选取");
		}
		if (StringUtils.isEmpty(userCompany.getCompanyType())) {
			throw new RuntimeException("公司请从下拉框中选取");
		}
	}

	@RequestMapping("/save")
	@ResponseBody
	public ResultResp getSave(HttpSession session, UserCompany userCompany) {
		validata(userCompany);
		userCompany.setWhCode(getBindWhCode(session));
		UserBean userBean = getUserInfo(session);
		List<UserCompany> company = userBiz.getCompanyByName(userCompany.getUserLoginname(), getBindWhCode(session));
		if (company.size() > 0) {
			throw new RuntimeException("一个用户只允许绑定一个公司");
		}
		userCompany.setInsertUser(userBean.getLoginname());
		return userBiz.save(userCompany);
	}

	/**
	 * 仓库下拉框
	 *
	 * @param resp
	 * @param id
	 * @return
	 */
	@RequestMapping("/getWarehouseCombobox.action")
	@ResponseBody
	public List<Map<String, String>> getWarehouseCombobox(HttpServletResponse resp, String id) {
		return warehouseManagementBiz.getCombobox();
	}
}
