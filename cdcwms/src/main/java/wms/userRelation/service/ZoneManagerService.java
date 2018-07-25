package wms.userRelation.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.userRelation.ZoneManager;

/**
 * 用户库区管理关系接口
 */
public interface ZoneManagerService {
    /**
     * 获取库位责任人
     */
    Set<String> getManager(String strageCode);

    /**
     * 查询分页数据
     */
    PageData<ZoneManager> getPageData(int page, int rows, String loginname,String whCode);

    /**
     * 根据登陆名称获取库区集合
     */
    List<Map<String, String>> getWarehouseMap(String loginname);

    /**
     * 保存库区绑定
     */
    ResultResp save(ZoneManager zoneManager);

    /**
     * 删除库区绑定
     */
    ResultResp del(String id);
}
