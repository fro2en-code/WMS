package wms.business.service;

import java.util.List;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsStocktakePlan;
import its.base.service.BaseService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsStocktakePlanService extends BaseService<WmsStocktakePlan> {

    /**
     * 分页查询
     */
    PageData<WmsStocktakePlan> getPageData(int page, int rows, WmsStocktakePlan wmsStocktakePlan);
    /**
     * 新增,修改
     * @param wmsStocktakePlan
     * @return
     */
    ResultResp saveStocktakePlan(WmsStocktakePlan wmsStocktakePlan);
    
    /**
     * 删除
     */
    ResultResp deleteStocktakePlan(String id); 
    /**
     * 根据billid查询
     */
    List<WmsStocktakePlan> getWmsStocktakePlan(String billId);
}
