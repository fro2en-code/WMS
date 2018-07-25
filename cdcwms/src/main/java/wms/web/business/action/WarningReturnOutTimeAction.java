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
@RequestMapping("/warningReturnOutTime")
public class WarningReturnOutTimeAction extends BaseAction {
	@Resource
	WarningBiz warningBiz;

	@RequestMapping("/toList.action")
	public String toList(HttpServletResponse resp) {
		return "warningReturnOutTime";
	}

	@RequestMapping(value = "/list.action")
	@ResponseBody
	public PageData<Map<String, Object>> list(int page, int rows,
			String MAP_SHEET_NO, Long IS_EMERGE, String DELIVERY_REC_TYPE,
			String mriCreateTime, HttpSession session) {
		return warningBiz.queryWarningReturnOutTime(page, rows, MAP_SHEET_NO,
				IS_EMERGE, DELIVERY_REC_TYPE, mriCreateTime,
				getBindWhCode(session));
	}

}
