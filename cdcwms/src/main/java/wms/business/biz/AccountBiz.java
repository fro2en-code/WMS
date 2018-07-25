package wms.business.biz;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.AccountInit;
import com.wms.business.AccountInitList;
import com.wms.business.WmsAccount;
import com.wms.business.WmsTotalAccount;
import com.wms.warehouse.WmsObject;

/**
 * 台账接口实现
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年3月14日
 */
public interface AccountBiz {
    ResultResp saveAccountInit(AccountInit account);

    ResultResp delAccountInit(AccountInit accountInit);

    ResultResp delAccountInitList(AccountInitList accountInitList);

    PageData<AccountInit> getPageData(int page, int rows, AccountInit account);

    PageData<AccountInitList> getPageData(int page, int rows, AccountInitList accountInitList);

    PageData<WmsAccount> getPageData(int page, int rows, WmsAccount account);

    PageData<WmsObject> getPageData(int page, int rows, WmsObject wmsObject);

    PageData<WmsTotalAccount> getPageData(int page, int rows, WmsTotalAccount totalaccount, String... companys);

    /**
     * 审核台账初始化单据
     */
    ResultResp updateSureAccountInit(AccountInit accountInit);

}
