package wms.web.business.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobDataMap;

import com.wms.business.TaskFlow;
import com.wms.business.WmsTask;
import com.ymt.utils.SpringContex;
import com.ymt.utils.YmtJob;

import wms.business.biz.TaskBiz;
import wms.business.biz.TaskFlowBiz;

public class TaskJob extends YmtJob {
    private static final long serialVersionUID = -2985742791189696944L;
    private String cronExpression;
    private String id;

    public TaskJob() {
        super();
    }

    public TaskJob(String id, String cronExpression) {
        super();
        this.id = id;
        this.cronExpression = cronExpression;
    }

    @Override
    public String getCronExpression() {
        return cronExpression;
    }

    @Override
    public String getKey() {
        return "wms.web.business.action.TaskJob" + id;
    }

    @Override
    public Map<String, Serializable> getMap() {
        Map<String, Serializable> map = new HashMap<>();
        map.put("id", id);
        return map;
    }

    @Override
    public void run(JobDataMap dataMap) {
        TaskFlowBiz taskFlowBiz = SpringContex.getBean("taskFlowBiz");
        TaskBiz taskBiz = SpringContex.getBean("taskBiz");
        id = dataMap.getString("id");
        TaskFlow taskFlow = taskFlowBiz.getTaskFlow(id);
        WmsTask task = new WmsTask();
        task.setType(taskFlow.getNowTaskCode());
        task.setLevel(0);
        task.setBillid(id);
        task.setWhCode(taskFlow.getWhCode());
        taskBiz.addTask(task);
    }

}
