package com.dxb.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 类说明：
 */
@Component
@RabbitListener(queues = "queue3")
public class Consumer3 {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("Consumer3-Receiver : " + msg);
    }

}
