package wms.business.service;

import java.io.Serializable;

import com.plat.common.page.PageData;
import com.wms.business.WmsStockLockLog;

import its.base.service.BaseService;

public interface WmsStockLockLogService extends BaseService<WmsStockLockLog> {
    PageData<WmsStockLockLog> getPageData(int page, int rows, WmsStockLockLog ws);

    void del(Serializable id);

    void save(WmsStockLockLog bean);

    void sure(Serializable id, String userName);
}
