package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsTotalAccount;

import its.base.service.BaseServiceImpl;
import wms.business.service.TotalAccountService;

@Service("totalAccountService")
public class TotalAccountServiceImpl extends BaseServiceImpl<WmsTotalAccount> implements TotalAccountService {

    /**
     * 总账分页查询
     */
    @Override
    public PageData<WmsTotalAccount> getPageData(int page, int rows, WmsTotalAccount totalaccount, String... companys) {
        StringBuilder base_hql = new StringBuilder();
        List<Serializable> params = new ArrayList<>();
        base_hql.append("From WmsTotalAccount where 1=1 ");
        if (!StringUtil.isEmpty(totalaccount.getOraCode())) {
            base_hql.append(" and oraCode like ? ");
            params.add("%" + totalaccount.getOraCode() + "%");
        }
        if (!StringUtil.isEmpty(totalaccount.getGcode())) {
            base_hql.append(" And gcode like ? ");
            params.add("%" + totalaccount.getGcode() + "%");
        }
        if (!StringUtil.isEmpty(totalaccount.getWhCode())) {
            base_hql.append(" And whCode = ? ");
            params.add(totalaccount.getWhCode());
        }
        if (null != companys && companys.length > 0) {
            base_hql.append(" And oraName in ( ");
            for (String company : companys) {
                base_hql.append("?,");
                params.add(company);
            }
            base_hql.deleteCharAt(base_hql.length() - 1);
            base_hql.append(")");
        }
        return getPageDataByBaseHql(base_hql.toString(), " Order By gcode", page, rows, params);
    }

    /**
     * 根据仓库代码、供应商代码、物料代码查询台账实体
     */
    @Override
    public WmsTotalAccount getTotalAccount(WmsTotalAccount totalAccount) {
        return (WmsTotalAccount) uniqueResult(
                "From WmsTotalAccount Where whCode=? And oraCode = ? And gcode=? and gtype=?", totalAccount.getWhCode(),
                totalAccount.getOraCode(), totalAccount.getGcode(), totalAccount.getGtype());
    }

}
