package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsLesReceive;
import com.wms.business.WmsLesReceiveList;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsLesReceiveListService;
import wms.business.service.WmsLesReceiveService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsLesReceiveListService")
public class WmsLesReceiveListServiceImpl extends BaseServiceImpl<WmsLesReceiveList>
		implements WmsLesReceiveListService {

	@Resource
	private WmsLesReceiveService wmsLesReceiveService;

	@Override
	public ResultResp del(WmsLesReceiveList wmsLesReceiveList) {
		ResultResp resp = new ResultResp();
		WmsLesReceive wmsLesReceive = wmsLesReceiveService.getLesReceiveByMapSheetNo(wmsLesReceiveList.getMapSheetNo(),
				wmsLesReceiveList.getWhCode());
		if (BaseModel.INT_INIT.equals(wmsLesReceive.getStatus())) {
			deleteEntity(wmsLesReceiveList);
			resp.setRetcode("0");
			resp.setRetmsg("删除成功！");
		} else {
			resp.setRetcode("-1");
			resp.setRetmsg("该单据已经生成任务,无法进行操作!");
		}
		return resp;
	}

	@Override
	public void delByHandworkReceiveList(String mapSheetNo, String whCode) {
		batchHandle("delete from WmsLesReceiveList where mapSheetNo = ? and whCode =?", mapSheetNo, whCode);
	}

	@Override
	public List<WmsLesReceiveList> getLesReceiveList(String mapSheetNo, String whCode) {
		return findEntityByHQL("from WmsLesReceiveList where mapSheetNo = ? and whCode =?", mapSheetNo, whCode);
	}

	@Override
	public WmsLesReceiveList getLesReceiveList(String mapSheetNo, String sxCardNo, String whCode) {
		return (WmsLesReceiveList) uniqueResult(
				"from WmsLesReceiveList where mapSheetNo = ? and sxCardNo =? and whCode = ?", mapSheetNo, sxCardNo,
				whCode);
	}

	/**
	 * 分页
	 */
	@Override
	public PageData<WmsLesReceiveList> getPageData(int page, int rows, WmsLesReceiveList wmsLesReceiveList) {
		return getPageDataByBaseHql("From WmsLesReceiveList where 1=1 ", null, page, rows,
				new ArrayList<Serializable>());
	}

	@Override
	public PageData<WmsLesReceiveList> getPageDataLesReceiveList(int page, int rows,
			WmsLesReceiveList wmsLesReceiveList) {
		StringBuilder base_hql = new StringBuilder();
		base_hql.append("from WmsLesReceiveList where 1 = 1");
		List<Serializable> params = new ArrayList<>();
		if (!StringUtil.isEmpty(wmsLesReceiveList.getMapSheetNo())) {
			base_hql.append("and mapSheetNo = ?");
			params.add(wmsLesReceiveList.getMapSheetNo());
		}
		if (!StringUtil.isEmpty(wmsLesReceiveList.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(wmsLesReceiveList.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " order by sxCardNo", page, rows, params);
	}

	@Override
	public ResultResp save(WmsLesReceiveList wmsLesReceiveList) {
		ResultResp resp = new ResultResp();
		// 获取Les收货单信息
		WmsLesReceive wmsLesReceive = wmsLesReceiveService.getLesReceiveByMapSheetNo(wmsLesReceiveList.getMapSheetNo(),
				wmsLesReceiveList.getWhCode());
		if (BaseModel.INT_INIT.equals(wmsLesReceive.getStatus())) {
			if (wmsLesReceiveList.getMapSheetNo().equals(wmsLesReceiveList.getSxCardNo())) {
				resp.setRetcode("-1");
				resp.setRetmsg("随箱卡号'" + wmsLesReceiveList.getSxCardNo() + "'与配送单号一样");
				return resp;
			} else {
				String check = "select count(*) from WmsLesReceiveList where sxCardNo = ? and mapSheetNo = ? and whCode = ?";
				List<Object> params = new ArrayList<>();
				params.add(wmsLesReceiveList.getSxCardNo());
				params.add(wmsLesReceiveList.getMapSheetNo());
				params.add(wmsLesReceiveList.getWhCode());
				if (StringUtil.isEmpty(wmsLesReceiveList.getId())) {
					if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
						resp.setRetcode("-1");
						resp.setRetmsg("随箱卡号" + wmsLesReceiveList.getSxCardNo() + "已经存在！");
					} else {
						resp.setBillId((String) saveEntity(wmsLesReceiveList));
						resp.setRetcode("0");
						resp.setRetmsg("新增收货明细单成功！");
					}
				} else {
					check += "and id <> ?";
					params.add(wmsLesReceiveList.getId());
					if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
						resp.setRetcode("-1");
						resp.setRetmsg("随箱卡号" + wmsLesReceiveList.getSxCardNo() + "已经存在！");
					} else {
						updateEntity(wmsLesReceiveList);
						resp.setRetcode("0");
						resp.setRetmsg("新增收货明细单成功！");
					}
				}

			}
		} else {
			resp.setRetcode("-1");
			resp.setRetmsg("该单据已经生成任务,无法进行操作!");
		}
		return resp;
	}

	@Override
	public void saveNewLesReceiveList(WmsLesReceiveList wmsLesReceiveList) {
		updateEntity(wmsLesReceiveList);
	}
}
