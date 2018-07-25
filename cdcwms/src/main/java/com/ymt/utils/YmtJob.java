package com.ymt.utils;

import java.io.Serializable;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 用于定时任务(注意这里所有属性必需支持Serializable,否则会报错)
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年4月7日
 */
public abstract class YmtJob implements Job, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4226096330498705509L;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        run(dataMap);
    }

    /**
     * 获取 cronExpression 表达示
     */
    public abstract String getCronExpression();

    /**
     * 获取唯一key
     */
    public abstract String getKey();

    /**
     * 设置上下文map
     *
     * @return
     */
    public abstract Map<String, Serializable> getMap();

    /**
     * 执行任务
     */
    public abstract void run(JobDataMap dataMap);

}
