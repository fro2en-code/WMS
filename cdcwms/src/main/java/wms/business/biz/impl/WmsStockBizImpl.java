package wms.business.biz.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.wms.business.WmsStockLockLog;
import com.wms.warehouse.WmsStock;

import wms.business.biz.WmsStockBiz;
import wms.business.service.WmsStockLockLogService;
import wms.warehouse.service.StockService;

@Service("wmsStockBiz")
public class WmsStockBizImpl implements WmsStockBiz {
    @Resource
    private StockService stockService;
    @Resource
    private WmsStockLockLogService wmsStockLockLogService;

    @Override
    public void delWmsStockLockLog(Serializable id) {
        wmsStockLockLogService.del(id);
    }

    @Override
    public PageData<WmsStock> getPageData(int page, int rows, WmsStock ws) {
        return stockService.getPageData(page, rows, ws);
    }

    @Override
    public PageData<WmsStockLockLog> getPageData(int page, int rows, WmsStockLockLog ws) {
        return wmsStockLockLogService.getPageData(page, rows, ws);
    }

    @Override
    public void save(WmsStockLockLog ws) {
        wmsStockLockLogService.save(ws);
    }

    @Override
    public void sure(Serializable id, String userName) {
        wmsStockLockLogService.sure(id, userName);
    }

}
