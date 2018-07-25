package wms.business.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;
import com.wms.business.AccountInit;
import com.wms.business.AccountInitList;
import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsStorag;
import com.wms.warehouse.WmsWarehouse;
import com.wms.warehouse.WmsZone;

import its.base.service.BaseServiceImpl;
import wms.business.service.AccountInitListService;
import wms.business.service.AccountInitService;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;
import wms.warehouse.service.ZoneService;

/**
 * 初始化单据子表实现
 */
@Service("accountInitListService")
public class AccountInitListServiceImpl extends BaseServiceImpl<AccountInitList> implements AccountInitListService {

    @Resource
    private AccountInitService accountInitService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private StoragService storagService;
    @Resource
    private ZoneService zoneService;
    @Resource
    private WarehouseService warehouseService;

    /**
     * 初始化单据子表查询
     */
    @Override
    public PageData<AccountInitList> getPageData(int page, int rows, AccountInitList accountInitList) {
        return getPageDataByBaseHql("from AccountInitList where initCode=? ", " Order By gcode", page, rows,
                accountInitList.getInitCode());
    }

    /**
     * 查询全部初始化单据子表数据
     */
    @Override
    public List<AccountInitList> getAllInitList(String initCode) {
        return findEntityByHQL("From AccountInitList where initCode=?", initCode);
    }

    /**
     * 删除初始化单据子表(根据出库单号)
     */
    @Override
    public void delByInitCode(String initCode) {
        batchHandle("Delete From AccountInitList where initCode=?", initCode);
    }

    @Override
    public Serializable saveEntity(AccountInitList accountInitList) {
        if (StringUtil.isEmpty(accountInitList.getInitCode())) {
            throw new RuntimeException("初始化单号为空");
        }
        if (StringUtil.isEmpty(accountInitList.getWhCode())) {
            throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "仓库代码为空");
        }
        if (StringUtil.isEmpty(accountInitList.getZoneCode())) {
            throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "库区代码为空");
        }
        if (StringUtil.isEmpty(accountInitList.getStorageCode())) {
            throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "库位代码为空");
        }
        if (StringUtil.isEmpty(accountInitList.getGcode())) {
            throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "物料代码为空");
        }
        if (StringUtil.isEmpty(accountInitList.getGtype())) {
            throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "零件用途为空");
        }
        if (StringUtil.isEmpty(accountInitList.getOraCode())) {
            throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "客户代码为空");
        }
        if (accountInitList.getInitNum() == 0) {
            throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "初始化数量为0");
        }
        AccountInit account = accountInitService.getAccountInit(accountInitList.getInitCode());
        if (account == null) {
            throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "初始化单号不存在");
        }
        WmsWarehouse warehouse = warehouseService.getWmsByCode(accountInitList.getWhCode());
        if (warehouse == null) {
            throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "仓库代码不正确");
        }
        if (account.getStatus() == null) {
            throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "状态不存在");
        } else {
            if (!account.getStatus().equals(BaseModel.INT_INIT)) {
                throw new RuntimeException("初始化单号" + accountInitList.getInitCode() + "的状态不是  未确认   状态,不能导入数据");
            }
        }
        WmsGoods wmsGoods = goodsService.getGoodsInfo(accountInitList.getGcode(), accountInitList.getGtype(),
                accountInitList.getOraCode(), accountInitList.getWhCode());
        if (wmsGoods == null) {
            throw new RuntimeException(
                    "初始化单号" + accountInitList.getInitCode() + "中的物料" + accountInitList.getGcode() + "不存在");
        }
        WmsZone zone = zoneService.getComboboxByZoneCode(accountInitList.getZoneCode());
        if (zone == null) {
            throw new RuntimeException(
                    "初始化单号" + accountInitList.getInitCode() + "中的库区代码" + accountInitList.getZoneCode() + "不存在");
        }
        WmsStorag storag = storagService.getStorageByStorageCode(accountInitList.getStorageCode(),
                accountInitList.getWhCode());
        if (storag == null) {
            throw new RuntimeException(
                    "初始化单号" + accountInitList.getInitCode() + "中的库位代码" + accountInitList.getStorageCode() + "不存在");
        }
        return super.saveEntity(accountInitList);
    }

}
