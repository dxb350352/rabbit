package com.dxb.exchange.back;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 类说明：direct类型交换器的生产者
 */
public class BackProducer {

    public final static String EXCHANGE_NAME = "direct_exchange1";

    public static void main(String[] args)
            throws IOException, TimeoutException {
        //创建连接、连接到RabbitMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置下连接工厂的连接地址(使用默认端口5672)
        connectionFactory.setHost("127.0.0.1");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        // 声明备用交换器
        Map<String, Object> argsMap = new HashMap<String, Object>();
        argsMap.put("alternate-exchange", "Ae_exchange");
        //在信道中设置交换器(在生产者处理)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, false, argsMap);
        //备用交换器
        channel.exchangeDeclare("Ae_exchange", BuiltinExchangeType.FANOUT);
        //申明队列
        String queueName = "queue_1";
        channel.queueDeclare(queueName, false, false, false, null);
        //绑定交换与队列的绑定键
        channel.queueBind(queueName, BackProducer.EXCHANGE_NAME, "red");
        //申明队列
        queueName = "queue_2";
        channel.queueDeclare(queueName, false, false, false, null);
        //绑定交换与队列的绑定键
        channel.queueBind(queueName, "Ae_exchange", "white");


        //申明绑定键\消息体
        String[] routeKeys = {"white", "red"};
        for (int i = 0; i < 4; i++) {
            String routeKey = routeKeys[i % 2];
            String msg = "Hello,RabbitMQ" + (i + 1);
            //发布消息
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, msg.getBytes());
            System.out.println("Sent:" + routeKey + ":" + msg);
        }
        channel.close();
        connection.close();

    }

}
