package wms.business.unit.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.plat.common.utils.BeanUtils;
import com.plat.common.utils.Function;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsAccount;
import com.wms.business.WmsTotalAccount;
import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsObject;
import com.wms.warehouse.WmsStock;
import com.wms.warehouse.WmsStorag;
import com.wms.warehouse.WmsWarehouse;
import com.ymt.utils.GoodsException;
import com.ymt.utils.StockException;

import wms.business.service.AccountService;
import wms.business.service.TotalAccountService;
import wms.business.service.WmsObjectService;
import wms.business.service.WmsStockService;
import wms.business.unit.InventoryUnit;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;

/**
 * 我要在这里添加互斥锁,但是这里加同步锁以后可能会出现死锁的情况.但是,我有什么办法?我也很无奈啊 这里三元运算符搞错优先级了,可能是它导致的库存出错
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年5月4日
 */
@Service("inventoryUnit")
public class InventoryUnitImpl implements InventoryUnit {
    /**
     * 库存操作
     *
     * @author zhouxianglh@gmail.com
     *
     *         2017年9月13日
     */
    interface OperateStock {
        /**
         *
         * @param wmsStock
         *            库存记录
         * @param num
         *            需要操作的件数
         * @return 操作了多少件
         */
        int run(WmsStock wmsStock, int num);
    }

    @Resource
    private AccountService accountService;
    @Resource
    private StoragService storagService;

    @Resource
    private TotalAccountService totalAccountService;

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private WmsObjectService wmsObjectService;

    @Resource
    private WmsStockService wmsStockService;

    private void addAccoutList(int count, String batchCode, String number, WmsTotalAccount totalAccount) {
        WmsAccount account = new WmsAccount();
        account.setDealCode(number);
        BeanUtils.copyProperty(totalAccount, account, "gcode", "gname", "gtype", "oraCode", "oraName", "whCode",
                "whName", "totalNum");
        if (count > 0) {// 此操作为入库操作
            account.setAccountType(1);
            account.setDealType(1);
            account.setBatchCode(batchCode);// 入库存在批次操作,出库无批次操作
        } else {// 此操作为出库操作
            account.setAccountType(0);
            account.setDealType(0);
        }
        account.setDealNum(count);
        String nowTime = StringUtil.getCurStringDate(StringUtil.PATTERN);
        account.setInTime(nowTime);
        account.setDealTime(new Date());
        accountService.saveEntity(account);
    }

    private void addValue(int count, Map<String, Integer> resultMap, WmsStock wmsStock) {
        if (resultMap.containsKey(wmsStock.getStorageCode())) {
            resultMap.put(wmsStock.getStorageCode(), resultMap.get(wmsStock.getStorageCode()) + count);
        } else {
            resultMap.put(wmsStock.getStorageCode(), count);
        }
    }

    @Override
    public void addWmsStock(String storageCode, WmsGoods wmsGoods, int count, boolean lock, String number) {
        String batchCode = wmsGoods.getOraCode() + StringUtil.getCurStringDate("yyyyMMdd");
        // 更新库存
        updateStock(storageCode, wmsGoods, count, batchCode, lock);
        // 更新台账总账
        updateAccount(storageCode, wmsGoods, count, batchCode, number);
        // 更新零件表
        updateWmsObject(storageCode, wmsGoods, count, batchCode);
    }

    /**
     * 释放占拉库存
     */
    private void freePreLock(final WmsStock distStock, final int inNum) {
        operateStock(distStock, new OperateStock() {

            @Override
            public int run(WmsStock wmsStock, int num) {
                if (wmsStock.getPreLock() == 0) {
                    return 0;
                }
                if (num <= wmsStock.getPreLock()) {
                    wmsStock.setPreLock(wmsStock.getPreLock() - num);
                    return num;
                } else {
                    int lockNum = wmsStock.getPreLock();
                    wmsStock.setPreLock(0);
                    return lockNum;
                }
            }
        }, inNum);
    }

    @Override
    public int getCountWmsStock(String storageCode, WmsGoods wmsGoods, boolean lock) {
        WmsStock stock = getSearch(storageCode, wmsGoods);
        return wmsStockService.getWmsStockCount(stock, lock);
    }

    @Override
    public Map<String, Integer> getInWmsStorag(WmsGoods wmsGoods, int count) {
        int storageType = wmsGoods.getStoragezoneType();
        List<WmsStorag> wmsStoragList = getStorages(wmsGoods, storageType);
        String batchCode = wmsGoods.getOraCode() + StringUtil.getCurStringDate("yyyyMMdd");
        Map<String, Integer> map = new HashMap<>();
        for (WmsStorag strorag : wmsStoragList) {
            WmsStock searchStock = getSearch(strorag.getStorageCode(), wmsGoods);
            List<WmsStock> stockList = wmsStockService.getWmsStock(searchStock);
            // 统计总件数
            int total = 0;
            for (WmsStock wmsStock : stockList) {
                total += wmsStock.getQuantity();
                total += wmsStock.getPrePickNum();
                total += wmsStock.getLockNum();
                total += wmsStock.getPreLock();
            }
            if (1 == wmsGoods.getBatchType()) {// 区分批次
                boolean flag = true;
                for (WmsStock wmsStock : stockList) {
                    if (!batchCode.equals(wmsStock.getBatchCode())) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    continue;
                }
            }
            // 可存放件数
            int canCount = wmsGoods.getStorageMaxNum() - total;
            if (canCount > 0) {
                if (canCount >= count) {
                    map.put(strorag.getStorageCode(), count);
                    count = 0;
                } else {
                    map.put(strorag.getStorageCode(), canCount);
                    count -= canCount;
                }
            }
            //
            if (count == 0) {
                break;
            }
        }
        if (count != 0) {// 多出来的库存放到溢库区
            WmsWarehouse wh = warehouseService.getWmsByCode(wmsGoods.getWhCode());
            String overFlow = wh.getSpillZone();
            map.put(overFlow, count);
        }
        return map;
    }

    private List<WmsStorag> getStorages(WmsGoods wmsGoods, int storageType) {
        List<WmsStorag> wmsStoragList;
        String storages = wmsGoods.getStoragezoneId();
        String[] stroageArr = storages.split(",");
        if (storageType == 1) {// 指定库位
            wmsStoragList = storagService.getStorageByCode(wmsGoods.getWhCode(), stroageArr);
        } else {// 库区
            wmsStoragList = storagService.getStorageByZone(wmsGoods.getWhCode(), stroageArr);
        }
        return wmsStoragList;
    }

    @Override
    public Map<String, Integer> getOutWmsStorag(WmsGoods wmsGoods, int count) {
        WmsStock stock = new WmsStock();
        BeanUtils.copyProperty(wmsGoods, stock, "gcode", "gname", "gtype", "whCode");
        stock.setSupCode(wmsGoods.getOraCode());
        List<WmsStock> resultStock = wmsStockService.getWmsStock(stock);
        Map<String, Integer> resultMap = new HashMap<>();
        for (WmsStock wmsStock : resultStock) {
            if (null != wmsStock.getPrePickNum() && wmsStock.getPrePickNum() > 0) {
                if (count <= wmsStock.getPrePickNum()) {
                    addValue(count, resultMap, wmsStock);
                    count = 0;
                    break;
                } else {
                    addValue(wmsStock.getPrePickNum(), resultMap, wmsStock);
                    count = count - wmsStock.getPrePickNum();
                }
            }
            if (wmsStock.getQuantity() == 0) {// 在存在锁定库存有值,原库存无值 时会存在存库0的任务
                continue;
            }
            if (count <= wmsStock.getQuantity()) {
                addValue(count, resultMap, wmsStock);
                count = 0;
                break;
            } else {
                addValue(wmsStock.getQuantity(), resultMap, wmsStock);
                count = count - wmsStock.getQuantity();
            }
        }
        if (count > 0) {
            throw new StockException(wmsGoods.getGcode() + "没有足够的库存");
        }
        return resultMap;
    }

    /**
     * 获取 WmsStock 对象,用于查询
     */
    private WmsStock getSearch(String storageCode, WmsGoods wmsGoods) {
        if (StringUtils.isEmpty(storageCode)) {
            throw new RuntimeException(String.format("物料:%s 所在操作库位不存在", wmsGoods.getGcode()));
        }
        WmsStock stock = new WmsStock();
        BeanUtils.copyProperty(wmsGoods, stock, "gcode", "gname", "gtype", "whCode");
        stock.setStorageCode(storageCode);
        stock.setSupCode(wmsGoods.getOraCode());
        stock.setSupName(wmsGoods.getOraName());
        return stock;
    }

    @Override
    public List<WmsStock> getWmsStockCount(String storageCode, String whCode) {
        return wmsStockService.getWmsStockCount(storageCode, whCode);
    }

    private void inWmsObject(int count, String batchCode, WmsObject wmsObject) {
        wmsObject.setInTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        wmsObject.setBatchCode(batchCode);
        WmsObject resultWmsObject = wmsObjectService.getWmsObject(wmsObject);
        // 总账如果不存在就用新增的对象,存在就用数据库中的对象
        if (null != resultWmsObject) {
            wmsObject = resultWmsObject;
        }
        if (null == wmsObject.getQuantity()) {
            wmsObject.setQuantity(count);
        } else {
            wmsObject.setQuantity(wmsObject.getQuantity() + count);
        }
        wmsObjectService.saveOrUpdateEntity(wmsObject);
    }

    @Override
    public void lockWmsStock(String srcStorag, String distStorag, WmsGoods wmsGoods, int count) {
        // 锁定初始库位库存
        if (StringUtils.isNotEmpty(srcStorag)) {
            lockWmsStock(srcStorag, wmsGoods, count);
        }
        // 增加占位库存
        if (StringUtils.isNotEmpty(distStorag)) {
            WmsStock stock = getSearch(distStorag, wmsGoods);
            String batchCode = wmsGoods.getOraCode() + StringUtil.getCurStringDate("yyyyMMdd");
            stock.setBatchCode(batchCode);
            List<WmsStock> stockList = wmsStockService.getWmsStock(stock);
            if (null != stockList && stockList.size() > 0) {// 如果已有库存记录则修改
                WmsStock distStock = stockList.get(0);
                distStock.setPreLock(distStock.getPreLock() + count);
                wmsStockService.updateEntity(distStock);
            } else {// 如果无库存记录,新增
                WmsStock distStock = stock;
                distStock.setPreLock(count);
                wmsStockService.saveEntity(distStock);
            }
        }
    }

    @Override
    public void lockWmsStock(String storageCode, WmsGoods wmsGoods, int count) {
        WmsStock stock = getSearch(storageCode, wmsGoods);
        int result = operateStock(stock, new OperateStock() {
            @Override
            public int run(WmsStock wmsStock, int num) {
                return lockWmsStock(wmsStock, num);
            }

        }, count);
        if (result > 0) {
            throw new GoodsException(wmsGoods.getGcode() + "库存不足");
        }
    }

    /**
     * 锁定库存的实际操作类
     */
    private int lockWmsStock(WmsStock wmsStock, int num) {
        int flag = 0;
        // 优先操作预拣货库存
        if (wmsStock.getPrePickNum() > 0) {
            if (num <= wmsStock.getPrePickNum()) {
                wmsStock.setPrePickNum(wmsStock.getPrePickNum() - num);
                wmsStock.setLockNum(wmsStock.getLockNum() + num);
                flag += num;
                num = 0;
            } else {
                flag += wmsStock.getPrePickNum();
                num -= wmsStock.getPrePickNum();
                wmsStock.setLockNum(wmsStock.getLockNum() + wmsStock.getPrePickNum());
                wmsStock.setPrePickNum(0);
            }
        }
        // 操作正常库存
        if (wmsStock.getQuantity() > 0) {
            if (num <= wmsStock.getQuantity()) {
                wmsStock.setQuantity(wmsStock.getQuantity() - num);
                wmsStock.setLockNum(wmsStock.getLockNum() + num);
                flag += num;
                num = 0;
            } else {
                flag += wmsStock.getQuantity();
                num -= wmsStock.getQuantity();
                wmsStock.setLockNum(wmsStock.getLockNum() + wmsStock.getQuantity());
                wmsStock.setQuantity(0);
            }
        }
        return flag;
    }

    private int moveWmsStock(final String distStorag, final boolean inLock, final WmsStock distStock, WmsStock wmsStock,
            int num) {
        if (wmsStock.getLockNum() == 0) {
            return 0;
        }
        // 扣减锁定库存
        int lockNum = 0;
        if (num <= wmsStock.getLockNum()) {
            wmsStock.setLockNum(wmsStock.getLockNum() - num);
            lockNum = num;
        } else {
            lockNum = wmsStock.getLockNum();
            wmsStock.setLockNum(0);
        }
        // 入库
        final int inNum = lockNum;
        freePreLock(distStock, inNum);
        stockIn(distStorag, distStock, new Function<WmsStock>() {
            @Override
            public void run(WmsStock t) {
                if (inLock) {
                    t.setLockNum(t.getLockNum() + inNum);
                } else {
                    t.setQuantity(t.getQuantity() + inNum);
                }
            }
        });
        return inNum;
    }

    @Override
    public void moveWmsStock(final String srcStorag, final String distStorag, WmsGoods wmsGoods, int count,
            final boolean inLock) {
        WmsStock srcStock = getSearch(srcStorag, wmsGoods);
        final WmsStock distStock = getSearch(distStorag, wmsGoods);
        int result = operateStock(srcStock, new OperateStock() {
            @Override
            public int run(WmsStock wmsStock, int num) {
                return moveWmsStock(distStorag, inLock, distStock, wmsStock, num);
            }
        }, count);
        if (result > 0) {
            throw new RuntimeException(
                    String.format("物料:%s 所在库位:%s 锁定库存不足,请联系开发人员", srcStock.getGcode(), srcStock.getStorageCode()));
        }
    }

    /**
     * 库存操作
     *
     * @return 还差多少没执行
     */
    private int operateStock(WmsStock search, OperateStock function, int count) {
        // 按先入先出原则,优先处理先入库的批次
        List<WmsStock> list = wmsStockService.getWmsStock(search);
        for (WmsStock wmsStock : list) {
            int result = function.run(wmsStock, count);
            if (result == 0) {
                continue;
            }
            count -= result;
            // 如果库存清光了,这里就删除库存记录
            int nowCount = wmsStock.getQuantity() + wmsStock.getLockNum() + wmsStock.getPrePickNum()
                    + wmsStock.getPreLock();
            if (0 != nowCount) {
                wmsStockService.updateEntity(wmsStock);
            } else {
                wmsStockService.deleteEntity(wmsStock);
            }
            //
            if (count == 0) {
                break;
            }
        }
        return count;
    }

    private void outWmsObject(int count, WmsObject wmsObject) {
        List<WmsObject> list = wmsObjectService.getWmsObjectList(wmsObject);
        for (WmsObject nowWmsObject : list) {
            if (nowWmsObject.getQuantity() > count) {
                nowWmsObject.setQuantity(nowWmsObject.getQuantity() - count);
                wmsObjectService.updateEntity(nowWmsObject);
                break;
            } else if (nowWmsObject.getQuantity() == count) {
                wmsObjectService.deleteEntity(nowWmsObject);
                break;
            } else {
                count = count - nowWmsObject.getQuantity();
                wmsObjectService.deleteEntity(nowWmsObject);
            }
        }
    }

    @Override
    public void preWmsStock(String storageCode, WmsGoods wmsGoods, int count) {
        WmsStock stock = getSearch(storageCode, wmsGoods);
        int result = operateStock(stock, new OperateStock() {
            @Override
            public int run(WmsStock wmsStock, int num) {
                // 任何时候,只能减锁定库存
                if (wmsStock.getLockNum() == 0) {
                    return 0;
                }
                if (num <= wmsStock.getLockNum()) {
                    wmsStock.setLockNum(wmsStock.getLockNum() - num);
                    wmsStock.setPrePickNum(wmsStock.getPrePickNum() + num);
                    return num;
                } else {
                    int operNum = wmsStock.getLockNum();
                    wmsStock.setPrePickNum(wmsStock.getPrePickNum() + operNum);
                    wmsStock.setLockNum(0);
                    return operNum;
                }
            }

        }, count);
        if (result > 0) {
            throw new RuntimeException("预拣货数大于库存总数");
        }
    }

    /**
     * 入库,加库存
     */
    private void stockIn(String batchCode, WmsStock stock, Function<WmsStock> function) {
        stock.setBatchCode(batchCode);
        List<WmsStock> resultStock = wmsStockService.getWmsStock(stock);
        if (null != resultStock && resultStock.size() > 0) {// 如果已有库存记录则修改
            stock = resultStock.get(0);
            function.run(stock);
            wmsStockService.updateEntity(stock);
        } else {// 如果无库存记录,新增
            function.run(stock);
            wmsStockService.saveEntity(stock);
        }
    }

    /**
     * 扣减库存件数(这里只操作锁定库存,出库必需是 :1先锁定库存,2再扣减锁定库存)
     */
    private void stockOut(int count, WmsStock stock) {
        int result = operateStock(stock, new OperateStock() {
            @Override
            public int run(WmsStock wmsStock, int num) {
                if (wmsStock.getLockNum() == 0) {
                    return 0;
                }
                if (num <= wmsStock.getLockNum()) {
                    wmsStock.setLockNum(wmsStock.getLockNum() - num);
                    return num;
                } else {
                    int lockNum = wmsStock.getLockNum();
                    wmsStock.setLockNum(0);
                    return lockNum;
                }
            }
        }, count);
        if (result > 0) {
            throw new RuntimeException(
                    String.format("物料:%s 所在库位:%s 锁定库存不足,请联系开发人员", stock.getGcode(), stock.getStorageCode()));
        }
    }

    @Override
    public void unLockWmsStock(String storageCode, WmsGoods wmsGoods, int count) {
        WmsStock stock = getSearch(storageCode, wmsGoods);
        int result = operateStock(stock, new OperateStock() {

            @Override
            public int run(WmsStock wmsStock, int num) {
                if (wmsStock.getLockNum() == 0) {
                    return 0;
                }
                if (num <= wmsStock.getLockNum()) {
                    wmsStock.setLockNum(wmsStock.getLockNum() - num);
                    wmsStock.setQuantity(wmsStock.getQuantity() + num);
                    return num;
                } else {
                    int lockNum = wmsStock.getLockNum();
                    wmsStock.setLockNum(0);
                    wmsStock.setQuantity(wmsStock.getQuantity() + lockNum);
                    return lockNum;
                }
            }
        }, count);
        if (result > 0) {
            throw new RuntimeException(
                    String.format("物料:%s 所在库位:%s 解锁库存大于锁定库存数", stock.getGcode(), stock.getStorageCode()));
        }
    }

    /**
     * 更新台账
     */
    private void updateAccount(String storageCode, WmsGoods wmsGoods, int count, String batchCode, String number) {
        WmsTotalAccount totalAccount = new WmsTotalAccount();
        BeanUtils.copyProperty(wmsGoods, totalAccount, "gcode", "gname", "gtype", "oraCode", "oraName", "whCode",
                "whName");
        // 总账如果不存在就用新增的对象,存在就用数据库中的对象
        WmsTotalAccount resultTotalAccount = totalAccountService.getTotalAccount(totalAccount);
        if (null != resultTotalAccount) {
            totalAccount = resultTotalAccount;
        }
        totalAccount.setTotalNum(totalAccount.getTotalNum() + count);
        totalAccountService.saveOrUpdateEntity(totalAccount);
        // 新增台账明细
        addAccoutList(count, batchCode, number, totalAccount);
    }

    /**
     * 更新库存记录
     */
    private void updateStock(String storageCode, WmsGoods wmsGoods, final int count, String batchCode,
            final boolean lock) {
        WmsStock stock = getSearch(storageCode, wmsGoods);
        if (count > 0) {// 入库,则根据批次入库
            stockIn(batchCode, stock, new Function<WmsStock>() {

                @Override
                public void run(WmsStock t) {
                    if (lock) {
                        t.setLockNum(t.getLockNum() + count);
                    } else {
                        t.setQuantity(t.getQuantity() + count);
                    }
                }
            });
        } else {// 出库(如果出的不是锁定库存,则先锁定,再出库)
            if (!lock) {
                lockWmsStock(storageCode, wmsGoods, 0 - count);
            }
            stockOut(0 - count, stock);
        }
    }

    /**
     * 更新零件
     */
    private void updateWmsObject(String storageCode, WmsGoods wmsGoods, int count, String batchCode) {
        WmsObject wmsObject = new WmsObject();
        BeanUtils.copyProperty(wmsGoods, wmsObject, "gcode", "gname", "gtype", "whCode", "whName");
        wmsObject.setSupCode(wmsGoods.getOraCode());
        wmsObject.setSupName(wmsGoods.getOraName());
        if (count > 0) {// 入库
            inWmsObject(count, batchCode, wmsObject);
        } else {// 出库,因为出库严格先进先出,所以这里可以不理会批次号
            count = 0 - count;// 转换成正数,方便计算
            outWmsObject(count, wmsObject);
        }
    }

    @Override
    public List<WmsStock> getWmsStockObject(String storageCode, String whCode) {
        return wmsStockService.getWmsStockObject(storageCode, whCode);
    }

}
