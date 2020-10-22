package com.sl.quartz.dao;

import java.util.List;

import com.sl.quartz.entity.ScheduleJob;

public interface JobTaskDao {

    List<ScheduleJob> findAllTask();

    ScheduleJob findTaskById(String jobId);

    void updateJobStatus(ScheduleJob job);

    void updateJobCron(ScheduleJob job);

    void saveScheduleJob(ScheduleJob scheduleJob);

    void updateJobpause(ScheduleJob scheduleJob);

}
