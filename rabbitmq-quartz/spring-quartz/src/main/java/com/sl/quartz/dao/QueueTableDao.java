package com.sl.quartz.dao;

import java.util.List;

import com.sl.entity.QueueTable;


public interface QueueTableDao {

    List<QueueTable> queryStatusNotSend();

    Integer updateStatus(QueueTable queueTable);
}
