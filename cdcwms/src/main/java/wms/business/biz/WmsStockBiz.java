package wms.business.biz;

import java.io.Serializable;

import com.plat.common.page.PageData;
import com.wms.business.WmsStockLockLog;
import com.wms.warehouse.WmsStock;

public interface WmsStockBiz {
    /**
     * 库存分页查询
     */
    PageData<WmsStock> getPageData(int page, int rows, WmsStock ws);

    PageData<WmsStockLockLog> getPageData(int page, int rows, WmsStockLockLog ws);

    void save(WmsStockLockLog ws);

    void sure(Serializable id, String userName);

    void delWmsStockLockLog(Serializable id);
}
