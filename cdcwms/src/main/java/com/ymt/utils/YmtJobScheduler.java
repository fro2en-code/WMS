package com.ymt.utils;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

/**
 * 参考 Spring quartz 集群实现
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年4月7日
 */
@Service("ymtJobScheduler")
public class YmtJobScheduler {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 添加定时任务
     */
    public void addJob(YmtJob job) throws SchedulerException {
        JobDataMap jobData = new JobDataMap();
        if (null != job.getMap()) {
            jobData.putAll(job.getMap());
        }
        JobDetail jobDetail = JobBuilder.newJob(job.getClass()).setJobData(jobData)
                .withIdentity(JobKey.jobKey(job.getKey())).build();
        //
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey(job.getKey()))
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())).build();
        //
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 删除任务,这里只用到了 getKey() 方法
     */
    public void removeJob(YmtJob job) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(job.getKey());
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseJob(jobKey);
        scheduler.deleteJob(jobKey);
    }

    public void updateJob(YmtJob job) throws SchedulerException {
        removeJob(job);
        addJob(job);
    }

    /**
     * 启用,禁用任务
     */
    public void enableJob(boolean enable, YmtJob job) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(job.getKey());
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if (enable) {
            scheduler.resumeJob(jobKey);
        } else {
            scheduler.pauseJob(jobKey);
        }

    }
}
