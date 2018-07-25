package wms.business.service;

import com.plat.common.page.PageData;
import com.wms.business.WmsAccount;

import its.base.service.BaseService;

/**
 * 台账查询接口
 *
 * @author wlx
 */
public interface AccountService extends BaseService<WmsAccount> {

    /**
     * 台账分页查询
     */
    PageData<WmsAccount> getPageData(int page, int rows, WmsAccount account);
}
