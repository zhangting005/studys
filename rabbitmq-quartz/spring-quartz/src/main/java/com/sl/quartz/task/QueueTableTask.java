package com.sl.quartz.task;

import java.util.List;

import com.sl.entity.QueueTable;
import com.sl.quartz.dao.QueueTableDao;
import com.sl.quartz.enums.TableQueueStatusEnum;
import com.sl.quartz.util.SpringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueTableTask {

    @Autowired
    private AmqpTemplate rabbitAmqpTemplate;

    @Autowired
    private QueueTableDao queueTableDao;

    public void task() {
    	System.out.println("QueueTableTask******");
        if (rabbitAmqpTemplate == null) {
            rabbitAmqpTemplate = (AmqpTemplate) SpringUtils.getBean("rabbitAmqpTemplate");
        }
        if (queueTableDao == null) {
            queueTableDao = (QueueTableDao) SpringUtils.getBean("queueTableDaoImpl");
        }
        List<QueueTable> QueueTableList = queueTableDao.queryStatusNotSend();
        for (QueueTable queueTable : QueueTableList) {
            System.out.println(queueTable.getId());
            if (queueTable.getId() % 2 == 0) {
                //发送消息
                sendMessage("test_mq_exchange", "test_mq_patt", queueTable);
            } else {
                sendMessage("test_mq_exchange", "test_mq_patt_2", queueTable);
            }
            //更新状态
            queueTable.setStatus(TableQueueStatusEnum.HAS_SEND.toString());
            queueTableDao.updateStatus(queueTable);
        }
    }

    private void sendMessage(String exchange, String exchange_key, Object object) {
        // 设置消息持久化，也可以设置消息优先级等
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message)
                throws AmqpException {
                message.getMessageProperties().setDeliveryMode(
                    MessageDeliveryMode.PERSISTENT);
                message.getMessageProperties().setExpiration("60000");//设置消息过期的时间
                return message;
            }
        };
        // convertAndSend 将Java对象转换为消息发送至匹配key的交换机中Exchange
        rabbitAmqpTemplate.convertSendAndReceive(exchange, exchange_key,
            object, messagePostProcessor);
    }
}
