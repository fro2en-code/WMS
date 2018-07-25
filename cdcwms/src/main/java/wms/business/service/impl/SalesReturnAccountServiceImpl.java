package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;
import com.wms.business.SalesReturnAccount;

import its.base.service.BaseServiceImpl;
import wms.business.service.SalesReturnAccountService;

@Service("salesReturnAccountService")
public class SalesReturnAccountServiceImpl extends BaseServiceImpl<SalesReturnAccount>
		implements SalesReturnAccountService {

	@Override
	public PageData<SalesReturnAccount> getPageData(int rows, int page, HttpSession session,
			SalesReturnAccount salesReturnAccount) {
		StringBuffer base_hql = new StringBuffer("From SalesReturnAccount where 1=1 ");
		List<Serializable> params = new ArrayList<>();
		if (!StringUtil.isEmpty(salesReturnAccount.getGcode())) {
			base_hql.append(" and gcode like ?");
			params.add("%" + salesReturnAccount.getGcode() + "%");
		}
		if (!StringUtil.isEmpty(salesReturnAccount.getOraCode())) {
			base_hql.append(" and oraCode like ?");
			params.add("%" + salesReturnAccount.getOraCode() + "%");
		}
		if (!StringUtil.isEmpty(salesReturnAccount.getWhCode())) {
			base_hql.append(" and whCode = ?");
			params.add(salesReturnAccount.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By gcode desc", page, rows, params);
	}

	@Override
	public SalesReturnAccount getSalesReturnAccount(String gcode, String gtype, String oraCode, String whCode) {
		return (SalesReturnAccount) uniqueResult(
				"from SalesReturnAccount where gcode = ? and gtype = ? and oraCode = ? and whCode = ?", gcode, gtype,
				oraCode, whCode);
	}

	@Override
	public void save(SalesReturnAccount salesReturnAccount) {
		saveEntity(salesReturnAccount);
	}

	@Override
	public void update(SalesReturnAccount salesReturnAccount) {
		updateEntity(salesReturnAccount);
	}

}
