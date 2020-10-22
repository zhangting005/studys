package com.sl.quartz.controller;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;







import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sl.quartz.entity.RetObj;
import com.sl.quartz.entity.ScheduleJob;
import com.sl.quartz.service.JobTaskService;
import com.sl.quartz.util.SpringUtils;


@Controller
@RequestMapping
public class QuartzController {

	@Autowired
	private JobTaskService jobTaskService;
	
	
	@RequestMapping(value = "/index")
	public String test(HttpServletRequest request) {
		List<ScheduleJob> taskList = jobTaskService.getAllTask();
		request.setAttribute("taskList", taskList);
		return "/index";
	}
	
	@RequestMapping(value = "/changeJobStatus", method = RequestMethod.POST)
	@ResponseBody
	public RetObj changeJobStatus(String jobId, String cmd){
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		try {
			jobTaskService.changeStatus(jobId, cmd);
		} catch (SchedulerException e) {
			retObj.setMsg("任务状态改变失败！");
			return retObj;
		}
		retObj.setFlag(true);
		return retObj;
	}
	
	@RequestMapping(value = "/updateCron", method = RequestMethod.POST)
	@ResponseBody
	public RetObj updateCron(String jobId, String cron){
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		try{
			CronScheduleBuilder.cronSchedule(cron);
		}catch(Exception e){
			retObj.setMsg("cron表达式有误，不能被解析！");
			return retObj;
		}
		try{
			jobTaskService.updateCron(jobId, cron);
		}catch(Exception e){
			retObj.setMsg("cron更新失败！");
			return retObj;
		}
		retObj.setFlag(true);
		return retObj;
	}
	
	@RequestMapping(value = "/changeJobPause", method = RequestMethod.POST)
	@ResponseBody
	public RetObj changeJobPause(String jobId, String cmd){
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		if(cmd == null || jobId == null) return retObj;
		try{
			jobTaskService.changePauseJob(jobId, cmd);
		}catch(Exception e){
			e.printStackTrace();
			retObj.setMsg("任务状态改变失败！");
			return retObj;
		}
		retObj.setFlag(true);
		return retObj;
	}
	
	@RequestMapping(value = "/addJob", method = RequestMethod.POST)
	@ResponseBody
	public RetObj addJob(HttpServletRequest request,ScheduleJob scheduleJob){
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		String errorMessage = validate(scheduleJob);
		if(errorMessage != null) {
			retObj.setMsg(errorMessage);
			return retObj;
		}
		try {
			jobTaskService.addTask(scheduleJob);
		} catch (Exception e) {
			e.printStackTrace();
			retObj.setFlag(false);
			retObj.setMsg("保存失败，检查 name group 组合是否有重复！");
			return retObj;
		}
		retObj.setFlag(true);
		List<ScheduleJob> taskList = jobTaskService.getAllTask();
		request.setAttribute("taskList", taskList);
		return retObj;
	}
	
	@RequestMapping(value = "/runAJobNow", method = RequestMethod.POST)
	@ResponseBody
	public RetObj runAJobNow(String jobId){
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		if(StringUtils.isBlank(jobId)) return retObj;
		try{
			ScheduleJob  scheduleJob = this.jobTaskService.getTaskById(jobId);
			jobTaskService.runAJobNow(scheduleJob);
		}catch(Exception e){
			retObj.setMsg("马上执行遇到错误");
			e.printStackTrace();
			return retObj;
		}
		retObj.setFlag(true);
		return retObj; 
	}
	
	
	
	
	
	
	private String validate(ScheduleJob scheduleJob){
		try {
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronexpression());
		} catch (Exception e) {
			return "cron表达式有误，不能被解析！";
		}
		
		Object obj = null;
		try {
			if (StringUtils.isNotBlank(scheduleJob.getSpringid())) {
				obj = SpringUtils.getBean(scheduleJob.getSpringid());
			} else {
				Class clazz = Class.forName(scheduleJob.getBeanclass());
				obj = clazz.newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取对象失败";
		}
		if (obj == null) {
			return "未找到目标类！";
		}else{
			Class clazz = obj.getClass();
			Method method = null;
			try {
				method = clazz.getMethod(scheduleJob.getMethodname(), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (method == null) {
				return "未找到目标方法！";
			}
		}
		return null;
	}
}
