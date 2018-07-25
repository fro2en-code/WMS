package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsLesSendList;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsLesSendListService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsLesSendListService")
public class WmsLesSendListServiceImpl extends BaseServiceImpl<WmsLesSendList> implements WmsLesSendListService {

	@Override
	public ResultResp del(WmsLesSendList wmsLesSendList) {
		ResultResp resp = new ResultResp();
		deleteEntity(wmsLesSendList);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功！");
		return resp;
	}

	@Override
	public void delByMapSheetNo(String mapSheetNo, String whCode) {
		batchHandle("delete from WmsLesSendList where mapSheetNo = ? and whCode =?", mapSheetNo, whCode);
	}

	/**
	 * 主表
	 *
	 * @param page
	 * @param rows
	 * @param wmsLesSend
	 * @return
	 */
	@Override
	public PageData<WmsLesSendList> getPageDataList(int page, int rows, WmsLesSendList wmsLesSendList) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("From WmsLesSendList where whCode=? ");
		params.add(wmsLesSendList.getWhCode());
		if (!StringUtil.isEmpty(wmsLesSendList.getMapSheetNo())) {
			base_hql.append(" and mapSheetNo = ?");
			params.add(wmsLesSendList.getMapSheetNo());
		}
		return getPageDataByBaseHql(base_hql.toString(), " order by sxCardNo", page, rows, params);
	}

	@Override
	public List<WmsLesSendList> getWmsLesSendList(String mapSheetNo, String whCode) {
		return findEntityByHQL("from WmsLesSendList where mapSheetNo = ? and whCode=?", mapSheetNo, whCode);
	}

	@Override
	public List<WmsLesSendList> getWmsLesSendListBySXCarid(String sxCarid, String whCode) {
		return findEntityByHQL("from WmsLesSendList where sxCardNo = ? and whCode=?", sxCarid, whCode);
	}

	@Override
	public ResultResp save(WmsLesSendList wmsLesSendList) {
		ResultResp resp = new ResultResp();
		if (StringUtil.isEmpty(wmsLesSendList.getId())) {// 新增
			resp.setBillId((String) saveEntity(wmsLesSendList));
			resp.setRetcode("0");
			resp.setRetmsg("新增发货明细单成功！");
		} else {// 修改
			updateEntity(wmsLesSendList);
			resp.setRetcode("0");
			resp.setRetmsg("修改发货明细单成功！");
		}
		return resp;
	}

	@Override
	public List<WmsLesSendList> getInfo(String mapSheetNo, String gcode, String whCode) {
		return findEntityByHQL("from WmsLesSendList where mapSheetNo = ? and partNo = ? and whCode=?", mapSheetNo,
				gcode, whCode);
	}
}
