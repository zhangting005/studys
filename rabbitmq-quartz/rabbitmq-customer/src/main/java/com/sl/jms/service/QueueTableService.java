package com.sl.jms.service;


import java.util.Date;
import java.util.List;

import com.sl.entity.QueueTable;


public interface QueueTableService {

    boolean save(QueueTable queueTable);


    List<QueueTable> queryAll();

    Boolean updateStatus(QueueTable queueTable);


    void clearMessage();


    QueueTable quryByEntryDate(Date entryDate);


    void updateComment(QueueTable queue);

}
