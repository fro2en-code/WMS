package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.AccountInit;
import com.ymt.utils.SerialNumberUtils;

import its.base.service.BaseServiceImpl;
import wms.business.service.AccountInitService;

/**
 * 台账初始化主表实现
 */
@Service("accountInitService")
public class AccountInitServiceImpl extends BaseServiceImpl<AccountInit> implements AccountInitService {
	private SerialNumberUtils utils = new SerialNumberUtils() {

		@Override
		public boolean isExpired(Date startTime) {
			return new Date().getTime() / (1000L * 60 * 60 * 24) == startTime.getTime() / (1000L * 60 * 60 * 24);// 是否为同一天
		}
	};

	/**
	 * 台账初始化主表分页查询
	 */
	@Override
	public PageData<AccountInit> getPageData(int page, int rows, AccountInit account) {
		if (!StringUtil.isEmpty(account.getWhCode())) {
			return getPageDataByBaseHql("From AccountInit where 1=1 and whCode = '" + account.getWhCode() + "'",
					" Order By initTime desc", page, rows, new ArrayList<Serializable>());
		} else {
			return getPageDataByBaseHql("From AccountInit where 1=1 ", " Order By initTime desc", page, rows,
					new ArrayList<Serializable>());
		}
	}

	/**
	 * 保存、修改台账初始化主表
	 */
	@Override
	public ResultResp save(AccountInit account) {
		if (StringUtil.isEmpty(account.getId())) {// 新增
			account.setInitCode(this.generateTZNum());
			account.setStatus(0);
			account.setInitTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
			account.setUpdateTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
			saveEntity(account);
			return new ResultResp(ResultResp.SUCCESS_CODE, "新增初始化单据操作成功");
		} else {// 更新
			account.setUpdateTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
			updateEntity(account);
			return new ResultResp(ResultResp.SUCCESS_CODE, "修改初始化单据操作成功");
		}
	}

	// 生成初始化单据编号 TZ
	@SuppressWarnings("unchecked")
	private String generateTZNum() {
		return "TZ" + StringUtil.getCurStringDate("yyyy-MM-dd-") + utils.getSerialNumber(5);

	}

	/**
	 * 根据台账初始化主表编号获取出初始化实体
	 */
	@Override
	public AccountInit getAccountInit(String initCode) {
		return (AccountInit) uniqueResult("From AccountInit where initCode=?", initCode);
	}
}
