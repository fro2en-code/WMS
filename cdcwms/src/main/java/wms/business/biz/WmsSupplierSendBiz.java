package wms.business.biz;

import java.io.Serializable;
import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsSupplierSend;
import com.wms.business.WmsSupplierSendList;

public interface WmsSupplierSendBiz {
	WmsSupplierSend getWmsSupplierSendEntity(Serializable id);

	List<WmsSupplierSendList> getWmsSupplierSendList(String mapSheetNo, String whCode);

	/**
	 * 删除供应商发货单据
	 */
	void deleteSupplierSend(WmsSupplierSend supplierSend);

	/**
	 * 保存,修改供应商发货单据
	 */
	ResultResp saveWmsSupplierSend(WmsSupplierSend supplierSend);

	/**
	 * 保存,修改供应商发货单明细
	 */
	ResultResp saveWmsSupplierSendList(WmsSupplierSendList supplierSendList);

	/**
	 * 删除供应商发货单明细
	 */
	ResultResp deleteSupplierSendList(WmsSupplierSendList supplierSendList);

	/**
	 * 供应商发货单分页查询
	 */
	PageData<WmsSupplierSend> getPageDataSupplierSend(int page, int rows, WmsSupplierSend supplierSend);

	/**
	 * 供应商发货单明细分页查询
	 */
	PageData<WmsSupplierSendList> getPageDataSupplierSendList(int page, int rows, WmsSupplierSendList supplierSendList);

	public void updateEntity(WmsSupplierSend supplierSend);

	/**
	 * 根据配送单号查询
	 */
	WmsSupplierSend getWmsSupplierSendByMapSheetNo(String mapSheetNo, String whCode);

	public WmsSupplierSendList getEntity(String id);
}
