package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsHandworkReceive;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsHandworkReceiveService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsHandworkReceiveService")
public class WmsHandworkReceiveServiceImpl extends BaseServiceImpl<WmsHandworkReceive>
		implements WmsHandworkReceiveService {

	/**
	 * 分页
	 */
	@Override
	public PageData<WmsHandworkReceive> getPageData(int page, int rows, WmsHandworkReceive wmsHandworkReceive) {
		return getPageDataByBaseHql("From WmsHandworkReceive where 1=1 ", null, page, rows,
				new ArrayList<Serializable>());
	}

	@Override
	public PageData<WmsHandworkReceive> getPageDataList(int page, int rows, WmsHandworkReceive wmsHandworkReceive) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("From WmsHandworkReceive where 1=1 ");
		if (!StringUtil.isEmpty(wmsHandworkReceive.getMapSheetNo())) {
			base_hql.append(" and mapSheetNo like ? ");
			params.add("%" + wmsHandworkReceive.getMapSheetNo() + "%");
		}
		if (!StringUtil.isEmpty(wmsHandworkReceive.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(wmsHandworkReceive.getWhCode());
		}

		return getPageDataByBaseHql(base_hql.toString(), " Order By updateTime desc", page, rows, params);
	}

	@Override
	public ResultResp save(WmsHandworkReceive wmsHandworkReceive) {
		ResultResp resp = new ResultResp();
		String check = "select count(*) from WmsHandworkReceive where mapSheetNo = ? and whCode = ?";
		List<Object> params = new ArrayList<>();
		params.add(wmsHandworkReceive.getMapSheetNo());
		params.add(wmsHandworkReceive.getWhCode());
		if (StringUtil.isEmpty(wmsHandworkReceive.getId())) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("配送单号" + wmsHandworkReceive.getMapSheetNo() + "已经存在！");
			} else {
				wmsHandworkReceive.setStatus(BaseModel.INT_INIT);
				saveEntity(wmsHandworkReceive);
				resp.setRetcode("0");
				resp.setRetmsg("新增收货单成功！");
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(wmsHandworkReceive.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("配送单号" + wmsHandworkReceive.getMapSheetNo() + "已经存在！");
			} else {
				updateEntity(wmsHandworkReceive);
				resp.setRetcode("0");
				resp.setRetmsg("修改收货单成功！");
			}

		}
		return resp;
	}

	@Override
	public ResultResp del(WmsHandworkReceive wmsHandworkReceive) {
		ResultResp resp = new ResultResp();
		deleteEntity(wmsHandworkReceive);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功！");
		return resp;
	}

	@Override
	public void updateHandworkReceive(WmsHandworkReceive wmsHandworkReceive) {
		updateEntity(wmsHandworkReceive);
	}

	@Override
	public WmsHandworkReceive getReceiveByMapSheetNo(String mapSheetNo, String whCode) {
		return (WmsHandworkReceive) uniqueResult("from WmsHandworkReceive where mapSheetNo = ? and whCode = ?",
				mapSheetNo, whCode);
	}

}
