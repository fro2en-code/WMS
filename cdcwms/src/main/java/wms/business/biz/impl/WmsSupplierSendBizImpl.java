package wms.business.biz.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsSupplierSend;
import com.wms.business.WmsSupplierSendList;

import wms.business.biz.WmsSupplierSendBiz;
import wms.business.service.WmsSupplierSendListService;
import wms.business.service.WmsSupplierSendService;

@Service("wmsSupplierSendBiz")
public class WmsSupplierSendBizImpl implements WmsSupplierSendBiz {
	@Resource
	private WmsSupplierSendService wmsSupplierSendService;
	@Resource
	private WmsSupplierSendListService wmsSupplierSendListService;

	@Override
	public ResultResp saveWmsSupplierSend(WmsSupplierSend supplierSend) {
		return wmsSupplierSendService.save(supplierSend);
	}

	@Override
	public void deleteSupplierSend(WmsSupplierSend supplierSend) {
		wmsSupplierSendListService.delByMapSheetNo(supplierSend.getMapSheetNo(), supplierSend.getWhCode());
		wmsSupplierSendService.deleteEntity(supplierSend);
	}

	@Override
	public PageData<WmsSupplierSendList> getPageDataSupplierSendList(int page, int rows,
			WmsSupplierSendList supplierSendList) {
		return wmsSupplierSendListService.getPageDataList(page, rows, supplierSendList);
	}

	@Override
	public ResultResp saveWmsSupplierSendList(WmsSupplierSendList supplierSendList) {
		return wmsSupplierSendListService.save(supplierSendList);
	}

	@Override
	public ResultResp deleteSupplierSendList(WmsSupplierSendList supplierSendList) {
		return wmsSupplierSendListService.delete(supplierSendList);
	}

	@Override
	public WmsSupplierSend getWmsSupplierSendEntity(Serializable id) {
		return wmsSupplierSendService.getEntity(id);
	}

	@Override
	public List<WmsSupplierSendList> getWmsSupplierSendList(String mapSheetNo, String whCode) {
		return wmsSupplierSendListService.getWmsSupplierSendList(mapSheetNo, whCode);
	}

	@Override
	public PageData<WmsSupplierSend> getPageDataSupplierSend(int page, int rows, WmsSupplierSend supplierSend) {
		return wmsSupplierSendService.getPageDataList(page, rows, supplierSend);
	}

	@Override
	public void updateEntity(WmsSupplierSend supplierSend) {
		wmsSupplierSendService.updateEntity(supplierSend);

	}

	@Override
	public WmsSupplierSend getWmsSupplierSendByMapSheetNo(String mapSheetNo, String whCode) {
		return (WmsSupplierSend) wmsSupplierSendService.getWmsSupplierSendByMapSheetNo(mapSheetNo, whCode);
	}

	@Override
	public WmsSupplierSendList getEntity(String id) {
		return wmsSupplierSendListService.getEntity(id);
	}
}
