package wms.web.business.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import wms.business.biz.WarningBiz;

import com.plat.common.action.BaseAction;
import com.plat.common.page.PageData;

@Controller
@RequestMapping("/warningStock")
public class WarningStockAction extends BaseAction {
	@Resource
	WarningBiz warningBiz;

	@RequestMapping("/toList.action")
	public String toList(HttpServletResponse resp) {
		return "warningStockList";
	}

	@RequestMapping(value = "/list.action")
	@ResponseBody
	public PageData<Map<String, Object>> list(int page, int rows, String G_code,
			String G_NAME, String G_TYPE, String ora_code, String ora_name,
			String warning_max_num, String warning_min_num, HttpSession session) {
		return warningBiz.queryWarningStock(page, rows, G_code, G_NAME, G_TYPE,
				ora_code, ora_name, warning_max_num, warning_min_num, getBindWhCode(session));
	}

}
