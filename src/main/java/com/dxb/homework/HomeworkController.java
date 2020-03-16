package com.dxb.homework;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: dxb
 * Date: 2020/3/15
 * Description: When I wrote this, only God and I understood what I was doing. Now, God only knows
 */
@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static AtomicInteger countOld = new AtomicInteger();
    private static AtomicInteger countMq = new AtomicInteger();
    private static int maxCount = 9;

    @RequestMapping("/old")
    public String old() {
        int i = countOld.incrementAndGet();
        System.out.println(i);
        if (i > maxCount) {
            countOld.decrementAndGet();
            System.out.println("System Error!!!" + i);
            return "System Error!!!";
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countOld.decrementAndGet();
        return "OK";
    }

    @RequestMapping("/mq")
    public String mq() {
        int i = countMq.incrementAndGet();
        System.out.println(i);
        if (i > maxCount) {
            countMq.decrementAndGet();
            System.out.println("System Error!!!" + i);
            return "System Error!!!";
        }

        rabbitTemplate.convertAndSend("homework", "homework", "homework" + System.currentTimeMillis());

        countMq.decrementAndGet();
        return "OK";
    }


}
