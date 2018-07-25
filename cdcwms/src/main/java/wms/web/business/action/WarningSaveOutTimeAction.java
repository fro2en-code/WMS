package wms.web.business.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.page.PageData;
import wms.business.biz.WarningBiz;

@Controller
@RequestMapping("/warningSaveOutTime")
public class WarningSaveOutTimeAction extends BaseAction {

	@Resource
	private WarningBiz warningBiz;

	@RequestMapping("/toList.action")
	public String toList(HttpServletResponse resp) {
		return "warningSaveOutTime";
	}

	@RequestMapping(value = "/list.action")
	@ResponseBody
	public PageData<Map<String, Object>> list(int page, int rows,
			String sup_code, String Sup_name, String G_code, String g_name,
			String G_TYPE, HttpSession session) {
		return warningBiz.queryWarningSaveOutTime(page, rows, sup_code,
				Sup_name, G_code, g_name, G_TYPE, getBindWhCode(session));
	}

}
