package wms.web.business.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.AccountInitList;

import wms.business.biz.AccountBiz;

/**
 * 初使化单据子表
 */
@Controller
@RequestMapping("/accountInitList")
public class AccountInitListAction extends BaseAction {

    @Resource
    private AccountBiz accountBiz;

    /**
     * 查询初使化单据子表分页
     *
     * @return
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<AccountInitList> list(HttpServletResponse resp, int page, int rows,
            AccountInitList accountInitList) {
        return accountBiz.getPageData(page, rows, accountInitList);
    }

    /**
     * 删除初使化单据子表(根据主键编号删除)
     */
    @RequestMapping("del.action")
    @ResponseBody
    public ResultResp del(HttpServletResponse resp, AccountInitList accountInitList) {
        return accountBiz.delAccountInitList(accountInitList);
    }
}
