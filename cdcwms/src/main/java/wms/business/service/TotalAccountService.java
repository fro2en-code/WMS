package wms.business.service;

import com.plat.common.page.PageData;
import com.wms.business.WmsTotalAccount;

import its.base.service.BaseService;

/**
 * 总账查询接口
 *
 * @author wlx
 */
public interface TotalAccountService extends BaseService<WmsTotalAccount> {

    /**
     * 总账分页查询
     */
    PageData<WmsTotalAccount> getPageData(int page, int rows, WmsTotalAccount totalaccount, String... companys);

    /**
     * 根据仓库代码、供应商代码、物料代码,零件用途 查询总账实体
     */
    WmsTotalAccount getTotalAccount(WmsTotalAccount totalAccount);
}
