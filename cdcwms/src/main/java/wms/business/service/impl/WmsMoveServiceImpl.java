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
import com.wms.business.WmsMove;
import com.ymt.utils.SerialNumberUtils;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsMoveService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsMoveService")
public class WmsMoveServiceImpl extends BaseServiceImpl<WmsMove> implements WmsMoveService {
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
	public PageData<WmsMove> getPageData(int page, int rows, WmsMove wmsMove) {
		StringBuilder base_hql = new StringBuilder();
		List<Serializable> params = new ArrayList<>();
		base_hql.append("from WmsMove where 1=1");
		if (!StringUtil.isEmpty(wmsMove.getMoveCode())) {
			base_hql.append("and moveCode like ?");
			params.add("%" + wmsMove.getMoveCode() + "%");
		}
		if (!StringUtil.isEmpty(wmsMove.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(wmsMove.getWhCode());
		}
		return getPageDataByBaseHql(base_hql.toString(), " Order By moveCode desc", page, rows, params);

	}

	/**
	 * 新增,修改
	 */
	@Override
	public ResultResp saveMove(WmsMove wmsMove) {
		ResultResp resp = new ResultResp();
		// 保存当前插入时间
		wmsMove.setUpdateTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
		String id = wmsMove.getId();
		String check = "select count(*) From WmsMove Where moveCode= ?";
		List<Object> params = new ArrayList<>();
		params.add(wmsMove.getMoveCode());
		if (StringUtil.isEmpty(id)) {// 新增
			// 生成移库单编码
			String moveCode = "YK-" + StringUtil.getCurStringDate("yyyyMMdd") + utils.getSerialNumber(5);
			wmsMove.setMoveCode(moveCode);
			params.set(0, wmsMove.getMoveCode());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("移库单编码编号" + wmsMove.getMoveCode() + "已经存在！");
			} else {
				wmsMove.setStatu(BaseModel.INT_INIT);
				saveEntity(wmsMove);
				resp.setRetcode("0");
				resp.setRetmsg("新增移库单成功！");
			}
		} else {// 更新
			check += " and id <> ?";
			params.add(wmsMove.getId());
			if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
				resp.setRetcode("-1");
				resp.setRetmsg("移库单编码编号" + wmsMove.getMoveCode() + "已经存在！");
			} else {
				updateEntity(wmsMove);
				resp.setRetcode("0");
				resp.setRetmsg("修改移库单成功！");
			}
		}
		return resp;
	}

	/**
	 * 删除
	 */
	@Override
	public ResultResp deleteMove(String id) {
		ResultResp resp = new ResultResp();
		WmsMove wmsMove = getEntity(id);
		if (BaseModel.INT_INIT.equals(wmsMove.getStatu())) {
			deleteEntity(wmsMove);
			resp.setRetcode("0");
			resp.setRetmsg("删除成功!");
		} else {
			resp.setRetcode("-1");
			resp.setRetmsg("该单据已生成过任务,无法进行删除!");
		}
		return resp;

	}

	@Override
	public WmsMove getMoveInfoByMoveCode(String moveCode) {
		return (WmsMove) uniqueResult("from WmsMove where moveCode = ?", moveCode);
	}
}
