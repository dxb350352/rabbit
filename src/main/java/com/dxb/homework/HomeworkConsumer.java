package com.dxb.homework;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * User: dxb
 * Date: 2020/3/15
 * Description: When I wrote this, only God and I understood what I was doing. Now, God only knows
 */
@Component
@RabbitListener(queues = "homework")
public class HomeworkConsumer {
    @RabbitHandler
    public void process(String msg) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("handle:" + msg);
    }
}
