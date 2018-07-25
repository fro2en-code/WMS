package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.TaskFlow;

import its.base.service.BaseServiceImpl;
import wms.business.service.TaskFlowService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("taskFlowService")
public class TaskFlowServiceImpl extends BaseServiceImpl<TaskFlow> implements TaskFlowService {

    @Override
    public PageData<TaskFlow> getPageDataList(int page, int rows, TaskFlow taskFlow) {
        StringBuilder base_hql = new StringBuilder();
        List<Serializable> params = new ArrayList<>();
        base_hql.append("From TaskFlow where 1=1 ");
        if (!StringUtil.isEmpty(taskFlow.getWhCode())) {
            base_hql.append(" and whCode =? ");
            params.add(taskFlow.getWhCode());
        }
        if (null != taskFlow.getNowTaskCode()) {
            base_hql.append(" and nowTaskCode =? ");
            params.add(taskFlow.getNowTaskCode());
        }
        return getPageDataByBaseHql(base_hql.toString(), " Order By nowTaskCode desc", page, rows, params);
    }

    @Override
    public TaskFlow getTaskFlowByTaskCode(int taskCode, String whCode) {
        return (TaskFlow) uniqueResult("From TaskFlow where nowTaskCode=? and whCode=?", taskCode, whCode);
    }

    @Override
    public ResultResp save(TaskFlow taskFlow) {
        ResultResp resp = new ResultResp();
        String id = (String) saveEntity(taskFlow);
        resp.setRetcode("0");
        resp.setBillId(id);
        resp.setRetmsg("新增任务流程成功！");
        return resp;
    }
}
