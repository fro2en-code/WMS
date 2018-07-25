package wms.web.business.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobDataMap;

import com.wms.business.WmsTask;
import com.ymt.utils.SpringContex;
import com.ymt.utils.YmtJob;

import wms.business.biz.TaskBiz;
import wms.business.biz.TaskPrePickBiz;

public class PrePickJob extends YmtJob {
    /**
     *
     */
    private static final long serialVersionUID = -1178796219257251636L;
    private String cronExpression;

    private String id;

    public PrePickJob() {
        super();
    }

    public PrePickJob(String id, String cronExpression) {
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
        return "com.ymt.utils.PrePickJob" + id;
    }

    @Override
    public Map<String, Serializable> getMap() {
        Map<String, Serializable> map = new HashMap<>();
        map.put("id", id);
        return map;
    }

    @Override
    public void run(JobDataMap dataMap) {
        TaskBiz taskBiz = SpringContex.getBean("taskBiz");
        TaskPrePickBiz taskPrePickBiz = SpringContex.getBean("taskPrePickBiz");
        id = dataMap.getString("id");
        WmsTask task = taskPrePickBiz.createDefaultTaskByBillID(id);
        taskBiz.addTask(task);
    }

}
