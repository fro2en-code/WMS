package wms.userRelation.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.beans.Attr;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.userRelation.UserLabel;

import its.base.service.BaseServiceImpl;
import wms.userRelation.service.UserLabelService;

/**
 * 人员公司实现
 *
 * @author fc
 *
 */
@Service
public class UserLabelServiceImpl extends BaseServiceImpl<UserLabel> implements UserLabelService {

	@Resource
	private Attr attr;

	@Override
	public ResultResp del(String id) {
		ResultResp resp = new ResultResp();
		try {
			UserLabel wh = getEntity(id);
			deleteEntity(wh);
			resp.setRetcode("0");
			resp.setRetmsg("删除成功！");
		} catch (Exception e) {
			resp.setRetcode("-1");
			resp.setRetmsg("删除失败！<br>" + e.getMessage());
		}
		return resp;
	}

	@Override
	public PageData<UserLabel> getPageData(int page, int rows, String userLoginname) {
		return getPageDataByBaseHql("From UserLabel where userLoginname=?", null, page, rows, userLoginname);
	}

	@Override
	public ResultResp save(UserLabel la) {
		ResultResp resp = new ResultResp();
		String id = la.getId();
		String check = "select count(*) From UserLabel Where userId= ?";
		List<Object> params = new ArrayList<>();
		params.add(la.getUserId());
		if (StringUtil.isEmpty(id)) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("用户" + la.getUserLoginname() + "已经绑定！");
			} else {
				la.setUpdateUser(null);
				saveEntity(la);
				resp.setRetcode("0");
				resp.setRetmsg("绑定成功！");
			}
		} else {// 更新
			check += " and tid <> ?";
			params.add(la.getTid());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("用户" + la.getUserLoginname() + "已经绑定！");
			} else {
				updateEntity(la);
				resp.setRetcode("0");
				resp.setRetmsg("修改绑定成功！");
			}
		}

		return resp;

	}

	@Override
	public String getLoginNameByTid(String tid) {
		UserLabel lable = (UserLabel) uniqueResult("From UserLabel where tid=?", tid);
		if (null != lable) {
			return lable.getUserLoginname();
		} else {
			return null;
		}
	}
}
