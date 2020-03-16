package com.dxb.controller;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类说明： localhost:8080/rabbit/direct
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 普通类型测试
     */
    @GetMapping("/direct")
    public String direct() { //mq的消息发送
        String sendMsg = "direct:" + System.currentTimeMillis();
        System.out.println("DirectSender: " + sendMsg);
        //MQ只接受字节数组， String: springboot做的封装。 生产者 String-》byte数组 \消费者 byte数组-》 String :不推荐。

        this.rabbitTemplate.convertAndSend("DirectExchange", "king.jvm", sendMsg);

        return "发送direct消息成功！";
    }

    /**
     * topic exchange类型rabbitmq测试
     */
    @GetMapping("/topic")
    public String topic() {
        String sendMsg = "Topic:" + System.currentTimeMillis();
        System.out.println("TopicSender: " + sendMsg);
        this.rabbitTemplate.convertAndSend("TopicExchange", "king.jvm", sendMsg);
        return "发送topic消息成功！";
    }

    /**
     * fanout exchange类型rabbitmq测试
     */
    @GetMapping("/fanout")
    public String fanout() {
        String sendMsg = "Fanout MSG:" + System.currentTimeMillis();
        System.out.println("FanoutSender: " + sendMsg);
        this.rabbitTemplate.convertAndSend("FanoutExchange", "", sendMsg);
        return "发送fanout消息成功！";
    }
}
