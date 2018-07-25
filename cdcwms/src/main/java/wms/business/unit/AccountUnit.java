package wms.business.unit;

import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.AccountInit;
import com.wms.business.AccountInitList;
import com.wms.business.WmsAccount;
import com.wms.business.WmsTotalAccount;
import com.wms.warehouse.WmsObject;

/**
 * 账务单元
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年3月22日
 */
public interface AccountUnit {
    ResultResp delAccountInit(AccountInit accountInit);

    ResultResp delAccountInitList(AccountInitList accountInitList);

    /**
     * 删除初始化单据子表(根据出库单号)
     */
    void delByInitCode(String initCode);

    AccountInit getAccountInitById(String id);

    /**
     * 根据台账初始化主表编号获取出初始化实体
     */
    AccountInit getAccountInitByInitCode(String initCode);

    /**
     * 查询全部初始化单据子表数据
     */
    List<AccountInitList> getAllInitList(String initCode);

    PageData<AccountInit> getPageData(int page, int rows, AccountInit account);

    PageData<AccountInitList> getPageData(int page, int rows, AccountInitList accountInitList);

    PageData<WmsAccount> getPageData(int page, int rows, WmsAccount account);

    /**
     *
     * 查询零件管理分页
     *
     * @param page
     * @param rows
     * @param wmsObject
     * @return
     */
    PageData<WmsObject> getPageData(int page, int rows, WmsObject wmsObject);

    PageData<WmsTotalAccount> getPageData(int page, int rows, WmsTotalAccount totalaccount, String... companys);

    /**
     * 保存、修改台账初始化主表
     */
    ResultResp save(AccountInit account);

    ResultResp saveAccountInit(AccountInit account);

    void updateEntity(AccountInit accountInit);
}
