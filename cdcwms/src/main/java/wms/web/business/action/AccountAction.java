package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.wms.business.WmsAccount;
import com.ymt.utils.ExcelUtils;
import com.ymt.utils.ExcelUtils.ExportExcelHelper;

import wms.business.biz.AccountBiz;

/**
 * 台账
 */
@Controller
@RequestMapping("/account")
public class AccountAction extends BaseAction {

    @Resource
    private AccountBiz accountBiz;

    /**
     * 查询台账表分页
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<WmsAccount> list(HttpSession session, int page, int rows, WmsAccount account) {
        account.setWhCode(getBindWhCode(session));
        return accountBiz.getPageData(page, rows, account);
    }

    @RequestMapping("/export.action")
    @ResponseBody
    public void export(HttpServletResponse resp, HttpSession session, final String keyNo, final WmsAccount account) {
        account.setWhCode(getBindWhCode(session));
        ExportExcelHelper helper = new ExcelUtils.ExportExcelHelper() {

            @Override
            public PageData<? extends BaseModel> getPageDate(int page, int size) throws Exception {
                return accountBiz.getPageData(page, ExcelUtils.pageSize, account);
            }

            @Override
            public String getKey() {
                return keyNo;
            }
        };
        helper.run(resp);
    }
}
