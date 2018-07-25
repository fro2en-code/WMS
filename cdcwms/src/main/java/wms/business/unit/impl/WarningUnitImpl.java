package wms.business.unit.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;

import wms.business.service.WarningReturnOutTimeService;
import wms.business.service.WarningSaveOutTimeService;
import wms.business.service.WarningSendTaskOutTimeService;
import wms.business.service.WarningStockService;
import wms.business.unit.WarningUnit;

@Service("warningUnit")
public class WarningUnitImpl implements WarningUnit {
	@Resource
	private WarningStockService warningStockService;
	@Resource
	private WarningSendTaskOutTimeService warningSendTaskOutTimeService;
	@Resource
	WarningReturnOutTimeService warningReturnOutTimeService;
	@Resource
	WarningSaveOutTimeService warningSaveOutTimeService;

	@Override
	public PageData<Map<String, Object>> queryWarningStock(int page, int rows,
			String G_code, String G_NAME, String G_TYPE, String ora_code,
			String ora_name, String warning_max_num, String warning_min_num,
			String whCode) {
		List<Serializable> params = new ArrayList<>();
		StringBuffer listSql = new StringBuffer(
				"SELECT t.G_code gcode,t.G_NAME gname,t.G_TYPE gtype,t.ora_code oraCode,t.ora_name oraName,t.warning_max_num warningMaxNum,t.warning_min_num warningMinNum, SUM(IFNULL(t1.Quantity, 0) + IFNULL(t1.Pre_pick_num, 0) + IFNULL(t1.Lock_num, 0)) AS totalCount FROM wms_goods t INNER JOIN wms_stock t1 ON t1.Wh_code = t.Wh_code AND t1.G_code = t.G_CODE AND t1.G_TYPE = t.G_TYPE AND t1.sup_code = t.ora_code WHERE t.Wh_code = ? ");
		params.add(whCode);
		if (!StringUtil.isEmpty(G_code)) {
			listSql.append(" AND t.G_code like ?");
			params.add("%" + G_code + "%");
		}
		if (!StringUtil.isEmpty(G_NAME)) {
			listSql.append(" AND t.G_NAME like ?");
			params.add("%" + G_NAME + "%");
		}
		if (!StringUtil.isEmpty(ora_code)) {
			listSql.append(" AND t.ora_code like ?");
			params.add("%" + ora_code + "%");
		}

		listSql.append("GROUP BY t.G_code , t.G_NAME , t.G_TYPE , t.ora_code , t.ora_name , t.warning_max_num , t.warning_min_num HAVING SUM(IFNULL(t1.Quantity, 0) + IFNULL(t1.Pre_pick_num, 0) + IFNULL(t1.Lock_num, 0)) > t.warning_max_num OR SUM(IFNULL(t1.Quantity, 0) + IFNULL(t1.Pre_pick_num, 0) + IFNULL(t1.Lock_num, 0)) < t.warning_min_num");
		StringBuffer countSql = new StringBuffer("Select  count(*) From ("
				+ listSql + ") allInfo");
		return warningStockService.queryWarningStock(page, rows,
				listSql.toString() + " ORDER BY t.G_code", countSql.toString(),
				params);
	}

	@Override
	public PageData<Map<String, Object>> queryWarningSendTaskOutTime(int page,
			int rows, String Taskdesc, Integer Status, String executorName,
			String Billid, Integer Type, String whCode) {
		List<Serializable> params = new ArrayList<>();
		StringBuffer listSql = new StringBuffer(
				"SELECT t.id, t.Taskdesc , t.Status , t.executorName , t.Billid , t.Type  FROM wms_task t WHERE t.Type IN (6 , 7) AND t.wh_code = ? ");
		params.add(whCode);
		if (!StringUtil.isEmpty(Taskdesc)) {
			listSql.append(" AND t.Taskdesc like ?");
			params.add("%" + Taskdesc + "%");
		}

		listSql.append("AND ((t.Status =  0 AND t.begin_time < DATE_ADD(NOW(), INTERVAL - (SELECT t1.flag1 FROM system_config t1 WHERE t1.systemKey = 'sendTaskoutTime' AND t1.whCode = ?) MINUTE)) OR (t.Status = 1 AND t.accept_time < DATE_ADD(NOW(), INTERVAL - (SELECT t2.flag2 FROM system_config t2 WHERE t2.systemKey = 'sendTaskoutTime' AND t2.whCode = ?) MINUTE))) ");
		params.add(whCode);
		params.add(whCode);
		StringBuffer countSql = new StringBuffer("Select  count(*) From ("
				+ listSql + ") allInfo");
		return warningSendTaskOutTimeService.queryWarningSendTaskOutTime(page,
				rows, listSql.toString() + " order by begin_time",
				countSql.toString(), params);
	}

	@Override
	public PageData<Map<String, Object>> queryWarningReturnOutTime(int page,
			int rows, String MAP_SHEET_NO, Long IS_EMERGE,
			String DELIVERY_REC_TYPE, String mriCreateTime, String whCode) {
		List<Serializable> params = new ArrayList<>();
		StringBuffer listSql = new StringBuffer(
				"SELECT t.MAP_SHEET_NO mapSheetNo,t.IS_EMERGE isEmerge,t.DELIVERY_REC_TYPE deliveryRecType,t.mriCreateTime mriCreateTime FROM  wms_handwork_send t WHERE t.wh_code = ? AND t.status = '2' AND t.End_time < DATE_ADD(NOW(), INTERVAL - (SELECT t1.flag1 FROM system_config t1 WHERE t1.systemKey = 'returnOutTime' AND t1.whCode = ?) HOUR) ");
		params.add(whCode);
		params.add(whCode);
		if (!StringUtil.isEmpty(MAP_SHEET_NO)) {
			listSql.append(" AND t.MAP_SHEET_NO like ?");
			params.add("%" + MAP_SHEET_NO + "%");
		}
		listSql.append("UNION ALL SELECT t2.MAP_SHEET_NO,t2.IS_EMERGE,t2.DELIVERY_REC_TYPE,t2.mriCreateTime FROM wms_les_send t2 WHERE t2.wh_code = ? AND t2.status = '2' AND t2.End_time < DATE_ADD(NOW(), INTERVAL - (SELECT t1.flag1 FROM system_config t1 WHERE t1.systemKey = 'returnOutTime' AND t1.whCode = ?) HOUR)");
		params.add(whCode);
		params.add(whCode);
		if (!StringUtil.isEmpty(MAP_SHEET_NO)) {
			listSql.append(" AND t2.MAP_SHEET_NO like ?");
			params.add("%" + MAP_SHEET_NO + "%");
		}
		StringBuffer countSql = new StringBuffer("Select  count(*) From ("
				+ listSql + ") allInfo");
		return warningReturnOutTimeService.queryWarningReturnOutTime(page,
				rows, listSql.toString() + " ORDER BY mriCreateTime",
				countSql.toString(), params);
	}

	@Override
	public PageData<Map<String, Object>> queryWarningSaveOutTime(int page,
			int rows, String sup_code, String Sup_name, String G_code,
			String g_name, String G_TYPE, String whCode) {
		List<Serializable> params = new ArrayList<>();
		StringBuffer listSql = new StringBuffer(
				"SELECT t.sup_code supCode,t.Sup_name supName,t.G_code gcode,t.g_name gname,t.G_TYPE gtype, SUM(IFNULL(t.Quantity, 0) + IFNULL(t.Pre_pick_num, 0) + IFNULL(t.Lock_num, 0)) AS totalCount, t.insertTime FROM wms_stock t WHERE t.Wh_code = ? AND t.insertTime < DATE_ADD(NOW(), INTERVAL - (SELECT t1.flag1 FROM system_config t1 WHERE t1.systemKey = 'saveOutTime' AND t1.whCode = ?) DAY)");
		params.add(whCode);
		params.add(whCode);
		if (!StringUtil.isEmpty(sup_code)) {
			listSql.append(" AND t.sup_code like ?");
			params.add("%" + sup_code + "%");
		}
		if (!StringUtil.isEmpty(G_code)) {
			listSql.append(" AND t.G_code like ?");
			params.add("%" + G_code + "%");
		}
		if (!StringUtil.isEmpty(g_name)) {
			listSql.append(" AND t.g_name like ?");
			params.add("%" + g_name + "%");
		}
		listSql.append("GROUP BY t.sup_code , t.Sup_name , t.G_code , t.g_name , t.G_TYPE");
		StringBuffer countSql = new StringBuffer("Select  count(*) From ("
				+ listSql + ") allInfo");
		return warningSaveOutTimeService.queryWarningSaveOutTime(page, rows,
				listSql.toString(), countSql.toString(), params);
	}
}
