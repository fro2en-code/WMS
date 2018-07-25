package wms.warehouse.service;

import java.util.List;
import java.util.Map;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.warehouse.WmsWarehouse;

import its.base.service.BaseService;

public interface WarehouseService extends BaseService<WmsWarehouse> {

    /**
     * 仓库分页查询
     */
    PageData<WmsWarehouse> getPageData(int page, int rows, String name);

    /**
     * 增加、修改仓库
     **/
    ResultResp saveWms(WmsWarehouse ws);

    /**
     * 根据编号删除仓库
     */
    ResultResp delWmsById(String id);

    /**
     * 获取全部仓库
     */
    List<WmsWarehouse> getAllWmsWarehouse();

    /**
     * 获取全部仓库
     */
    Map<String, String> getAllWmsWarehouseMap();

    /**
     * 根据仓库代码获取仓库
     */
    WmsWarehouse getWmsByCode(String whCode);

    /**
     * 获取下拉仓库列表
     */
    List<Map<String, String>> getCombobox();

}