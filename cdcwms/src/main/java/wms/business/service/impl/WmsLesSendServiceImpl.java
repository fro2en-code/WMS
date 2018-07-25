package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsLesSend;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsLesSendService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsLesSendService")
public class WmsLesSendServiceImpl extends BaseServiceImpl<WmsLesSend> implements WmsLesSendService {

	@Override
	public ResultResp del(WmsLesSend wmsLesSend) {
		ResultResp resp = new ResultResp();
		deleteEntity(wmsLesSend);
		resp.setRetcode("0");
		resp.setRetmsg("删除成功！");
		return resp;
	}

	@Override
	public WmsLesSend getInfo(String mapSheetNo, String whCode) {
		return (WmsLesSend) uniqueResult("from WmsLesSend where mapSheetNo =? and whCode = ?", mapSheetNo, whCode);
	}

	/**
	 * 根据id查询les发货单
	 */
	@Override
	public WmsLesSend getLesSendList(String id) {
		List<WmsLesSend> lesSends = findEntityByHQL("From WmsLesSend Where id = ?", id);
		if (lesSends.isEmpty()) {
			return null;
		} else {
			return lesSends.get(0);
		}
	}

	/**
	 * 分页
	 */
	@Override
	public PageData<WmsLesSend> getPageDataList(int page, int rows, WmsLesSend wmsLesSend) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("From WmsLesSend where 1=1 ");
		if (!StringUtil.isEmpty(wmsLesSend.getMapSheetNo())) {
			base_hql.append(" and mapSheetNo like '%" + wmsLesSend.getMapSheetNo() + "%'");
		}
		if (wmsLesSend.getStatus() != null) {
			base_hql.append("and status = ?");
			params.add(wmsLesSend.getStatus());
		}
		if (!StringUtil.isEmpty(wmsLesSend.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(wmsLesSend.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By updateTime desc", page, rows, params);
	}

	@Override
	public ResultResp save(WmsLesSend wmsLesSend) {
		ResultResp resp = new ResultResp();
		String check = "select count(*) from WmsLesSend where mapSheetNo = ? and whCode = ?";
		List<Object> params = new ArrayList<>();
		params.add(wmsLesSend.getMapSheetNo());
		params.add(wmsLesSend.getWhCode());
		if (StringUtil.isEmpty(wmsLesSend.getId())) {// 新增
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("配送单号" + wmsLesSend.getMapSheetNo() + "已经存在！");
			} else {
				wmsLesSend.setStatus(BaseModel.INT_INIT);
				wmsLesSend.setUpdateUser("");
				resp.setBillId((String) saveEntity(wmsLesSend));
				resp.setRetcode("0");
				resp.setRetmsg("新增发货单成功！");
			}
		} else {// 更新
			check += "and id <> ?";
			params.add(wmsLesSend.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("配送单号" + wmsLesSend.getMapSheetNo() + "已经存在！");
			} else {
				updateEntity(wmsLesSend);
				resp.setRetcode("0");
				resp.setRetmsg("修改发货单成功！");
			}
		}
		return resp;
	}

	/**
	 * 变更状态
	 */
	@Override
	public ResultResp updateStatus(String billid) {
		ResultResp resp = new ResultResp();
		WmsLesSend wmsLesSend = getEntity(billid);
		wmsLesSend.setStatus(5);// 暂定发货完成状态为5
		updateEntity(wmsLesSend);
		return resp;
	}
}
