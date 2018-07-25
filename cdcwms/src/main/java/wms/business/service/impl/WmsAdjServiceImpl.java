package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsAdj;
import com.ymt.utils.SerialNumberUtils;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsAdjService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsAdjService")
public class WmsAdjServiceImpl extends BaseServiceImpl<WmsAdj> implements WmsAdjService {
	private SerialNumberUtils utils = new SerialNumberUtils() {

		@Override
		public boolean isExpired(Date startTime) {
			return new Date().getTime() / (1000L * 60 * 60 * 24) == startTime.getTime() / (1000L * 60 * 60 * 24);// 是否为同一天
		}
	};

	/**
	 * 分页
	 */
	@Override
	public PageData<WmsAdj> getPageData(int page, int rows, WmsAdj wmsAdj) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from WmsAdj where 1=1");
		if (!StringUtil.isEmpty(wmsAdj.getAdjCode())) {
			base_hql.append("and adjCode like ?");
			params.add("%" + wmsAdj.getAdjCode() + "%");
		}
		if (!StringUtil.isEmpty(wmsAdj.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(wmsAdj.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By adjCode desc", page, rows, params);
	}

	/**
	 * 新增,修改库存调整申请单
	 */
	@Override
	public ResultResp saveWmsAdj(WmsAdj wmsAdj) {
		ResultResp resp = new ResultResp();
		// 保存当前插入时间
		wmsAdj.setUpdateTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
		String id = wmsAdj.getId();
		String check = "select count(*) From WmsAdj Where adjCode = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(wmsAdj.getAdjCode());
		if (StringUtil.isEmpty(id)) {// 新增
			// 生成调库单编码
			String adjCode = "TK-" + StringUtil.getCurStringDate("yyyyMMdd") + utils.getSerialNumber(5);
			wmsAdj.setAdjCode(adjCode);
			params.set(0, wmsAdj.getAdjCode());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("调库单编码编号" + wmsAdj.getAdjCode() + "已经存在！");
			} else {
				wmsAdj.setStatu(BaseModel.INT_INIT);
				saveEntity(wmsAdj);
				resp.setRetcode("0");
				resp.setRetmsg("新增调库单成功！");
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(wmsAdj.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("调库单编码编号" + wmsAdj.getAdjCode() + "已经存在！");
			} else {
				updateEntity(wmsAdj);
				resp.setRetcode("0");
				resp.setRetmsg("修改调库单成功！");
			}
		}
		return resp;
	}

	/**
	 * 删除
	 */
	@Override
	public ResultResp deleteWmsAdj(String id) {
		ResultResp resp = new ResultResp();
		WmsAdj wmsAdj = getEntity(id);
		if (BaseModel.INT_INIT.equals(wmsAdj.getStatu())) {
			deleteEntity(wmsAdj);
			resp.setRetcode("0");
			resp.setRetmsg("删除成功!");
		} else {
			resp.setRetcode("-1");
			resp.setRetmsg("该单据已经执行,无法删除!");
		}
		return resp;
	}

}
