package com.sl.quartz.service;

import java.util.List;

import com.sl.quartz.entity.ScheduleJob;
import org.quartz.SchedulerException;

public interface JobTaskService {

    List<ScheduleJob> getAllTask();

    void changeStatus(String jobId, String cmd) throws SchedulerException;

    ScheduleJob getTaskById(String jobId);

    void deleteJob(ScheduleJob job) throws SchedulerException;

    void addJob(ScheduleJob job) throws SchedulerException;

    void updateCron(String jobId, String cron) throws SchedulerException;

    void addTask(ScheduleJob scheduleJob);

    /**
     * 立即执行job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    void runAJobNow(ScheduleJob scheduleJob) throws SchedulerException;

    void updateJobpause(ScheduleJob scheduleJob);

    void changePauseJob(String jobId, String cmd) throws Exception;
}
