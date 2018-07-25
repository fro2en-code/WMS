package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsLesReceive;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsLesReceiveService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsLesReceiveService")
public class WmsLesReceiveServiceImpl extends BaseServiceImpl<WmsLesReceive> implements WmsLesReceiveService {

	@Override
	public ResultResp del(WmsLesReceive wmsLesReceive) {
		ResultResp resp = new ResultResp();
		if (BaseModel.INT_INIT.equals(wmsLesReceive.getStatus())) {
			deleteEntity(wmsLesReceive);
			resp.setRetcode("0");
			resp.setRetmsg("删除成功！");
		} else {
			resp.setRetcode("-1");
			resp.setRetmsg("该收货单已经生成任务,无法修改!");
		}
		return resp;
	}

	@Override
	public WmsLesReceive getLesReceiveByMapSheetNo(String mapSheetNo, String whCode) {
		return (WmsLesReceive) uniqueResult("from WmsLesReceive where mapSheetNo = ? and whCode = ?", mapSheetNo,
				whCode);
	}

	/**
	 * 分页
	 */
	@Override
	public PageData<WmsLesReceive> getPageData(int page, int rows, WmsLesReceive wmsLesReceive) {
		return getPageDataByBaseHql("From WmsLesReceive where 1=1 ", null, page, rows, new ArrayList<Serializable>());
	}

	@Override
	public PageData<WmsLesReceive> getpageDataLesReceive(int page, int rows, WmsLesReceive wmsLesReceive) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from WmsLesReceive where 1=1");
		if (!StringUtil.isEmpty(wmsLesReceive.getMapSheetNo())) {
			base_hql.append("and mapSheetNo like ?");
			params.add("%" + wmsLesReceive.getMapSheetNo() + "%");
		}
		if (!StringUtil.isEmpty(wmsLesReceive.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(wmsLesReceive.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By updateTime desc", page, rows, params);

	}

	@Override
	public ResultResp save(WmsLesReceive wmsLesReceive) {
		ResultResp resp = new ResultResp();
		String check = "select count(*) from WmsLesReceive where mapSheetNo = ? and whCode = ?";
		List<Object> params = new ArrayList<>();
		params.add(wmsLesReceive.getMapSheetNo());
		params.add(wmsLesReceive.getWhCode());
		if (StringUtil.isEmpty(wmsLesReceive.getId())) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("配送单号" + wmsLesReceive.getMapSheetNo() + "已经存在！");
			} else {
				wmsLesReceive.setStatus(BaseModel.INT_INIT);
				resp.setBillId((String) saveEntity(wmsLesReceive));
				resp.setRetcode("0");
				resp.setRetmsg("新增收货单成功！");
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(wmsLesReceive.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("配送单号" + wmsLesReceive.getMapSheetNo() + "已经存在！");
			} else {
				updateEntity(wmsLesReceive);
				resp.setRetcode("0");
				resp.setRetmsg("修改收货单成功！");
			}
		}
		return resp;
	}

	@Override
	public void updateLesReceive(WmsLesReceive wmsLesReceive) {
		updateEntity(wmsLesReceive);
	}
}
