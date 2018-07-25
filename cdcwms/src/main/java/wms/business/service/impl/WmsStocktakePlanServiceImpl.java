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
import com.wms.business.WmsStocktakePlan;
import com.ymt.utils.SerialNumberUtils;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsStocktakePlanService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsStocktakePlanService")
public class WmsStocktakePlanServiceImpl extends BaseServiceImpl<WmsStocktakePlan> implements WmsStocktakePlanService {
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
	public PageData<WmsStocktakePlan> getPageData(int page, int rows, WmsStocktakePlan wmsStockTakePlan) {
		StringBuilder base_hql = new StringBuilder();
		base_hql.append("from WmsStocktakePlan where 1 = 1");
		List<Serializable> params = new ArrayList<>();
		if (!StringUtil.isEmpty(wmsStockTakePlan.getTakePlanCode())) {
			base_hql.append("and takePlanCode like ?");
			params.add("%" + wmsStockTakePlan.getTakePlanCode() + "%");
		}
		if (!StringUtil.isEmpty(wmsStockTakePlan.getWhCode())) {
			base_hql.append("and whCode = ?");
			params.add(wmsStockTakePlan.getWhCode());
		}

		return getPageDataByBaseHql(base_hql.toString(), " Order By takePlanCode desc", page, rows, params);
	}

	/**
	 * 新增,修改
	 */
	@Override
	public ResultResp saveStocktakePlan(WmsStocktakePlan wmsStocktakePlan) {
		ResultResp resp = new ResultResp();
		List<Object> params = new ArrayList<>();
		if (wmsStocktakePlan.getTakeType().equals(BaseModel.INT_INIT)) {// 库位
			String storageCode = "select count(*) From WmsStorag where storageCode = ?";
			if (count(storageCode, wmsStocktakePlan.getStorageCode()) > 0) {
				// 保存当前插入时间
				wmsStocktakePlan.setUpdateTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
				String id = wmsStocktakePlan.getId();
				String check = "select count(*) From WmsStocktakePlan Where takePlanCode= ?";
				params.add(wmsStocktakePlan.getTakePlanCode());
				if (StringUtil.isEmpty(id)) {// 新增
					// 生成盘库计划编码
					String takePlanCode = "PK-" + StringUtil.getCurStringDate("yyyyMMdd") + utils.getSerialNumber(5);
					wmsStocktakePlan.setTakePlanCode(takePlanCode);
					params.set(0, wmsStocktakePlan.getTakePlanCode());
					if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
						resp.setRetcode("-1");
						resp.setRetmsg("盘库计划编码" + wmsStocktakePlan.getTakePlanCode() + "已经存在！");
					} else {
						wmsStocktakePlan.setStatus(BaseModel.INT_INIT);
						saveEntity(wmsStocktakePlan);
						resp.setRetcode("0");
						resp.setRetmsg("新增盘库申请成功！");
					}
				} else {// 更新
					check += " and id <> ?";
					params.add(wmsStocktakePlan.getId());
					if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
						resp.setRetcode("-1");
						resp.setRetmsg("盘库计划编码" + wmsStocktakePlan.getTakePlanCode() + "已经存在！");
					} else {
						updateEntity(wmsStocktakePlan);
						resp.setRetcode("0");
						resp.setRetmsg("修改盘库申请成功！");
					}
				}
			} else {
				throw new RuntimeException("所选择库位编号" + wmsStocktakePlan.getStorageCode() + "不存在！");
			}
		} else if (wmsStocktakePlan.getTakeType().equals(BaseModel.INT_CREATE)) {// 库区
			String storageCode = "select count(*) From WmsZone where zoneCode = ?";
			if (count(storageCode, wmsStocktakePlan.getStorageCode()) > 0) {
				// 保存当前插入时间
				wmsStocktakePlan.setUpdateTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
				String id = wmsStocktakePlan.getId();
				String check = "select count(*) From WmsStocktakePlan Where takePlanCode= ?";
				params.add(wmsStocktakePlan.getTakePlanCode());
				if (StringUtil.isEmpty(id)) {// 新增
					// 生成盘库计划编码
					String takePlanCode = "PK-" + StringUtil.getCurStringDate("yyyyMMdd") + utils.getSerialNumber(5);
					wmsStocktakePlan.setTakePlanCode(takePlanCode);
					params.set(0, wmsStocktakePlan.getTakePlanCode());
					if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
						resp.setRetcode("-1");
						resp.setRetmsg("盘库计划编码" + wmsStocktakePlan.getTakePlanCode() + "已经存在！");
					} else {
						wmsStocktakePlan.setStatus(BaseModel.INT_INIT);
						saveEntity(wmsStocktakePlan);
						resp.setRetcode("0");
						resp.setRetmsg("新增盘库申请成功！");
					}
				} else {// 更新
					check += " and id <> ?";
					params.add(wmsStocktakePlan.getId());
					if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
						resp.setRetcode("-1");
						resp.setRetmsg("盘库计划编码" + wmsStocktakePlan.getTakePlanCode() + "已经存在！");
					} else {
						updateEntity(wmsStocktakePlan);
						resp.setRetcode("0");
						resp.setRetmsg("修改盘库申请成功！");
					}
				}
			} else {
				throw new RuntimeException("所选择库区编号" + wmsStocktakePlan.getStorageCode() + "不存在！");

			}
		} else {
			throw new RuntimeException("盘点类型请选择库位或者库区");
		}

		return resp;
	}

	/**
	 * 删除
	 */
	@Override
	public ResultResp deleteStocktakePlan(String id) {
		ResultResp resp = new ResultResp();
		WmsStocktakePlan wmsStocktakePlan = getEntity(id);
		if (BaseModel.INT_INIT.equals(wmsStocktakePlan.getStatus())) {
			deleteEntity(wmsStocktakePlan);
			resp.setRetcode("0");
			resp.setRetmsg("删除成功!");
		} else {
			resp.setRetcode("-1");
			resp.setRetmsg("该单据已生成了任务,无法对改单据进行操作!");
		}
		return resp;
	}

	@Override
	public List<WmsStocktakePlan> getWmsStocktakePlan(String billId) {
		return findEntityByHQL("from WmsStocktakePlan where takePlanCode = ?", billId);
	}
}
