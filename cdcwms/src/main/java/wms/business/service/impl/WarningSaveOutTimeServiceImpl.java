package wms.business.service.impl;

import its.base.service.BaseServiceImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import wms.business.service.WarningSaveOutTimeService;

import com.plat.common.page.PageData;
import com.plat.common.utils.Function;
import com.wms.business.WarningSaveOutTime;

@Service("warningSaveOutTimeService")
public class WarningSaveOutTimeServiceImpl extends
		BaseServiceImpl<WarningSaveOutTime> implements
		WarningSaveOutTimeService {

	@SuppressWarnings("unchecked")
	@Override
	public PageData<Map<String, Object>> queryWarningSaveOutTime(int page,
			int rows, String listsql, String countsql, List<Serializable> params) {
		return (PageData<Map<String, Object>>) getPageListBySql(listsql,
				countsql, new Function<SQLQuery>() {

					@Override
					public void run(SQLQuery t) {
						t.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					}
				}, page, rows, params.toArray(new Serializable[params.size()]));
	}
}
