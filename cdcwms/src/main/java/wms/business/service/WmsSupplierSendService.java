package wms.business.service;


import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsSupplierSend;

import its.base.service.BaseService;

public interface WmsSupplierSendService extends BaseService<WmsSupplierSend> {
	/**
	 * 分页查询
	 */
	PageData<WmsSupplierSend> getPageDataList(int page, int rows, WmsSupplierSend supplierSend);

	/**
	 * 保存,修改供应商发货单据
	 */
	public ResultResp save(WmsSupplierSend supplierSend);

	/**
	 * 根据配送单号查询
	 */
	WmsSupplierSend getWmsSupplierSendByMapSheetNo(String mapSheetNo, String whCode);
}
