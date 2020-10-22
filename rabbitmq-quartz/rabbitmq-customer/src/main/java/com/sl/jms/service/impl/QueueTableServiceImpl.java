package com.sl.jms.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sl.entity.QueueTable;
import com.sl.jms.dao.QueueTableDao;
import com.sl.jms.service.QueueTableService;

@Component(value="queueTableService")
public class QueueTableServiceImpl implements QueueTableService{

	@Autowired
	private QueueTableDao queueTableDao;
	
	
	@Override
	public boolean save(QueueTable queueTable) {
		int count = queueTableDao.save(queueTable);
		if(count <= 0) return false;
		return true;
	}


	@Override
	public List<QueueTable> queryAll() {
		return queueTableDao.queryAll();
	}

	@Override
	public Boolean updateStatus(QueueTable queueTable) {
		int count = queueTableDao.updateStatus(queueTable);
		if(count > 0) return true;
		return false;
	}


	@Override
	public void clearMessage() {
		queueTableDao.clearMessage();
	}


	@Override
	public QueueTable quryByEntryDate(Date entryDate) {
		return queueTableDao.quryByEntryDate(entryDate);
	}


	@Override
	public void updateComment(QueueTable queue) {
		 queueTableDao.updateComment(queue);
		
	}
}
