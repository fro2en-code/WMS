package wms.business.service;

import com.plat.common.page.PageData;
import com.wms.business.WmsWaybill;

import its.base.service.BaseService;

public interface WmsWaybillService extends BaseService<WmsWaybill>{
    /**
     * 分页查询
     */
    PageData<WmsWaybill> getPageData(int page, int rows, WmsWaybill wmsWaybill);
  
}
