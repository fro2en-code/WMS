package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.wms.warehouse.WmsObject;

import its.base.service.BaseService;

/**
 * 零件表
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年3月15日
 */
public interface WmsObjectService extends BaseService<WmsObject> {
    /**
     * 更新总账数据
     */
    void updateWmsObject(WmsObject wmsObject);

    /**
     * 根据仓库代码、供应商代码、物料代码,零件类型 查询零件实体
     */
    WmsObject getWmsObject(WmsObject wmsObject);

    /**
     * 根据仓库代码、供应商代码、物料代码,零件类型 查询零件实体
     */
    List<WmsObject> getWmsObjectList(WmsObject wmsObject);

    /**
     * 零件分页查询
     */
    PageData<WmsObject> getPageData(int page, int rows, WmsObject wmsObject);

}
