package wms.orginfo.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.orginfo.ExtOraTruck;

import its.base.service.BaseService;

/**
 * 车辆管理接口
 * 
 * @author fc
 */
public interface TruckService extends BaseService<ExtOraTruck> {

    /**
     * 车辆分页查询
     */
    PageData<ExtOraTruck> getPageData(int page, int rows, ExtOraTruck extOraTruck);

    /**
     * 查询全部车辆数据
     */
    List<ExtOraTruck> getAllExtOraTruck();

    /**
     * 查询全部车辆数据
     */
    List<ExtOraTruck> getTruck(String key, String whCode);

    /**
     * 保存、修改车辆
     */
    ResultResp save(ExtOraTruck et);

    /**
     * 删除车辆
     */
    ResultResp del(String id);
}
