package wms.business.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsHandworkSend;

import its.base.service.BaseService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsHandworkSendService extends BaseService<WmsHandworkSend> {

	/**
	 * 分页查询
	 */
	PageData<WmsHandworkSend> getPageDataList(int page, int rows, WmsHandworkSend WmsHandworkSend);

	/**
	 * 保存,修改手工发货单据
	 */
	public ResultResp save(WmsHandworkSend WmsHandworkSend);

	/**
	 * 删除手工发货单据
	 */
	public ResultResp del(WmsHandworkSend WmsHandworkSend);

	/**
	 * 根据billid更改手工发货单状态
	 */
	ResultResp updateStatus(String billid);

	/**
	 * 根据配送单号查询主表
	 */
	WmsHandworkSend getInfo(String mapSheetNo, String whCode);
}
