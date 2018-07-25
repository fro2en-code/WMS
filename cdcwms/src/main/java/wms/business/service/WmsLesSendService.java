package wms.business.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsLesSend;
import its.base.service.BaseService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsLesSendService extends BaseService<WmsLesSend> {

	/**
	 * 分页查询
	 */
	PageData<WmsLesSend> getPageDataList(int page, int rows, WmsLesSend wmsLesSend);

	/**
	 * 根据id查询发货单
	 */
	WmsLesSend getLesSendList(String id);

	/**
	 * 保存,修改les发货单据
	 */
	public ResultResp save(WmsLesSend wmsLesSend);

	/**
	 * 删除les发货单据
	 */
	public ResultResp del(WmsLesSend wmsLesSend);

	/**
	 * 根据billid更改手工发货单状态
	 */
	ResultResp updateStatus(String billid);

	/**
	 * 根据mapSheetNo查询主表
	 */
	WmsLesSend getInfo(String mapSheetNo, String whCode);
}
