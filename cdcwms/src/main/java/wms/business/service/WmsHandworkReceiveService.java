package wms.business.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsHandworkReceive;

import its.base.service.BaseService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsHandworkReceiveService extends BaseService<WmsHandworkReceive> {

	/**
	 * 分页查询
	 */
	PageData<WmsHandworkReceive> getPageData(int page, int rows, WmsHandworkReceive wmsHandworkReceive);

	/**
	 * 手工收货单据分页
	 * 
	 * @param page
	 * @param rows
	 * @param wmsHandworkReceive
	 * @return
	 */
	PageData<WmsHandworkReceive> getPageDataList(int page, int rows, WmsHandworkReceive wmsHandworkReceive);

	/**
	 * 保存,修改手工收货单据
	 */
	public ResultResp save(WmsHandworkReceive wmsHandworkReceive);

	/**
	 * 删除手工收货单据
	 */
	public ResultResp del(WmsHandworkReceive wmsHandworkReceive);

	/**
	 * 更新手工收货单
	 */
	void updateHandworkReceive(WmsHandworkReceive wmsHandworkReceive);

	/**
	 * 通过配送单号获取收货单信息
	 */
	WmsHandworkReceive getReceiveByMapSheetNo(String mapSheetNo, String whCode);
}
