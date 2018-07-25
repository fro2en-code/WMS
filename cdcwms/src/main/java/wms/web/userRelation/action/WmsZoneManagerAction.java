package wms.web.userRelation.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.rtzltech.user.page.PageData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.userRelation.UserWarehouse;
import com.wms.userRelation.ZoneManager;
import com.ymt.utils.UserUtils;

import wms.business.biz.UserBiz;
import wms.business.biz.WarehouseManagementBiz;

/**
 * 
 * 库区管理人
 */
@Controller
@RequestMapping("/zoneUser")
public class WmsZoneManagerAction extends BaseAction {
	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;

	/**
	 * 
	 * 渲染页面
	 */
	@RequestMapping(value = "/toList.action")
	public String toList() {
		return "zoneManager";
	}

	@Resource
	private UserUtils userUtils;
	@Resource
	private UserBiz userBiz;

	/**
	 * 
	 * 获取平台用户
	 * 
	 * 
	 * 
	 * @return
	 */
	@RequestMapping("/getUserList.action")
	@ResponseBody
	public PageData<UserWarehouse> getUserPageData(HttpServletResponse resp,
			HttpSession session, int page, int rows, UserWarehouse userWarehouse) {
		userWarehouse.setWhCode(getBindWhCode(session));
		return userBiz.getUserPageData(page, rows, userWarehouse);
	}

	/**
	 * 
	 * 获取用户绑定的库区列表
	 * 
	 * 
	 * 
	 * @return
	 */
	@RequestMapping("/getPageZone.action")
	@ResponseBody
	public PageData<ZoneManager> getPageZone(HttpServletResponse resp,
			HttpSession session, int page, int rows, String userLoginname) {
		String whCode = getBindWhCode(session);
		return warehouseManagementBiz.getPageData(page, rows, userLoginname,
				whCode);
	}

	private void validata(ZoneManager zoneManger) {
		if (StringUtils.isEmpty(zoneManger.getGroupid())) {
			zoneManger.setGroupid(null);
		}
	}

	/**
	 * 
	 * 保存库区绑定
	 * 
	 * 
	 * 
	 * @return
	 */
	@RequestMapping("/save.action")
	@ResponseBody
	public ResultResp save(HttpServletResponse resp, HttpSession session,
			ZoneManager zoneManger) {
		validata(zoneManger);
		String whCode = getBindWhCode(session);
		zoneManger.setWhcode(whCode);
		UserBean ub = getUserInfo(session);
		zoneManger.setUpdateUser(ub.getLoginname());// 插入当前登陆用户名

		zoneManger.setUpdateTime(StringUtil
				.getCurStringDate("yyyy-MM-dd HH:mm:ss"));// 插入当前时间

		return warehouseManagementBiz.save(zoneManger);
	}

	/**
	 * 
	 * 删除库区绑定
	 * 
	 * 
	 * 
	 * @return
	 */
	@RequestMapping("/del.action")
	@ResponseBody
	public ResultResp del(HttpServletResponse resp, HttpSession session,
			String id) {
		return warehouseManagementBiz.delZoneManager(id);
	}

}