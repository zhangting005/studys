package com.sl.jms.service;

import java.util.List;

import com.sl.entity.QueueTable;

public interface QueueTableService {

	 boolean save(QueueTable queueTable);
	 
	 List<QueueTable> queryStatusNotSend();
	 
	 List<QueueTable> queryAll();

	 void clearMessage();
}
