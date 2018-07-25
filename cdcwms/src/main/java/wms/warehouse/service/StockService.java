package wms.warehouse.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.warehouse.WmsStock;

import its.base.service.BaseService;

public interface StockService extends BaseService<WmsStock> {
    /**
     * 库存分页查询
     */
    PageData<WmsStock> getPageData(int page, int rows, WmsStock ws);

    /**
     * 根据批次号查询库存
     */
    List<WmsStock> getStockByBatchCode(String batchCode);

    /**
     * 根据批次号查询库存(未上架的物料)
     */
    List<WmsStock> getNoShelvesByBatchCode(String batchCode);

    /**
     * 查询已上架的库存(精确查询)
     */
    List<WmsStock> getStockBy(String whCode, String batchCode, String gcode, String oraCode);

    /**
     * 插入库存表
     */
    ResultResp insertStock(WmsStock ws) throws Exception;

    /**
     * 插入库存表
     */
    ResultResp insertStock(List<WmsStock> list) throws Exception;

    /**
     * 消减库存
     */
    ResultResp cutStock(WmsStock ws) throws Exception;

    /**
     * 消减库存
     */
    ResultResp cutStock(List<WmsStock> list) throws Exception;
}