package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;
import com.wms.business.SalesReturnAccountList;

import its.base.service.BaseServiceImpl;
import wms.business.service.SalesReturnAccountListService;

@Service("salesReturnAccountListService")
public class SalesReturnAccountListServiceImpl extends BaseServiceImpl<SalesReturnAccountList>
		implements SalesReturnAccountListService {

	@Override
	public PageData<SalesReturnAccountList> getPageData(int rows, int page, HttpSession session,
			SalesReturnAccountList salesReturnAccountList) {
		StringBuffer base_hql = new StringBuffer("From SalesReturnAccountList where 1=1 ");
		List<Serializable> params = new ArrayList<>();
		if (!StringUtil.isEmpty(salesReturnAccountList.getGcode())) {
			base_hql.append(" and gcode =? ");
			params.add(salesReturnAccountList.getGcode());
		}
		if (!StringUtil.isEmpty(salesReturnAccountList.getGtype())) {
			base_hql.append(" and gtype =? ");
			params.add(salesReturnAccountList.getGtype());
		}

		if (!StringUtil.isEmpty(salesReturnAccountList.getOraCode())) {
			base_hql.append(" and oraCode =? ");
			params.add(salesReturnAccountList.getOraCode());
		}
		if (!StringUtil.isEmpty(salesReturnAccountList.getWhCode())) {
			base_hql.append(" and whCode =? ");
			params.add(salesReturnAccountList.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By inTime desc", page, rows, params);

	}

	@Override
	public void save(SalesReturnAccountList salesReturnAccountList) {
		saveEntity(salesReturnAccountList);
	}

}
