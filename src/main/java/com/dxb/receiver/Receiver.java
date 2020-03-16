package com.dxb.receiver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * 类说明：
 */
@Component
public class Receiver implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String msg = new String(message.getBody());
            System.out.println("Receiver>>>>>>>接收到消息:" + msg);

            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                        false);
                System.out.println("Receiver>>>>>>消息已消费");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                        false, true);
                System.out.println("Receiver>>>>>>拒绝消息，要求Mq重新派发");
                throw e;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
