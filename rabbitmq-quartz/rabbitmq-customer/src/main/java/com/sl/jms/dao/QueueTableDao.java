package com.sl.jms.dao;


import java.util.Date;
import java.util.List;

import com.sl.entity.QueueTable;

public interface QueueTableDao {

    int save(QueueTable queueTable);

    /**
     * 查询今天的消息
     *
     * @return
     */
    List<QueueTable> queryAll();

    Integer updateStatus(final QueueTable queueTable);

    void clearMessage();

    QueueTable quryByEntryDate(Date entryDate);

    void updateComment(QueueTable queue);

}
