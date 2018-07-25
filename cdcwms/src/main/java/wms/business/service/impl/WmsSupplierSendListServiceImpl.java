package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsSupplierSendList;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsSupplierSendListService;

@Service("wmsSupplierSendListService")
public class WmsSupplierSendListServiceImpl extends BaseServiceImpl<WmsSupplierSendList>
		implements WmsSupplierSendListService {

	public void delByMapSheetNo(String mapSheetNo, String whCode) {
		batchHandle("delete from WmsSupplierSendList where mapSheetNo = ? and whCode =?", mapSheetNo, whCode);
	}

	@Override
	public PageData<WmsSupplierSendList> getPageDataList(int page, int rows, WmsSupplierSendList wmsSupplierSendList) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("From WmsSupplierSendList where whCode=? ");
		params.add(wmsSupplierSendList.getWhCode());
		if (!StringUtil.isEmpty(wmsSupplierSendList.getMapSheetNo())) {
			base_hql.append(" and mapSheetNo = ?");
			params.add(wmsSupplierSendList.getMapSheetNo());
		}
		return getPageDataByBaseHql(base_hql.toString(), " order by sxCardNo", page, rows, params);
	}

	@Override
	public ResultResp save(WmsSupplierSendList wmsSupplierSendList) {
		ResultResp resp = new ResultResp();
		if (StringUtil.isEmpty(wmsSupplierSendList.getId())) {
			saveEntity(wmsSupplierSendList);
			resp.setRetcode("0");
			resp.setRetmsg("新增发货明细单成功！");
		} else {
			updateEntity(wmsSupplierSendList);
			resp.setRetcode("0");
			resp.setRetmsg("修改发货明细单成功！");
		}
		return resp;
	}

	@Override
	public ResultResp delete(WmsSupplierSendList wmsSupplierSendList) {
		ResultResp resp = new ResultResp();
		deleteEntity(wmsSupplierSendList);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功！");
		return resp;
	}

	@Override
	public List<WmsSupplierSendList> getWmsSupplierSendList(String mapSheetNo, String whCode) {
		return findEntityByHQL("from WmsSupplierSendList where mapSheetNo = ? and whCode = ?", mapSheetNo, whCode);
	}
}
