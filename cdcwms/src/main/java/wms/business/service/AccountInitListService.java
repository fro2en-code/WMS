package wms.business.service;

import java.util.List;

import com.plat.common.page.PageData;
import com.wms.business.AccountInitList;

import its.base.service.BaseService;

/**
 * 初始化单据子表接口
 *
 * @author wlx
 */
public interface AccountInitListService extends BaseService<AccountInitList> {

    /**
     * 初始化单据子表查询
     */
    PageData<AccountInitList> getPageData(int page, int rows, AccountInitList accountInitList);

    /**
     * 查询全部初始化单据子表数据
     */
    List<AccountInitList> getAllInitList(String initCode);

    /**
     * 删除初始化单据子表(根据出库单号)
     */
    void delByInitCode(String initCode);

}
