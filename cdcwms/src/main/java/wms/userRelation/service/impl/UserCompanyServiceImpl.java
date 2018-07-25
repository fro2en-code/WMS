package wms.userRelation.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.userRelation.UserCompany;

import its.base.service.BaseServiceImpl;
import wms.userRelation.service.UserCompanyService;

@Service("userCompanyService")
public class UserCompanyServiceImpl extends BaseServiceImpl<UserCompany> implements UserCompanyService {

	@Override
	public PageData<UserCompany> getPageData(int page, int rows, UserCompany userCompany) {
		List<Serializable> list = new ArrayList<>();
		list.add(userCompany.getUserLoginname());
		list.add(userCompany.getWhCode());
		return getPageDataByBaseHql("from UserCompany where userLoginname=? and whCode = ?", null, page, rows, list);
	}

	@Override
	public List<UserCompany> getUserCompany(UserCompany userCompany) {
		StringBuilder hql = new StringBuilder("from UserCompany where userLoginname=?");
		List<Serializable> list = new ArrayList<>();
		list.add(userCompany.getUserLoginname());
		if (null != userCompany.getCompanyType()) {
			hql.append(" and companyType=? ");
			list.add(userCompany.getCompanyType());
		}
		if (!StringUtil.isEmpty(userCompany.getWhCode())) {
			hql.append(" and whCode = ?");
			list.add(userCompany.getWhCode());
		}
		return findEntityByHQL(hql.toString(), list.toArray(new Serializable[list.size()]));
	}

	@Override
	public ResultResp save(UserCompany userCompany) {
		ResultResp resp = new ResultResp();
		String check = "select count(*) from UserCompany where userLoginname = ? and  companyid = ?";
		List<Object> params = new ArrayList<>();
		params.add(userCompany.getUserLoginname());
		params.add(userCompany.getCompanyid());
		if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
			resp.setRetcode("-1");
			resp.setRetmsg("该用户已经绑定了" + userCompany.getCompanyName());
		} else {
			saveEntity(userCompany);
			resp.setRetcode("0");
			resp.setRetmsg("绑定成功");
		}
		return resp;
	}

	@Override
	public List<UserCompany> getCompanyByName(String UserName, String whCode) {
		return findEntityByHQL(" from UserCompany where userLoginname = ? and whCode = ?", UserName, whCode);
	}

}
