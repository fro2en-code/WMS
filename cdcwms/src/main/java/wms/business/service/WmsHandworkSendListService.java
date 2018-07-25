package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsHandworkSendList;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsHandworkSendListService extends BaseService<WmsHandworkSendList> {

	/**
	 * 删除
	 */
	public ResultResp del(WmsHandworkSendList wmsHandworkSendList);

	/**
	 * 分页查询
	 */
	PageData<WmsHandworkSendList> getPageDataList(int page, int rows, WmsHandworkSendList wmsHandworkSendList);

	/**
	 * 根据billId查询
	 */
	List<WmsHandworkSendList> getWmsHandworkSendList(String mapSheetNo, String whCode);

	/**
	 * 保存,修改
	 */
	public ResultResp save(WmsHandworkSendList wmsHandworkSendList);

	/**
	 * 根据配送单号和零件信息查询指定的记录
	 */
	List<WmsHandworkSendList> getInfo(String mapSheetNo, String gcode, String whCode);
}
