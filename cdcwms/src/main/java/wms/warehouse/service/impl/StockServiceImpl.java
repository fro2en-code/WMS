package wms.warehouse.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.warehouse.WmsStock;

import its.base.service.BaseServiceImpl;
import wms.warehouse.service.StockService;

/**
 * 库存查询实现
 */

@Service("stockService")
public class StockServiceImpl extends BaseServiceImpl<WmsStock> implements StockService {
    /*
     * 查询分页
     */
    @Override
    public PageData<WmsStock> getPageData(int page, int rows, WmsStock ws) {
        List<Serializable> params = new ArrayList<>();
        StringBuffer base_hql = new StringBuffer("from WmsStock where 1=1 ");
        if (!StringUtil.isEmpty(ws.getWhCode())) {
            base_hql.append(" And whCode =? ");
            params.add(ws.getWhCode());
        }
        if (!StringUtil.isEmpty(ws.getZoneCode())) {
            base_hql.append(" And zoneCode like ? ");
            params.add("%" + ws.getZoneCode() + "%");
        }
        if (!StringUtil.isEmpty(ws.getStorageCode())) {
            base_hql.append(" And storageCode like ? ");
            params.add("%" + ws.getStorageCode() + "%");
        }
        if (!StringUtil.isEmpty(ws.getGcode())) {
            base_hql.append(" And gcode like ? ");
            params.add("%" + ws.getGcode() + "%");
        }
        if (!StringUtil.isEmpty(ws.getSupCode())) {
            base_hql.append(" And supCode like ? ");
            params.add("%" + ws.getSupCode() + "%");
        }
        return getPageDataByBaseHql(base_hql.toString(), null, page, rows, params);
    }

    /**
     * 插入库存表
     */
    @Override
    public ResultResp insertStock(WmsStock ws) throws Exception {
        ResultResp resp = new ResultResp();
        saveEntity(ws);
        resp.setRetcode("0");
        resp.setRetmsg("插入库存成功！");
        return resp;
    }

    /**
     * 插入库存表
     */
    @Override
    public ResultResp insertStock(List<WmsStock> list) throws Exception {
        ResultResp resp = new ResultResp();
        for (WmsStock ws : list) {
            resp = this.insertStock(ws);
        }
        return resp;
    }

    /**
     * 根据批次号查询库存
     */
    @Override
    public List<WmsStock> getStockByBatchCode(String batchCode) {
        return findEntityByHQL("From WmsStock Where batchCode = ? ", batchCode);
    }

    /**
     * 根据批次号查询库存(未上架的物料)
     */
    @Override
    public List<WmsStock> getNoShelvesByBatchCode(String batchCode) {
        return findEntityByHQL("From WmsStock Where batchCode = ? And  IfNull(storageCode,0) = 0 ", batchCode);
    }

    /**
     * 查询已上架的库存(精确查询)
     */
    @Override
    public List<WmsStock> getStockBy(String whCode, String batchCode, String gcode, String oraCode) {
        String sql = "From WmsStock Where whCode=? And batchCode = ? And gcode = ? And oraCode = ? And storageCode <> ''";
        return findEntityByHQL(sql, whCode, batchCode, gcode, oraCode);
    }

    /**
     * 消减库存
     */
    @Override
    public ResultResp cutStock(WmsStock ws) throws Exception {
        ResultResp resp = new ResultResp();
        // List<WmsStock> stocks = this.getStockBy(ws.getWhCode(),ws.getBatchCode(), ws.getgcode(), ws.getOraCode());
        // if(stocks.size() != 1){
        // throw new RuntimeException("未查询到该物料!");
        // }
        // WmsStock stock = stocks.get(0);
        // if(ws.getQuantity() > stock.getQuantity()){
        // throw new RuntimeException("不能负库存消减,请求数量[" + ws.getQuantity() + "]消减数量[" + stock.getQuantity() + "],请检查!");
        // }
        // stock.setQuantity(stock.getQuantity() - ws.getQuantity());
        // updateEntity(stock);
        // resp.setRetcode("0");
        // resp.setRetmsg("消减库存成功！");
        return resp;
    }

    /**
     * 消减库存
     */
    @Override
    public ResultResp cutStock(List<WmsStock> list) throws Exception {
        ResultResp resp = new ResultResp();
        for (WmsStock ws : list) {
            resp = this.cutStock(ws);
        }
        return resp;
    }
}
