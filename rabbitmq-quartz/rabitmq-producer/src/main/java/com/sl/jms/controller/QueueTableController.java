package com.sl.jms.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sl.entity.QueueTable;
import com.sl.entity.SplitWorkOrderCondition;
import com.sl.jms.service.QueueTableService;
import com.sl.jms.util.StatusEnum;

@RequestMapping
@Controller
public class QueueTableController {

	@Autowired
	private QueueTableService queueTableService;
	
	@RequestMapping(value = "/index",method=RequestMethod.GET)
	public String index(HttpServletRequest request){
		List<QueueTable> queueTableList = queueTableService.queryAll();
		request.getSession().setAttribute("queueTableList", queueTableList);
		return "index";
	}
	
	@RequestMapping(value = "/getQueueTable",method=RequestMethod.GET)
	@ResponseBody
	public List<QueueTable> getQueueTables(){
		List<QueueTable> queueTableList = queueTableService.queryAll();
		return queueTableList;
	}
	
	@RequestMapping(value="/sendMessage",method=RequestMethod.POST)
	public String sendMessage(SplitWorkOrderCondition splitWorkOrderCondition){
		QueueTable queueTable = new QueueTable();
		queueTable.setEntryDate(new Date());
		queueTable.setStatus(StatusEnum.NOT_SEND.getStatus());
		queueTable.setType("split");
		queueTable.setRequest(splitWorkOrderCondition.toJson());
		queueTableService.save(queueTable);
		return "redirect:index";
	}
	
	@RequestMapping(value = "/resetMessage",method=RequestMethod.GET)
	public String resetMessage(){
		queueTableService.clearMessage();
		return "index";
	}
}
