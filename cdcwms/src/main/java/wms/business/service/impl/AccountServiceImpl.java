package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsAccount;

import its.base.service.BaseServiceImpl;
import wms.business.service.AccountService;
import wms.business.service.TotalAccountService;

/**
 * 台账实现
 */
@Service("accountService")
public class AccountServiceImpl extends BaseServiceImpl<WmsAccount> implements AccountService {

	@Resource
	private TotalAccountService totalAccountService;

	/**
	 * 台账分页
	 */
	@Override
	public PageData<WmsAccount> getPageData(int page, int rows, WmsAccount account) {
		StringBuffer base_hql = new StringBuffer("From WmsAccount where 1=1 ");
		List<Serializable> params = new ArrayList<>();
		if (!StringUtil.isEmpty(account.getGcode())) {
			base_hql.append(" and gcode =? ");
			params.add(account.getGcode());
		}
		if (!StringUtil.isEmpty(account.getGtype())) {
			base_hql.append(" and gtype =? ");
			params.add(account.getGtype());
		}

		if (!StringUtil.isEmpty(account.getOraCode())) {
			base_hql.append(" and oraCode =? ");
			params.add(account.getOraCode());
		}
		if (!StringUtil.isEmpty(account.getWhCode())) {
			base_hql.append(" and whCode =? ");
			params.add(account.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By inTime desc", page, rows, params);
	}

}
