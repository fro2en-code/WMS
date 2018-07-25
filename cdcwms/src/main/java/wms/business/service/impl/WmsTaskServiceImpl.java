package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.Function;
import com.wms.business.WmsTask;
import com.ymt.utils.UserUtils;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsLesSendService;
import wms.business.service.WmsTaskService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsTaskService")
public class WmsTaskServiceImpl extends BaseServiceImpl<WmsTask> implements WmsTaskService {
    @Resource
    private UserUtils userUtils;

    @Resource
    private WmsLesSendService wmsLesSendService;

    /**
     * 分页
     */
    @Override
    public PageData<WmsTask> getPageData(int page, int rows, WmsTask wmsTask) {
        List<Serializable> params = new ArrayList<>();
        StringBuilder baseHQL = new StringBuilder(
                " From WmsTask as task,WmsTaskBill as bill where task.id=bill.taskid  ");
        if (null != wmsTask && StringUtils.isNotEmpty(wmsTask.getExecutorName())) {
            params.add(wmsTask.getExecutorName());
            baseHQL.append(" and task.executorName =? ");
        }
        if (null != wmsTask && null != wmsTask.getStatus()) {
            params.add(wmsTask.getStatus());
            baseHQL.append(" and task.status =? ");
        } else if (null != wmsTask && null != wmsTask.getRemark()) {
            params.add(Integer.valueOf(wmsTask.getRemark()));
            baseHQL.append(" and task.status =? ");
        }
        if (null != wmsTask && StringUtils.isNotEmpty(wmsTask.getBillNumber())) {
            params.add(wmsTask.getBillNumber());
            baseHQL.append(" and bill.billNumber =? ");
        }
        if (null != wmsTask && null != wmsTask.getWhCode()) {
            params.add(wmsTask.getWhCode());
            baseHQL.append(" and task.whCode =? ");
        }
        StringBuilder hql = new StringBuilder("select task.id as id ,task.taskdesc as taskdesc,task.whCode as whCode,"
                + "task.level as level,task.type as type,task.beginTime as beginTime,"
                + "task.acceptTime as acceptTime,"
                + "task.endTime as endTime,task.status as status,task.executorName as executorName, "
                + "bill.oraName as oraName,bill.storageCode as storageCode, bill.nextStorageCode as nextStorageCode,bill.billNumber as billNumber");
        StringBuilder countHQL = new StringBuilder("select count(*) ");
        hql.append(baseHQL);
        hql.append(" order by task.beginTime desc");
        countHQL.append(baseHQL);
        return getPageDataByHql(hql.toString(), countHQL.toString(), new Function<Query>() {
            @Override
            public void run(Query t) {
                t.setResultTransformer(Transformers.aliasToBean(WmsTask.class));
            }
        }, page, rows, params.toArray(new Serializable[params.size()]));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<WmsTask> getWmsTaskData(int page, int rows, String user, String whCode) {
        List<String> role = userUtils.getRolesByUser(user);
        String sql = "SELECT DISTINCT id, beginTime, taskdesc, status, level FROM ( SELECT wms_task.id AS id, wms_task.begin_time AS beginTime, wms_task.Taskdesc AS taskdesc, 1 AS status, wms_task.Level AS level FROM wms_task WHERE wms_task.wh_code = :whCode AND wms_task.Status = 1 AND wms_task.executorName = :user UNION ALL SELECT wms_task.id AS id, wms_task.begin_time AS beginTime, wms_task.Taskdesc AS taskdesc, 0 AS status, wms_task.Level AS level FROM wms_task INNER JOIN wms_task_bill ON wms_task_bill.Taskid = wms_task.id INNER JOIN wms_dock ON wms_dock.zone_Code = wms_task_bill.next_storage_code AND wms_dock.wh_code = :whCode INNER JOIN user_sessionid ON user_sessionid.dockcode = wms_dock.dock_code AND user_sessionid.user_loginName = :user INNER JOIN task_flow ON task_flow.nowTaskCode = wms_task.Type AND task_flow.wh_code = :whCode AND (:user = task_flow.nowPerson OR task_flow.nowRole IN (:role)) WHERE wms_task.wh_code = :whCode AND wms_task.Status = 0 AND wms_task.Type IN (1, 2) UNION ALL SELECT wms_task.id AS id, wms_task.begin_time AS beginTime, wms_task.Taskdesc AS taskdesc, 0 AS status, wms_task.Level AS level FROM wms_task INNER JOIN task_flow ON task_flow.nowTaskCode = wms_task.Type AND task_flow.wh_code = :whCode AND (:user = task_flow.nowPerson OR task_flow.nowRole IN (:role)) WHERE wms_task.wh_code = :whCode AND wms_task.Status = 0 AND wms_task.Type IN (3, 10) UNION ALL SELECT wms_task.id AS id, wms_task.begin_time AS beginTime, wms_task.Taskdesc AS taskdesc, 0 AS status, wms_task.Level AS level FROM wms_task INNER JOIN task_flow ON task_flow.nowTaskCode = wms_task.Type AND task_flow.wh_code = :whCode AND (:user = task_flow.nowPerson OR task_flow.nowRole IN (:role)) INNER JOIN wms_task_bill ON wms_task_bill.Taskid = wms_task.id INNER JOIN wms_storage ON wms_storage.STORAGE_CODE = wms_task_bill.Next_storage_code AND wms_storage.wh_code = :whCode INNER JOIN wms_zone_manager ON (wms_zone_manager.Groupid = wms_storage.group_code OR wms_storage.group_code IS NULL AND wms_zone_manager.Zone_code = wms_storage.ZONE_CODE) AND wms_zone_manager.User_loginname = :user AND wms_zone_manager.wh_code = :whCode WHERE wms_task.wh_code = :whCode AND wms_task.Status = 0 AND wms_task.Type IN (4, 5, 9) UNION ALL SELECT wms_task.id AS id, wms_task.begin_time AS beginTime, wms_task.Taskdesc AS taskdesc, 0 AS status, wms_task.Level AS level FROM wms_task INNER JOIN task_flow ON task_flow.nowTaskCode = wms_task.Type AND task_flow.wh_code = :whCode AND (:user = task_flow.nowPerson OR task_flow.nowRole IN (:role)) INNER JOIN wms_task_bill ON wms_task_bill.Taskid = wms_task.id INNER JOIN wms_storage ON wms_storage.STORAGE_CODE = wms_task_bill.storage_code AND wms_storage.wh_code = :whCode INNER JOIN wms_zone_manager ON (wms_zone_manager.Groupid = wms_storage.group_code OR wms_storage.group_code IS NULL AND wms_zone_manager.Zone_code = wms_storage.ZONE_CODE) AND wms_zone_manager.User_loginname = :user AND wms_zone_manager.wh_code = :whCode WHERE wms_task.wh_code = :whCode AND wms_task.Status = 0 AND wms_task.Type IN (6, 7, 8) ) mytask ORDER BY level DESC, beginTime LIMIT :startRow,:row";
        //
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("whCode", whCode);
        query.setParameter("user", user);
        query.setParameter("startRow", (page - 1) * rows);
        query.setParameter("row", rows);
        query.setParameterList("role", role);
        // id, beginTime, taskdesc, status, level
        query.addScalar("id", StandardBasicTypes.STRING);
        query.addScalar("beginTime", StandardBasicTypes.STRING);
        query.addScalar("taskdesc", StandardBasicTypes.STRING);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("level", StandardBasicTypes.INTEGER);
        query.setResultTransformer(Transformers.aliasToBean(WmsTask.class));
        return query.list();
    }

    @Override
    public List<WmsTask> getwmsTaskList(String billId) {
        return findEntityByHQL("from WmsTask where billId = ?", billId);
    }

    @Override
    public boolean isAllTaskCompleteByBillId(String billId) {
        String hql = "select count(*) from WmsTask where billid=? and (status=0 or status=1)";
        Integer result = count(hql, billId);
        return 0 == result;
    }

}
