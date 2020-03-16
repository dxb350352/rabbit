package com.dxb.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 类说明：
 */
@Component
@RabbitListener(queues = "queue2")
public class Consumer2 {
    @RabbitHandler
    public void process(String msg) {
        System.out.println("Consumer2-Receiver : " + msg);
    }

}
