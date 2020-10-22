package com.sl.jms.dao;

import java.util.List;

import com.sl.entity.QueueTable;

public interface QueueTableDao {

	int save(QueueTable queueTable);
	
	/**
	 * 查询今天的消息
	 * @return
	 */
	List<QueueTable> queryAll();
	
	/**
	 * 查询所有还没有发送的消息
	 * @return
	 */
	List<QueueTable> queryStatusNotSend();

	void clearMessage();
}
