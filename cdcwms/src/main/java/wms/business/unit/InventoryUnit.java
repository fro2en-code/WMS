package wms.business.unit;

import java.util.List;
import java.util.Map;

import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsStock;

/**
 * 库存单元
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年3月22日
 */
public interface InventoryUnit {

	/*
	 * 修改库存信息,同步 台账,零件(count 为正则入库,为负则为出库)
	 */
	void addWmsStock(String storageCode, WmsGoods wmsGoods, int count, boolean lock, String number);

	/**
	 * 获取库存总数(预拣货+当前库存件数)
	 *
	 * @param storag
	 * @param wmsGoods
	 * @return
	 */
	int getCountWmsStock(String storageCode, WmsGoods wmsGoods, boolean lock);

	/**
	 * 获取库位(入库)
	 *
	 * @param wmsGoods
	 *            物料
	 * @param count
	 *            上架件数
	 */
	Map<String, Integer> getInWmsStorag(WmsGoods wmsGoods, int count);

	/**
	 * 获取库位(出库)
	 *
	 * @param wmsGoods
	 *            物料
	 * @param whCode
	 *            仓库
	 * @param count
	 *            上架件数
	 */
	Map<String, Integer> getOutWmsStorag(WmsGoods wmsGoods, int count);

	/**
	 * 获取库位零件总数
	 */
	List<WmsStock> getWmsStockCount(String storageCode, String whCode);

	/**
	 * 预拣货库存
	 *
	 * @param storag
	 *            库位
	 * @param wmsGoods
	 *            物料
	 * @param count
	 *            操作数量
	 * @param lockType
	 *            锁定类型
	 */
	void lockWmsStock(String srcStorag, String distStorag, WmsGoods wmsGoods, int count);

	/**
	 * 锁定库存
	 *
	 * @param storag
	 *            库位
	 * @param wmsGoods
	 *            物料
	 * @param count
	 *            操作数量
	 */
	void lockWmsStock(String storageCode, WmsGoods wmsGoods, int count);

	/**
	 * 移库(因为现在没有事务,调用前务必判断库存够不够)
	 *
	 * @param srcStorag
	 *            源库位
	 * @param distStorag
	 *            目标库位
	 * @param wmsGoods
	 *            物料
	 * @param count
	 *            操作数量
	 */
	void moveWmsStock(String srcStorag, String distStorag, WmsGoods wmsGoods, int count, boolean inLock);

	/**
	 * 预拣货库存
	 *
	 * @param storag
	 *            库位
	 * @param wmsGoods
	 *            物料
	 * @param count
	 *            操作数量
	 * @param lockType
	 *            锁定类型
	 */
	void preWmsStock(String storageCode, WmsGoods wmsGoods, int count);

	/**
	 * 锁定库存
	 *
	 * @param storag
	 *            库位
	 * @param wmsGoods
	 *            物料
	 * @param count
	 *            操作数量
	 */
	void unLockWmsStock(String storageCode, WmsGoods wmsGoods, int count);

	/**
	 * 获取库位上的零件信息
	 */
	List<WmsStock> getWmsStockObject(String storageCode, String whCode);

}
