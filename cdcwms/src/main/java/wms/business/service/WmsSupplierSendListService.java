package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsSupplierSendList;

import its.base.service.BaseService;

public interface WmsSupplierSendListService extends BaseService<WmsSupplierSendList> {

	public void delByMapSheetNo(String mapSheetNo, String whCode);

	public PageData<WmsSupplierSendList> getPageDataList(int page, int rows, WmsSupplierSendList wmsSupplierSendList);

	public ResultResp save(WmsSupplierSendList wmsSupplierSendList);

	public ResultResp delete(WmsSupplierSendList wmsSupplierSendList);

	List<WmsSupplierSendList> getWmsSupplierSendList(String mapSheetNo, String whCode);
}
