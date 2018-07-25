package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import wms.business.biz.SystemConfigBiz;
import com.plat.common.action.BaseAction;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.systemConfig.SystemConfig;

@Controller
@RequestMapping("/systemConfig")
public class SystemConfigAction extends BaseAction {
	@Resource
	private SystemConfigBiz systemConfigBiz;

	@RequestMapping(value = "/toList.action")
	public String toList(HttpServletResponse resp) {
		return "systemConfig";
	}

	@RequestMapping("/getPage.action")
	@ResponseBody
	public PageData<SystemConfig> getPage(HttpSession session, int page,
			int rows, SystemConfig systemConfig) {
		systemConfig.setWhCode(getBindWhCode(session));
		return systemConfigBiz.getPageData(page, rows, systemConfig);
	}

	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp del(HttpServletResponse resp, String id) {
		return systemConfigBiz.del(id);
	}

	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp save(HttpSession session, SystemConfig systemConfig) {
		systemConfig.setWhCode(getBindWhCode(session));
		return systemConfigBiz.save(systemConfig);
	}

}
