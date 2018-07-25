package wms.userRelation.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.userRelation.UserWarehouse;

import its.base.service.BaseService;

public interface UserWarehouseService extends BaseService<UserWarehouse> {

    /**
     * 查询分页数据
     */
    PageData<UserWarehouse> getPageData(int page, int rows, String loginname);

    /**
     * 根据登陆名称获取仓库集合
     */
    List<UserWarehouse> getWarehouseMap(String loginname);

    /**
     * 保存仓库绑定
     */
    ResultResp save(UserWarehouse userWarehouse);

    /**
     * 删除仓库绑定
     */
    ResultResp del(String id);
    
    /**
     * 获取用户
     */

	PageData<UserWarehouse> getUserPageData(int page, int rows,UserWarehouse userWarehouse);

}