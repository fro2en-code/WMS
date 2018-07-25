package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.page.PageData;
import com.wms.business.SalesReturnAccountList;

import wms.business.biz.SalesReturnAccountBiz;

@Controller
@RequestMapping("salesReturnAccountList")
public class SalesReturnAccountListAction extends BaseAction {
	@Resource
	private SalesReturnAccountBiz salesReturnAccountBiz;

	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<SalesReturnAccountList> pageData(HttpSession session, int rows, int page,
			SalesReturnAccountList salesReturnAccountList) {
		salesReturnAccountList.setWhCode(getBindWhCode(session));

		return salesReturnAccountBiz.getPageData(rows, page, session, salesReturnAccountList);
	}
}
