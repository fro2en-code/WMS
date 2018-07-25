package com.ymt.utils;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.plat.common.utils.StringUtil;
import com.wms.userRelation.UserSessionID;
import com.wms.warehouse.WmsDock;

import wms.business.biz.UserBiz;
import wms.business.biz.WarehouseManagementBiz;

@Service("dockUtils")
public class DockUtils {
	@Resource
	private UserBiz userBiz;

	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;

	public WmsDock getWmsDockByTag(String tagId) {
		return warehouseManagementBiz.getWmsDockByTag(tagId);
	}

	/**
	 * 绑写收货道口
	 */
	public void bindWmsDock(String dockCode, String loginName) {
		UserSessionID userSession = userBiz.getUserInfo(loginName);
		userSession.setDockCode(dockCode);
		userBiz.update(userSession);
	}

	/**
	 * 绑写收货道口
	 *
	 * @return
	 */
	public boolean defaultBindWmsDock(HttpSession session, String loginName, String whCode) {
		List<WmsDock> list = warehouseManagementBiz.getAllDock(whCode);
		if (list.size() == 1) {
			WmsDock dock = list.get(0);
			UserSessionID userSession = userBiz.getUserInfo(loginName);
			userSession.setDockCode(dock.getDockCode());
			session.setAttribute(StringUtil.DEFAULT_DOCK, dock.getDockCode());
			userBiz.update(userSession);
			return true;
		} else if (list.size() == 1) {
			session.setAttribute("isShow", false);
			return false;
		} else {
			session.removeAttribute("isShow");
			return false;
		}
	}

	/**
	 * 返回收货道口对应的收货区
	 */
	public String getStorage(String loginName, String whCode) {
		UserSessionID userSession = userBiz.getUserInfo(loginName);
		WmsDock wmsDock = warehouseManagementBiz.getWmsDockByDockCode(userSession.getDockCode(), whCode);
		if (null != wmsDock) {
			return wmsDock.getZoneCode();
		} else {
			return null;
		}
	}
}
