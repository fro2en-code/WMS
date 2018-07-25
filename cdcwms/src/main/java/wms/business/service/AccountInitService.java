package wms.business.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.AccountInit;

import its.base.service.BaseService;

/**
 * 台账初始化主表接口
 *
 * @author wlx
 */
public interface AccountInitService extends BaseService<AccountInit> {

    /**
     * 台账初始化主表分页查询
     */
    PageData<AccountInit> getPageData(int page, int rows, AccountInit account);

    /**
     * 保存、修改台账初始化主表
     */
    ResultResp save(AccountInit account);

    /**
     * 根据台账初始化主表编号获取出初始化实体
     */
    AccountInit getAccountInit(String initCode);

}
