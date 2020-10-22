package com.sl.jms.listener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.rabbitmq.client.Channel;
import com.sl.entity.QueueTable;
import com.sl.entity.SplitWorkOrderCondition;
import com.sl.jms.service.QueueTableService;
import com.sl.jms.util.StatusEnum;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

public class RabbitCumstomerListener implements ChannelAwareMessageListener {

    private static Integer RETRY_COUNT = 1;
    private static Integer BASE_COUNT = 0;
    @Autowired
    private QueueTableService queueTableService;

    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            //1.接收到先保存起来
            QueueTable queueTable;
            queueTable = (QueueTable) toObject(message.getBody());
            queueTable.setStatus(StatusEnum.NOT_HANDLE.getStatus());
            queueTableService.save(queueTable);
            QueueTable queue = queueTableService.quryByEntryDate(queueTable.getEntryDate());
            String request = queueTable.getRequest();
            // 真正的请求数据
            SplitWorkOrderCondition splitWorkOrderCondition = SplitWorkOrderCondition
                .toObject(request);
            // 具体的业务逻辑写在下面，这里是模拟的,暂停3s
            Thread.sleep(3000);
            int originOrderType = splitWorkOrderCondition.getOriginOrderType();
            // 处理失败，重试三次成功
            if (originOrderType == 0 && RETRY_COUNT >= BASE_COUNT) {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                if (queue != null) {
                    int count = BASE_COUNT + 1;
                    queue.setComment("重新尝试" + count + "次");
                    queueTableService.updateComment(queue);
                    BASE_COUNT++;
                }
            } else {
                // 更新状态
                if (queue != null) {
                    queue.setStatus(StatusEnum.SUCCESS_HANDLE.getStatus());
                    queueTableService.updateStatus(queue);
                }
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                RETRY_COUNT = 1;
                BASE_COUNT = 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

}
