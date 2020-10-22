package com.sl.quartz.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sl.quartz.dao.JobTaskDao;
import com.sl.quartz.entity.ScheduleJob;

@Repository
public class JobTaskDaoImpl implements JobTaskDao{

	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<ScheduleJob> findAllTask() {
		String sql ="select * from schedule_job";
		return  getResult(sql);
	}

	private List<ScheduleJob> getResult(String sql) {
		return jdbcTemplate.query(sql, new RowMapper<ScheduleJob>() {

			@Override
			public ScheduleJob mapRow(ResultSet rs, int rowNum) throws SQLException {
				ScheduleJob scheduleJob = new ScheduleJob();
				scheduleJob.setJobid(rs.getString("jobid"));
				scheduleJob.setCreatetime(rs.getTimestamp("createtime"));
				scheduleJob.setUpdatetime(rs.getTimestamp("updatetime"));
				scheduleJob.setJobname(rs.getString("jobname"));
				scheduleJob.setJobgroup(rs.getString("jobgroup"));
				scheduleJob.setJobpause(rs.getString("jobpause"));
				scheduleJob.setJobstatus(rs.getString("jobstatus"));
				scheduleJob.setCronexpression(rs.getString("cronexpression"));
				scheduleJob.setDescription(rs.getString("description"));
				scheduleJob.setBeanclass(rs.getString("beanclass"));
				scheduleJob.setMethodname(rs.getString("methodname"));
				scheduleJob.setIsconcurrent(rs.getString("isconcurrent"));
				return scheduleJob;
			}

		});
	}

	@Override
	public ScheduleJob findTaskById(String jobId) {
		String sql ="select * from schedule_job where jobid='"+jobId+"'";
		List<ScheduleJob> result = getResult(sql);
		if(result != null && result.size() > 0){
			return result.get(0);
		}
		return null;
	}

	@Override
	public void updateJobStatus(ScheduleJob job) {
       String sql = "update schedule_job set jobstatus ='"+job.getJobstatus()+"' WHERE jobid = '"+job.getJobid()+"'";
       jdbcTemplate.update(sql);
	}

	@Override
	public void updateJobCron(ScheduleJob job) {
		String sql = "update schedule_job set cronexpression ='"+job.getCronexpression()+"' WHERE jobid = '"+job.getJobid()+"'";
		jdbcTemplate.update(sql);
	}

	@Override
	public void updateJobpause(ScheduleJob scheduleJob) {
		String sql = "update schedule_job set jobpause ='"+scheduleJob.getJobpause()+"' WHERE jobid = '"+scheduleJob.getJobid()+"'";
		jdbcTemplate.update(sql);
	}
	
	@Override
	public void saveScheduleJob(final ScheduleJob scheduleJob) {
		String sql ="insert into schedule_job(jobid,createtime,updatetime,jobname,jobgroup,jobpause,jobstatus,cronexpression,description,beanclass,isconcurrent,springid,methodname)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  jdbcTemplate.update(sql, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, getValue(scheduleJob.getJobid()));
				ps.setTimestamp(2, new Timestamp(scheduleJob.getCreatetime().getTime()));
				ps.setTimestamp(3, scheduleJob.getUpdatetime() == null? null:new Timestamp(scheduleJob.getUpdatetime().getTime()));
				ps.setString(4, getValue(getValue(scheduleJob.getJobname())));
				ps.setString(5, getValue(scheduleJob.getJobgroup()));
				ps.setString(6, getValue(scheduleJob.getJobpause()));
				ps.setString(7, getValue(scheduleJob.getJobstatus()));
				ps.setString(8, getValue(scheduleJob.getCronexpression()));
				ps.setString(9, getValue(scheduleJob.getDescription()));
				ps.setString(10, getValue(scheduleJob.getBeanclass()));
				ps.setString(11, getValue(scheduleJob.getIsconcurrent()));
				ps.setString(12, getValue(scheduleJob.getSpringid()));
				ps.setString(13, getValue(scheduleJob.getMethodname()));
			}
		 });		
	}

	private String getValue(String s){
		if(StringUtils.isNotBlank(s)){
			return s.trim();
		}else{
			return "";
		}
	}
  
}
