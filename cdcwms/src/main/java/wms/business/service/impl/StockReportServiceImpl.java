package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.Function;
import com.wms.business.StockReport;

import its.base.service.BaseServiceImpl;
import jodd.util.StringUtil;
import wms.business.service.StockReportService;

@Service("stockReportService")
public class StockReportServiceImpl extends BaseServiceImpl<StockReport> implements StockReportService {

	@Override
	public List<StockReport> getStockReportData(Date startTime, Date endTime) {
		String sql = "SELECT t.wh_Code as whCode, t.G_code as goodsCode, t.G_name as goodsName, "
				+ "t.G_TYPE as goodsType, t.ora_code as oraCode, t.ora_name as oraName, t1.inner_coding as goodsAlias, "
				+ "(SELECT c1.total_num FROM wms_account c1 WHERE c1.deal_time > ? "
				+ "AND c1.Wh_code = t.WH_CODE AND c1.G_CODE = t.g_code "
				+ "AND c1.G_TYPE = t.G_TYPE AND c1.ora_code = t.ora_code ORDER BY c1.deal_time LIMIT 1) AS 'beforeStock', "
				+ "(SELECT c.total_num FROM wms_account c WHERE c.deal_time < ? AND c.Wh_code = t.WH_CODE "
				+ "AND c.G_CODE = t.g_code AND c.G_TYPE = t.G_TYPE AND c.ora_code = t.ora_code "
				+ "ORDER BY c.deal_time DESC LIMIT 1) AS 'nowStock', (SELECT SUM(c3.deal_num) "
				+ "FROM wms_account c3 WHERE c3.deal_time > ? AND c3.deal_time < ? "
				+ "AND c3.Wh_code = t.WH_CODE AND c3.G_CODE = t.g_code AND c3.G_TYPE = t.G_TYPE "
				+ "AND c3.ora_code = t.ora_code AND c3.deal_type = 1) AS 'inStock', "
				+ "(SELECT SUM(c4.deal_num) FROM wms_account c4 WHERE c4.deal_time > ? AND c4.deal_time < ? "
				+ "AND c4.Wh_code = t.WH_CODE AND c4.G_CODE = t.g_code AND c4.G_TYPE = t.G_TYPE "
				+ "AND c4.ora_code = t.ora_code AND c4.deal_type = 0) AS 'outStock' "
				+ "FROM wms_total_account t INNER JOIN wms_goods t1 ON t1.Wh_code = t.WH_CODE "
				+ "AND t1.G_CODE = t.g_code AND t1.G_TYPE = t.G_TYPE AND t1.ora_code = t.ora_code";
		return findEntityBySQL(sql, new Function<SQLQuery>() {

			@Override
			public void run(SQLQuery t) {
				t.addScalar("whCode", StandardBasicTypes.STRING);
				t.addScalar("goodsCode", StandardBasicTypes.STRING);
				t.addScalar("goodsName", StandardBasicTypes.STRING);
				t.addScalar("goodsType", StandardBasicTypes.STRING);
				t.addScalar("oraCode", StandardBasicTypes.STRING);
				t.addScalar("oraName", StandardBasicTypes.STRING);
				t.addScalar("goodsAlias", StandardBasicTypes.STRING);
				t.addScalar("beforeStock", StandardBasicTypes.INTEGER);
				t.addScalar("nowStock", StandardBasicTypes.INTEGER);
				t.addScalar("inStock", StandardBasicTypes.INTEGER);
				t.addScalar("outStock", StandardBasicTypes.INTEGER);
				t.setResultTransformer(Transformers.aliasToBean(StockReport.class));
			}
		}, startTime, endTime, startTime, endTime, startTime, endTime);
	}

	@Override
	public PageData<StockReport> queryStockMonthlyStatement(int page, int rows, StockReport stockReport) {
		List<Serializable> params = new ArrayList<>();
		StringBuffer listSql = new StringBuffer(
				"SELECT t.reportDate,t.oraCode,t.oraName,t.goodsCode,t.goodsAlias,t.goodsName,t.goodsType,t.beforeStock,t.inStock,t.outStock,t.nowStock FROM stockReport t where 1= 1");
		if (!StringUtil.isEmpty(stockReport.getWhCode())) {
			listSql.append(" AND t.whCode = ?");
			params.add(stockReport.getWhCode());
		}
		if (!StringUtil.isEmpty(stockReport.getOraCode())) {
			listSql.append(" AND t.oraCode like ?");
			params.add("%" + stockReport.getOraCode() + "%");
		}
		if (!StringUtil.isEmpty(stockReport.getGoodsCode())) {
			listSql.append(" AND t.goodsCode like ?");
			params.add("%" + stockReport.getGoodsCode() + "%");
		}
		if (!StringUtil.isEmpty(stockReport.getGoodsAlias())) {
			listSql.append(" AND t.goodsAlias like ?");
			params.add("%" + stockReport.getGoodsAlias() + "%");
		}
		if (!StringUtil.isEmpty(stockReport.getGoodsType())) {
			listSql.append(" AND t.goodsType like ?");
			params.add("%" + stockReport.getGoodsType() + "%");
		}

		StringBuffer countSql = new StringBuffer("Select  count(*) From (" + listSql + ") allInfo");
		return getPageDataBySql(listSql.toString(), countSql.toString(), new Function<SQLQuery>() {

			@Override
			public void run(SQLQuery t) {
				t.addScalar("reportDate", StandardBasicTypes.STRING);
				t.addScalar("goodsCode", StandardBasicTypes.STRING);
				t.addScalar("goodsName", StandardBasicTypes.STRING);
				t.addScalar("goodsType", StandardBasicTypes.STRING);
				t.addScalar("oraCode", StandardBasicTypes.STRING);
				t.addScalar("oraName", StandardBasicTypes.STRING);
				t.addScalar("goodsAlias", StandardBasicTypes.STRING);
				t.addScalar("beforeStock", StandardBasicTypes.INTEGER);
				t.addScalar("nowStock", StandardBasicTypes.INTEGER);
				t.addScalar("inStock", StandardBasicTypes.INTEGER);
				t.addScalar("outStock", StandardBasicTypes.INTEGER);
				t.setResultTransformer(Transformers.aliasToBean(StockReport.class));
			}
		}, page, rows, params.toArray(new Serializable[params.size()]));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageData<Map<String, Object>> queryOutStroage(int page, int rows, String listsql, String countsql,
			List<Serializable> params) {
		return (PageData<Map<String, Object>>) getPageListBySql(listsql, countsql, new Function<SQLQuery>() {

			@Override
			public void run(SQLQuery t) {
				t.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
		}, page, rows, params.toArray(new Serializable[params.size()]));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageData<Map<String, Object>> queryIntoStorage(int page, int rows, String listsql, String countsql,
			List<Serializable> params) {
		return (PageData<Map<String, Object>>) getPageListBySql(listsql, countsql, new Function<SQLQuery>() {

			@Override
			public void run(SQLQuery t) {
				t.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
		}, page, rows, params.toArray(new Serializable[params.size()]));
	}

}
