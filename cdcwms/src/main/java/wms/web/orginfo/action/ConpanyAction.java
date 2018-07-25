
package wms.web.orginfo.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.ExcelUtil.Excel;
import com.wms.orginfo.OrgConpany;

import wms.business.biz.CompanyBiz;

/**
 * 公司管理
 */
@Controller
@RequestMapping("/conpany")
public class ConpanyAction extends BaseAction {

	@Resource
	private CompanyBiz conpanyBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		new Excel().setColumn("companyid", "公司编码").setColumn("name", "公司名称").setColumn("contacter", "联系人")
				.setColumn("address", "公司地址").setColumn("longitude", "gps经度").setColumn("latitude", "gps纬度")
				.setColumn("telphone", "联系电话").setColumn("mobile", "手机号码").setColumn("companyemail", "公司电子邮箱")
				.setColumn("type", "公司类型(运行公司  供应商 承运商  业务中心)").setColumn("whCode", "仓库代码").setTempName("公司管理导入模板")
				.setClassName(OrgConpany.class.getName()).Finish(request);
		return "conpanyList";
	}

	/**
	 * 根据公司类型查询下拉公司
	 *
	 * @return
	 */
	@RequestMapping("getComboboxByConCode.action")
	@ResponseBody
	public List<Map<String, String>> getComboboxByConCode(HttpSession session, String q) {
		String whCode = getBindWhCode(session);
		return conpanyBiz.getComboboxByConCode(q, whCode);
	}

	/**
	 * 查询公司管理分页
	 *
	 * @return
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<OrgConpany> list(HttpSession session, int page, int rows, OrgConpany conpany) {
		String whCode = getBindWhCode(session);
		conpany.setWhCode(whCode);
		return conpanyBiz.getPageData(page, rows, conpany);
	}

	/**
	 * 保存、修改公司
	 *
	 * @return
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ResultResp saveRoleMenu(HttpSession session, OrgConpany conpany) {
		UserBean user = getUserInfo(session);
		String whCode = getBindWhCode(session);
		conpany.setUpdateUser(user.getLoginname());
		conpany.setWhCode(whCode);
		return conpanyBiz.save(conpany);
	}

	/**
	 * 删除公司
	 *
	 * @return
	 */
	@RequestMapping("del.action")
	@ResponseBody
	public ResultResp del(HttpServletResponse resp, String id) {
		return conpanyBiz.del(id);
	}

}
