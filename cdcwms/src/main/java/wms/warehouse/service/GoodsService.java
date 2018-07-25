package wms.warehouse.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.warehouse.WmsGoods;

import its.base.service.BaseService;

/**
 * 物料接口
 *
 * @author wlx
 */
public interface GoodsService extends BaseService<WmsGoods> {

	/**
	 * 删除物料
	 */
	ResultResp del(String id);

	/**
	 * 根据物料编号获取ABC收货类型
	 */
	String getAbcByGcode(String gcode, String whCode);

	/**
	 * 根据物料编码,零件用途来查找物料信息
	 */
	WmsGoods getGoodsInfo(String gcode, String gtype, String oraCode, String whCode);

	/**
	 * 根据物料编号获取Name
	 */
	String getNameByGcode(String gcode, String whCode);

	/**
	 * 物料分页查询
	 */
	PageData<WmsGoods> getPageData(int page, int rows, WmsGoods goods);

	/**
	 * 根据物料编号查询供货商
	 */
	List<Map<String, String>> getWmsGoodsByKey(String key, String whCode);

	/**
	 * 根据物料编号查询供货商
	 */
	List<Map<String, String>> getWmsGoodsInfoByKey(String key, String whCode);

	/**
	 * 保存、修改物料
	 */
	ResultResp save(WmsGoods goods);

	public Serializable saveEntity(WmsGoods goods);
}
