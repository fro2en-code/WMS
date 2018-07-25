package wms.business.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsLesReceive;

import its.base.service.BaseService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsLesReceiveService extends BaseService<WmsLesReceive> {

	/**
	 * 分页查询
	 */
	PageData<WmsLesReceive> getPageData(int page, int rows, WmsLesReceive wmsLesReceiveLES);

	/**
	*  
	*/
	PageData<WmsLesReceive> getpageDataLesReceive(int page, int rows, WmsLesReceive wmsLesReceive);

	/**
	 * 保存,修改Les收货单据
	 */
	public ResultResp save(WmsLesReceive wmsLesReceive);

	/**
	 * 删除Les收货单据
	 */
	public ResultResp del(WmsLesReceive wmsLesReceive);

	/**
	 * 更新Les收货单
	 */
	void updateLesReceive(WmsLesReceive wmsLesReceive);

	/**
	 * 根绝配送单号获取les收货单
	 */
	WmsLesReceive getLesReceiveByMapSheetNo(String mapSheetNo, String whCode);
}
