package com.sl.quartz.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import com.sl.quartz.dao.JobTaskDao;
import com.sl.quartz.entity.ScheduleJob;
import com.sl.quartz.enums.IsconcurrentEnum;
import com.sl.quartz.enums.JobstatusEnum;
import com.sl.quartz.service.JobTaskService;
import com.sl.quartz.util.QuartzJobFactory;
import com.sl.quartz.util.QuartzJobFactoryDisallowConcurrentExecution;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class JobTaskServiceImpl implements JobTaskService {

    @Autowired
    private JobTaskDao jobTaskDao;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    public void init() throws Exception {
        List<ScheduleJob> jobList = jobTaskDao.findAllTask();
        for (ScheduleJob scheduleJob : jobList) {
            addJob(scheduleJob);
        }
    }

    @Override
    public List<ScheduleJob> getAllTask() {
        return jobTaskDao.findAllTask();
    }

    @Override
    public void changeStatus(String jobId, String cmd) throws SchedulerException {
        ScheduleJob job = getTaskById(jobId);
        if (job == null) {
            return;
        }
        if ("stop".equals(cmd)) {
            deleteJob(job);
            job.setJobstatus(JobstatusEnum.STATUS_NOT_RUNNING.getStatus());
        } else if ("start".equals(cmd)) {
            job.setJobstatus(JobstatusEnum.STATUS_RUNNING.getStatus());
            addJob(job);
        }
        jobTaskDao.updateJobStatus(job);
    }

    public void addJob(ScheduleJob job) throws SchedulerException {
        if (job == null || !JobstatusEnum.STATUS_RUNNING.getStatus().equals(job.getJobstatus())) {
            return;
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobname(), job.getJobgroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (null == trigger) {
            Class<? extends Job> clazz = null;
            if (IsconcurrentEnum.CONCURRENT_IS.getStatus().equals(job.getIsconcurrent())) {
                clazz = QuartzJobFactory.class;
            } else {
                clazz = QuartzJobFactoryDisallowConcurrentExecution.class;
            }
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobname(), job.getJobgroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronexpression());
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobname(), job.getJobgroup()).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronexpression());
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobname(), scheduleJob.getJobgroup());
        scheduler.deleteJob(jobKey);
    }

    public ScheduleJob getTaskById(String jobId) {
        return jobTaskDao.findTaskById(jobId);
    }

    @Override
    public void updateCron(String jobId, String cron) throws SchedulerException {
        ScheduleJob job = getTaskById(jobId);
        if (job == null) {
            return;
        }
        job.setCronexpression(cron);
        if (JobstatusEnum.STATUS_RUNNING.getStatus().equals(job.getJobstatus())) {
            updateJobCron(job);
        }
        jobTaskDao.updateJobCron(job);
    }

    private void updateJobCron(ScheduleJob scheduleJob) throws SchedulerException {

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobname(), scheduleJob.getJobgroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronexpression());
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    @Override
    public void addTask(ScheduleJob scheduleJob) {
        scheduleJob.setCreatetime(new Date());
        jobTaskDao.saveScheduleJob(scheduleJob);
    }

    private void pauseJob(ScheduleJob scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobname(), scheduleJob.getJobgroup());
        scheduler.pauseJob(jobKey);
    }

    private void resumeJob(ScheduleJob scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobname(), scheduleJob.getJobgroup());
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void runAJobNow(ScheduleJob scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobname(), scheduleJob.getJobgroup());
        scheduler.triggerJob(jobKey);
    }

    @Override
    public void updateJobpause(ScheduleJob scheduleJob) {
        if (scheduleJob == null ||
            scheduleJob.getJobpause() == null ||
            StringUtils.isEmpty(scheduleJob.getJobpause())) {
            return;
        }
        this.jobTaskDao.updateJobpause(scheduleJob);
    }

    @Override
    public void changePauseJob(String jobId, String cmd) throws Exception {
        ScheduleJob scheduleJob = getTaskById(jobId);
        if ("pause".equals(cmd)) {
            pauseJob(scheduleJob);
            scheduleJob.setJobpause("0");
        } else if ("reusme".equals(cmd)) {
            resumeJob(scheduleJob);
            scheduleJob.setJobpause("1");
        }
        updateJobpause(scheduleJob);
    }

}
