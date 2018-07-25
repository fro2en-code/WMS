package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.ExcelUtil.Excel;
import com.plat.common.utils.StringUtil;
import com.wms.business.AccountInit;
import com.wms.business.AccountInitList;
import com.ymt.utils.Lock;

import wms.business.biz.AccountBiz;

/**
 * 台账初始化主表
 */
@Controller
@RequestMapping("/accountInit")
public class AccountInitAction extends BaseAction {

	@Resource
	private AccountBiz accountBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList(HttpServletRequest request) {
		new Excel().setColumn("initCode", "初始化单号").setColumn("whCode", "仓库代码").setColumn("whName", "仓库名称")
				.setColumn("zoneCode", "库区代码").setColumn("storageCode", "库位代码").setColumn("gcode", "物料代码")
				.setColumn("gname", "物料名称").setColumn("gtype", "物料用途(生产件,备件,出口件)").setColumn("oraCode", "客户代码")
				.setColumn("oraName", "客户名称").setColumn("initNum", "初始化数量 ")
				.setClassName(AccountInitList.class.getName()).setTempName("初始化数据导入模版").Finish(request);
		return "accountInitList";
	}

	/**
	 * 查询台账初始化主表分页
	 *
	 * @return
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<AccountInit> list(HttpSession session, int page, int rows, AccountInit account) {
		account.setWhCode(getBindWhCode(session));
		return accountBiz.getPageData(page, rows, account);
	}

	/**
	 * 保存、修改 台账初始化主表
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ResultResp saveRoleMenu(HttpSession session, AccountInit accountInit) {
		UserBean user = getUserInfo(session);
		accountInit.setWkCode(user.getLoginname());
		accountInit.setWhCode((String) session.getAttribute("defaultWhCode"));
		accountInit.setWhName((String) session.getAttribute("defaultWhName"));
		accountInit.setUpdateUser(user.getLoginname());
		accountInit.setInsertUser(user.getLoginname());
		return accountBiz.saveAccountInit(accountInit);
	}

	/**
	 * 删除 台账初始化单据
	 *
	 * @return
	 */
	@RequestMapping("del.action")
	@ResponseBody
	public ResultResp del(AccountInit accountInit) {
		return accountBiz.delAccountInit(accountInit);
	}

	/**
	 * 确认台账初始化单据
	 *
	 * @return
	 */
	@RequestMapping("sure.action")
	@ResponseBody
	@Lock
	public ResultResp sure(HttpSession session, AccountInit accountInit) {
		UserBean userBean = getUserInfo(session);
		accountInit.setUpdateUser(userBean.getLoginname());
		accountInit.setUpdateTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
		return accountBiz.updateSureAccountInit(accountInit);
	}

}
