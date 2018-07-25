package wms.business.service;

import com.plat.common.page.PageData;
import com.wms.business.WmsWaybillList;

import its.base.service.BaseService;

public interface WmsWaybillListService extends BaseService<WmsWaybillList>{
    /**
     * 分页查询
     */
    PageData<WmsWaybillList> getPageData(int page, int rows, WmsWaybillList wmsWaybillList);
  
}
