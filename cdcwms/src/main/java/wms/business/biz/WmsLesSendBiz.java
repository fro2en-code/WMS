package wms.business.biz;

import java.io.Serializable;
import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsLesSend;
import com.wms.business.WmsLesSendList;
import com.wms.business.WmsTask;

public interface WmsLesSendBiz {

	WmsTask createDefaultTaskByBillID(String billId);

	/**
	 * 删除les发货单据
	 */
	public void deleteLesSend(WmsLesSend wmsLesSend);

	/**
	 * 删除les发货明细单
	 */
	public ResultResp deleteLesSendList(WmsLesSendList wmsLesSendList);

	WmsLesSend getInfo(String mapSheetNo, String whCode);

	PageData<WmsLesSend> getPageDataLesSend(int page, int rows, WmsLesSend wmsLesSend);

	PageData<WmsLesSendList> getPageDataLesSendList(int page, int rows, WmsLesSendList wmsLesSendList);

	WmsLesSend getWmsLesSendEntity(Serializable id);

	WmsLesSendList getWmsLesSendListEntity(Serializable id);

	/**
	 * 保存,修改les发货单据
	 */
	public ResultResp saveLesSend(WmsLesSend wmsLesSend);

	/**
	 * 保存,修改les发货单据,及明细
	 */
	ResultResp saveLesSendBill(WmsLesSend wmsLesSend);

	/**
	 * 保存,修改les发货明细单
	 */
	public ResultResp saveLesSendList(WmsLesSendList wmsLesSendList);

	/**
	 * 设置LES 回单
	 */
	ResultResp setLesSendListReturn(String id, int receiveQty);

	/**
	 * 设置LES 回单
	 */
	ResultResp setLesSendReturn(String id);

	void updateWmsLesSendEntity(WmsLesSend bean);

	List<WmsLesSendList> getWmsLesSendList(String mapSheetNo, String whCode);
}
