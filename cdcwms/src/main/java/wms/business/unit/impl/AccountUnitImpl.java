package wms.business.unit.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.AccountInit;
import com.wms.business.AccountInitList;
import com.wms.business.WmsAccount;
import com.wms.business.WmsTotalAccount;
import com.wms.warehouse.WmsObject;

import wms.business.service.AccountInitListService;
import wms.business.service.AccountInitService;
import wms.business.service.AccountService;
import wms.business.service.TotalAccountService;
import wms.business.service.WmsObjectService;
import wms.business.service.WmsStockService;
import wms.business.unit.AccountUnit;
import wms.warehouse.service.WarehouseService;

@Service("accountUnit")
public class AccountUnitImpl implements AccountUnit {
    @Resource
    private AccountInitListService accountInitListService;
    @Resource
    private AccountInitService accountInitService;
    @Resource
    private AccountService accountService;
    @Resource
    private TotalAccountService totalAccountService;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private WmsObjectService wmsObjectService;
    @Resource
    private WmsStockService wmsStockService;

    @Override
    public ResultResp delAccountInit(AccountInit accountInit) {
        accountInit = accountInitService.getEntity(accountInit.getId());
        Integer stuts = accountInit.getStatus();
        if ((stuts == null) || stuts == 0) {
            accountInitService.deleteEntity(accountInit);
            accountInitListService.delByInitCode(accountInit.getInitCode());
            return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
        } else {
            return new ResultResp(ResultResp.ERROR_CODE, "删除失败,该出单据已确认!");
        }

    }

    @Override
    public ResultResp delAccountInitList(AccountInitList accountInitList) {
        if (!StringUtils.isEmpty(accountInitList.getId())) {// 如果有ID则根据ID删除
            delAccountInitListByID(accountInitList);
        } else if (!StringUtils.isEmpty(accountInitList.getInitCode())) {// 如果有initCode,根据initCode删除
            delAccountInitListByCode(accountInitList);
        } else {
            return new ResultResp(ResultResp.ERROR_CODE, "没有可删除的数据");
        }
        return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
    }

    /**
     * 根据initCode 删除对象
     */
    public ResultResp delAccountInitListByCode(AccountInitList accountInitList) {
        AccountInit accountInit = accountInitService.getAccountInit(accountInitList.getInitCode());
        if (accountInit == null) {
            return new ResultResp(ResultResp.ERROR_CODE, "初始化单据不存在！");
        }
        if (accountInit.getStatus() == 1) {
            return new ResultResp(ResultResp.ERROR_CODE, "该单据已确认不可在编辑！");
        }
        accountInitListService.delByInitCode(accountInitList.getInitCode());
        return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
    }

    /**
     * 根据ID 删除对象
     */
    private ResultResp delAccountInitListByID(AccountInitList accountInitList) {
        accountInitList = accountInitListService.getEntity(accountInitList.getId());
        AccountInit accountInit = accountInitService.getAccountInit(accountInitList.getInitCode());
        if (accountInit == null) {
            return new ResultResp(ResultResp.ERROR_CODE, "初始化单据不存在！");
        }
        if (accountInit.getStatus() == 1) {
            return new ResultResp(ResultResp.ERROR_CODE, "该单据已确认不可在编辑！");
        }
        accountInitListService.deleteEntity(accountInitList);
        return new ResultResp(ResultResp.SUCCESS_CODE, "删除成功!");
    }

    @Override
    public void delByInitCode(String initCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public AccountInit getAccountInitById(String id) {
        return accountInitService.getEntity(id);
    }

    @Override
    public AccountInit getAccountInitByInitCode(String initCode) {
        return accountInitService.getAccountInit(initCode);
    }

    @Override
    public List<AccountInitList> getAllInitList(String initCode) {
        return accountInitListService.getAllInitList(initCode);
    }

    @Override
    public PageData<AccountInit> getPageData(int page, int rows, AccountInit account) {
        PageData<AccountInit> pageData = accountInitService.getPageData(page, rows, account);
        List<AccountInit> list = pageData.getRows();
        Map<String, String> wareHouseMap = warehouseService.getAllWmsWarehouseMap();
        for (AccountInit init : list) {
            init.setWhName(wareHouseMap.get(init.getWhCode()));
        }
        return pageData;
    }

    @Override
    public PageData<AccountInitList> getPageData(int page, int rows, AccountInitList accountInitList) {
        return accountInitListService.getPageData(page, rows, accountInitList);
    }

    @Override
    public PageData<WmsAccount> getPageData(int page, int rows, WmsAccount account) {
        return accountService.getPageData(page, rows, account);
    }

    /**
     * 查询零件管理分页
     */
    @Override
    public PageData<WmsObject> getPageData(int page, int rows, WmsObject wmsObject) {
        return wmsObjectService.getPageData(page, rows, wmsObject);
    }

    @Override
    public PageData<WmsTotalAccount> getPageData(int page, int rows, WmsTotalAccount totalaccount, String... companys) {
        return totalAccountService.getPageData(page, rows, totalaccount, companys);
    }

    @Override
    public ResultResp save(AccountInit account) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultResp saveAccountInit(AccountInit account) {
        return accountInitService.save(account);
    }

    @Override
    public void updateEntity(AccountInit accountInit) {
        accountInitService.updateEntity(accountInit);
    }

}
