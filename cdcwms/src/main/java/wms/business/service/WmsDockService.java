package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.warehouse.WmsDock;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsDockService extends BaseService<WmsDock> {

	/**
	 *
	 * 月台分页查询
	 *
	 */
	PageData<WmsDock> getPageData(int page, int rows, String whCode);

	/**
	 *
	 * 保存、修改月台
	 *
	 */
	ResultResp save(WmsDock dock);

	/**
	 *
	 * 删除月台
	 *
	 */
	ResultResp del(String id);

	/**
	 * 分页查询
	 */
	PageData<WmsDock> getPageData(int page, int rows, WmsDock wmsDock);

	/**
	 * 根据月台号,查询收货道口
	 *
	 * @param dockCode
	 *            月台号
	 */
	WmsDock getWmsDockByDockCode(String dockCode, String whCode);

	WmsDock getWmsDockByTag(String tagId);

	/**
	 * 查询全部月台
	 */
	List<WmsDock> getAllDock(String whCode);
}
