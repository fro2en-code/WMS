package wms.business.service.impl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsSupplierSend;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsSupplierSendService;

/**
 * 
 * @author Administrator
 *
 */
@Service("wmsSupplierSendService")
public class WmsSupplierSendServiceImpl extends BaseServiceImpl<WmsSupplierSend> implements WmsSupplierSendService {

	public PageData<WmsSupplierSend> getPageDataList(int page, int rows, WmsSupplierSend supplierSend) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("From WmsSupplierSend where 1=1 ");
		if (!StringUtil.isEmpty(supplierSend.getMapSheetNo())) {
			base_hql.append(" and mapSheetNo like ?");
			params.add("%" + supplierSend.getMapSheetNo() + "%");
		}
		if (!StringUtil.isEmpty(supplierSend.getWhCode())) {
			base_hql.append(" and whCode = ?");
			params.add(supplierSend.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By updateTime desc", page, rows, params);

	}

	@Override
	public ResultResp save(WmsSupplierSend supplierSend) {
		ResultResp resp = new ResultResp();
		String check = "select count(*) from WmsSupplierSend where mapSheetNo = ? and whCode = ?";
		List<Object> params = new ArrayList<>();
		params.add(supplierSend.getMapSheetNo());
		params.add(supplierSend.getWhCode());
		if (StringUtil.isEmpty(supplierSend.getId())) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("配送单号" + supplierSend.getMapSheetNo() + "已经存在！");
			} else {
				supplierSend.setStatus(BaseModel.INT_INIT);
				resp.setBillId((String) saveEntity(supplierSend));
				resp.setRetcode("0");
				resp.setRetmsg("新增发货单成功！");
			}
		} else {// 更新
			check += "and id <> ?";
			params.add(supplierSend.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("配送单号" + supplierSend.getMapSheetNo() + "已经存在！");
			} else {
				updateEntity(supplierSend);
				resp.setRetcode("0");
				resp.setRetmsg("修改发货单成功！");
			}
		}
		return resp;

	}

	@Override
	public WmsSupplierSend getWmsSupplierSendByMapSheetNo(String mapSheetNo, String whCode) {
		return (WmsSupplierSend) uniqueResult("from WmsSupplierSend where mapSheetNo = ? and whCode = ?", mapSheetNo,
				whCode);
	}

}
