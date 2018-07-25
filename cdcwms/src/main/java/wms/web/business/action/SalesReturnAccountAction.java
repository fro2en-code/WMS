package wms.web.business.action;

import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.SalesReturnAccount;
import com.wms.business.SalesReturnAccountList;
import com.ymt.utils.Lock;

import wms.business.biz.SalesReturnAccountBiz;

@Controller
@RequestMapping("salesReturnAccount")
public class SalesReturnAccountAction extends BaseAction {
	@Resource
	private SalesReturnAccountBiz salesReturnAccountBiz;

	@RequestMapping("/toList.action")
	public String toList(HttpServletRequest req) {
		return "salesReturnAccountList";
	}

	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<SalesReturnAccount> pageData(HttpSession session, int rows, int page,
			SalesReturnAccount salesReturnAccount) {
		salesReturnAccount.setWhCode(getBindWhCode(session));
		return salesReturnAccountBiz.getPageData(rows, page, session, salesReturnAccount);
	}

	/**
	 * 退货
	 */
	@RequestMapping("/salesRrturn.action")
	@ResponseBody
	@Lock(type = Lock.LOCK_static, value = "SalesReturnAccountAction.salesRrturn")
	public ResultResp salesRrturn(HttpSession session, SalesReturnAccountList salesReturnAccountList) {
		salesReturnAccountList.setWhCode(getBindWhCode(session));
		return salesReturnAccountBiz.salesReturn(session, salesReturnAccountList);
	}

	/**
	 * 二次入库
	 */
	@RequestMapping("/again.action")
	@ResponseBody
	@Lock()
	public ResultResp again(HttpSession session, String id, String dealNum, String storageCode, String dealCode) {
		Pattern pattern = Pattern.compile("^([1-9][0-9]*){1,3}$");
		boolean flag = pattern.matcher(dealNum).matches();
		if (flag) {
			return salesReturnAccountBiz.again(session, id, Integer.parseInt(dealNum), storageCode, dealCode);
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "请输入正整数!");
		}
	}

	/**
	 * 供应商退货
	 */
	@RequestMapping("/supplierSalesReturn.action")
	@ResponseBody
	@Lock()
	public ResultResp supplierSalesReturn(HttpSession session, String id, String dealNum, String dealCode) {
		Pattern pattern = Pattern.compile("^([1-9][0-9]*){1,3}$");
		boolean flag = pattern.matcher(dealNum).matches();
		if (flag) {
			return salesReturnAccountBiz.supplierSalesReturn(session, id, Integer.parseInt(dealNum), dealCode);
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "请输入正整数!");
		}

	}
}
