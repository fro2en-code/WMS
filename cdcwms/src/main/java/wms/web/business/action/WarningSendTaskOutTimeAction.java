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
@RequestMapping("/warningSendTaskOutTime")
public class WarningSendTaskOutTimeAction extends BaseAction {
	@Resource
	WarningBiz warningBiz;

	@RequestMapping("/toList.action")
	public String toList(HttpServletResponse resp) {
		return "warningSendTaskOutTime";
	}

	@RequestMapping(value = "/list.action")
	@ResponseBody
	public PageData<Map<String, Object>> list(int page, int rows,
			String Taskdesc, Integer Status, String executorName, String Billid,
			Integer Type, HttpSession session) {
		return warningBiz.queryWarningSendTaskOutTime(page, rows, Taskdesc,
				Status, executorName, Billid, Type, getBindWhCode(session));
	}

}
