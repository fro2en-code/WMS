package wms.business.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.AccountInit;
import com.wms.business.AccountInitList;
import com.wms.business.WmsAccount;
import com.wms.business.WmsTotalAccount;
import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsObject;
import com.wms.warehouse.WmsWarehouse;

import wms.business.biz.AccountBiz;
import wms.business.unit.AccountUnit;
import wms.business.unit.InventoryUnit;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;

@Service("accountBiz")
public class AccountBizImpl implements AccountBiz {
    @Resource
    private AccountUnit accountUnit;
    @Resource
    private GoodsService goodsService;
    @Resource
    private InventoryUnit inventoryUnit;
    @Resource
    private StoragService storagService;
    @Resource
    private WarehouseService warehouseService;

    @Override
    public ResultResp delAccountInit(AccountInit accountInit) {
        return accountUnit.delAccountInit(accountInit);
    }

    @Override
    public ResultResp delAccountInitList(AccountInitList accountInitList) {
        return accountUnit.delAccountInitList(accountInitList);
    }

    @Override
    public PageData<AccountInit> getPageData(int page, int rows, AccountInit account) {
        return accountUnit.getPageData(page, rows, account);
    }

    @Override
    public PageData<AccountInitList> getPageData(int page, int rows, AccountInitList accountInitList) {
        return accountUnit.getPageData(page, rows, accountInitList);
    }

    @Override
    public PageData<WmsAccount> getPageData(int page, int rows, WmsAccount account) {
        return accountUnit.getPageData(page, rows, account);
    }

    @Override
    public PageData<WmsObject> getPageData(int page, int rows, WmsObject wmsObject) {
        return accountUnit.getPageData(page, rows, wmsObject);
    }

    @Override
    public PageData<WmsTotalAccount> getPageData(int page, int rows, WmsTotalAccount totalaccount, String... companys) {
        return accountUnit.getPageData(page, rows, totalaccount, companys);
    }

    @Override
    public ResultResp saveAccountInit(AccountInit account) {
        return accountUnit.saveAccountInit(account);
    }

    @Override
    public ResultResp updateSureAccountInit(AccountInit accountInit) {
        accountInit = accountUnit.getAccountInitById(accountInit.getId());
        if (accountInit.getStatus() == 1) {
            return new ResultResp(ResultResp.ERROR_CODE, "确认失败,该台账初始化单据据已确认!");
        }
        List<AccountInitList> list = accountUnit.getAllInitList(accountInit.getInitCode());
        if (list.size() == 0) {
            return new ResultResp(ResultResp.ERROR_CODE, "确认失败,该台账初始化单据据没有明细!");
        }
        WmsWarehouse warehouse = warehouseService.getWmsByCode(accountInit.getWhCode());
        if (null == warehouse) {
            return new ResultResp(ResultResp.ERROR_CODE, "确认失败,仓库不存在!");
        }
        // 保存台账明细

        for (AccountInitList accountInitList : list) {
            WmsGoods wmsGoods = goodsService.getGoodsInfo(accountInitList.getGcode(), accountInitList.getGtype(),
                    accountInitList.getOraCode(), accountInitList.getWhCode());
            int count = accountInitList.getInitNum();
            inventoryUnit.addWmsStock(accountInitList.getStorageCode(), wmsGoods, count, false,
                    accountInit.getInitCode());
        }
        // 更新状态
        accountInit.setStatus(1);
        accountUnit.updateEntity(accountInit);
        return new ResultResp(ResultResp.SUCCESS_CODE, "确认成功");
    }

}
