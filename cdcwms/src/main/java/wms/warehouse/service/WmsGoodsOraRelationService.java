package wms.warehouse.service;

import java.io.Serializable;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.warehouse.WmsGoodsOraRelation;

import its.base.service.BaseService;

public interface WmsGoodsOraRelationService extends BaseService<WmsGoodsOraRelation>{

	/**
	 * 物料供应商分页查询
	 */
	PageData<WmsGoodsOraRelation> getPageData(int page, int rows, WmsGoodsOraRelation WmsGoodsOraRelation);
	
	/**
	 * 保存、修改物料供应商关系
	 */
	ResultResp save(WmsGoodsOraRelation wmsGoodsOraRelation);

	public Serializable saveEntity(WmsGoodsOraRelation wmsGoodsOraRelation);
	
	/**
	 * 删除物料供应商关系
	 */
	ResultResp del(String id);

}
