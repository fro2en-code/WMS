package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsHandworkSend;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsHandworkSendService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsHandworkSendService")
public class WmsHandworkSendServiceImpl extends BaseServiceImpl<WmsHandworkSend> implements WmsHandworkSendService {

	/**
	 * 分页
	 */
	@Override
	public PageData<WmsHandworkSend> getPageDataList(int page, int rows, WmsHandworkSend wmsHandworkSend) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("From WmsHandworkSend where 1=1 ");
		if (!StringUtil.isEmpty(wmsHandworkSend.getMapSheetNo())) {
			base_hql.append(" and mapSheetNo like '%" + wmsHandworkSend.getMapSheetNo() + "%'");
		}
		if (!StringUtil.isEmpty(wmsHandworkSend.getOriginalNo())) {
			base_hql.append(" and originalNo like ? ");
			params.add("%" + wmsHandworkSend.getOriginalNo() + "%");
		}
		if (wmsHandworkSend.getStatus() != null) {
			base_hql.append("and status = ?");
			params.add(wmsHandworkSend.getStatus());
		}
		if (!StringUtil.isEmpty(wmsHandworkSend.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(wmsHandworkSend.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By updateTime desc", page, rows, params);
	}

	@Override
	public ResultResp save(WmsHandworkSend wmsHandworkSend) {
		ResultResp resp = new ResultResp();
		String check = "select count(*) from WmsHandworkSend where mapSheetNo = ? and whCode = ?";
		List<Object> params = new ArrayList<>();
		params.add(wmsHandworkSend.getMapSheetNo());
		params.add(wmsHandworkSend.getWhCode());
		if (StringUtil.isEmpty(wmsHandworkSend.getId())) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("配送单号" + wmsHandworkSend.getMapSheetNo() + "已经存在！");
			} else {
				wmsHandworkSend.setStatus(BaseModel.INT_INIT);
				saveEntity(wmsHandworkSend);
				resp.setRetcode("0");
				resp.setRetmsg("新增发货单成功！");
			}
		} else {// 更新
			check += "and id <> ?";
			params.add(wmsHandworkSend.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("配送单号" + wmsHandworkSend.getMapSheetNo() + "已经存在！");
			} else {
				updateEntity(wmsHandworkSend);
				resp.setRetcode("0");
				resp.setRetmsg("修改发货单成功！");
			}

		}
		return resp;
	}

	@Override
	public ResultResp del(WmsHandworkSend wmsHandworkSend) {
		ResultResp resp = new ResultResp();
		deleteEntity(wmsHandworkSend);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功！");
		return resp;
	}

	/**
	 * 变更状态
	 */
	@Override
	public ResultResp updateStatus(String billid) {
		ResultResp resp = new ResultResp();
		WmsHandworkSend wmsHandworkSend = getEntity(billid);
		wmsHandworkSend.setStatus(5);// 暂定发货完成状态为5
		updateEntity(wmsHandworkSend);
		return resp;
	}

	@Override
	public WmsHandworkSend getInfo(String mapSheetNo, String whCode) {
		return (WmsHandworkSend) uniqueResult("from WmsHandworkSend where mapSheetNo = ? and whCode = ?", mapSheetNo,
				whCode);
	}
}
