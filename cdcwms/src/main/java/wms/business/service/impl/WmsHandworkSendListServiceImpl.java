package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsHandworkSendList;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsHandworkSendListService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsHandworkSendListService")
public class WmsHandworkSendListServiceImpl extends BaseServiceImpl<WmsHandworkSendList>
		implements WmsHandworkSendListService {

	@Override
	public PageData<WmsHandworkSendList> getPageDataList(int page, int rows, WmsHandworkSendList wmsHandworkSendList) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("From WmsHandworkSendList where 1=1 ");
		if (!StringUtil.isEmpty(wmsHandworkSendList.getMapSheetNo())) {
			base_hql.append(" and mapSheetNo  = ?");
			params.add(wmsHandworkSendList.getMapSheetNo());
		}
		if (!StringUtil.isEmpty(wmsHandworkSendList.getWhCode())) {
			base_hql.append(" and whCode  = ?");
			params.add(wmsHandworkSendList.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By updateTime desc", page, rows, params);
	}

	@Override
	public ResultResp save(WmsHandworkSendList wmsHandworkSendList) {
		ResultResp resp = new ResultResp();
		if (StringUtil.isEmpty(wmsHandworkSendList.getId())) {// 新增
			saveEntity(wmsHandworkSendList);
			resp.setRetcode("0");
			resp.setRetmsg("新增发货明细单成功！");
		} else {// 修改
			updateEntity(wmsHandworkSendList);
			resp.setRetcode("0");
			resp.setRetmsg("修改发货明细单成功！");
		}
		return resp;
	}

	@Override
	public ResultResp del(WmsHandworkSendList wmsHandworkSendList) {
		ResultResp resp = new ResultResp();
		deleteEntity(wmsHandworkSendList);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功！");
		return resp;
	}

	@Override
	public List<WmsHandworkSendList> getWmsHandworkSendList(String mapSheetNo, String whCode) {
		return findEntityByHQL("from WmsHandworkSendList where mapSheetNo = ? and whCode=?", mapSheetNo, whCode);
	}

	@Override
	public List<WmsHandworkSendList> getInfo(String mapSheetNo, String gcode, String whCode) {
		return findEntityByHQL("from WmsHandworkSendList where mapSheetNo = ? and partNo = ? and whCode=?", mapSheetNo,
				gcode, whCode);
	}
}
